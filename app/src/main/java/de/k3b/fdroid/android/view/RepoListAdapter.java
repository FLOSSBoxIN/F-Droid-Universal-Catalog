/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.fdroid.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.html.AndroidStringResourceMustacheContext;
import de.k3b.fdroid.android.html.util.HtmlUtil;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.service.RepoIconService;
import de.k3b.fdroid.html.service.FormatService;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {
    private static final String TAG = Global.LOG_TAG + "RepoList";

    private final FormatService formatService;
    private final List<Repo> details;
    private final int defaultBackgroundColor;
    private final int defaultForegroundColor;
    private final RepoIconService iconService;
    private final Executor threadExecutor;

    /**
     * Initialize the dataset of the Adapter.
     */
    public RepoListAdapter(Context context, List<Repo> details,
                           RepoIconService iconService, Executor threadExecutor) {
        this.details = details;
        formatService = new FormatService("list_repo", Repo.class,
                new AndroidStringResourceMustacheContext(context));

        this.defaultBackgroundColor = HtmlUtil.getDefaultBackgroundColor(context);
        this.defaultForegroundColor = HtmlUtil.getDefaultForegroundColor(context);
        this.iconService = iconService;
        this.threadExecutor = threadExecutor;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_row_item_separate_icon, viewGroup, false);

        return new ViewHolder(v, defaultBackgroundColor, defaultForegroundColor);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        Repo repo = details.get(position);
        viewHolder.repo = repo;
        viewHolder.getTitle().setText(repo.getName() + "(" +
                repo.getLastAppCount() + ")");

        String html = formatService.format(repo);
        HtmlUtil.setHtml(viewHolder.getDescrtiption(), html, defaultForegroundColor, defaultBackgroundColor, null);


        bindIcon(viewHolder, viewHolder.repo);
    }

    private void bindIcon(ViewHolder viewHolder, Repo repo) {
        File iconFile = iconService.getLocalImageFile(repo);

        if (!iconService.error(iconFile)) {
            // (icon download ok) -> nothing to do
            setIcon(viewHolder, iconFile);
        } else if (iconFile == null || iconFile.exists()) {
            // (no icon defined) || (error download) -> nothing to do
            setIcon(viewHolder, null);
        } else {
            // (icon not downloaded yet) || (download error >= 24h)
            setIcon(viewHolder, null);
            threadExecutor.execute(() -> {
                File localIconFile = iconService.getOrDownloadLocalImageFile(repo);
                if (localIconFile != null) {
                    viewHolder.getIcon().post(() -> setIcon(viewHolder, localIconFile)
                    );
                }
            });
        }
    }

    private void setIcon(ViewHolder viewHolder, File iconFile) {
        ImageView icon = viewHolder.getIcon();
        if (iconFile != null) {
            icon.setImageURI(Uri.fromFile(iconFile));
        } else {
            icon.setImageURI(null);
        }
        icon.invalidate();
    }


    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final TextView title;
        private final TextView descrtiption;
        private final Button button;
        Repo repo;

        public ViewHolder(View v, int defaultBackgroundColor, int defaultForegroundColor) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(v1 -> getActivity(v1).onRepoClick(repo));
            icon = v.findViewById(R.id.icon);
            title = v.findViewById(R.id.title);
            title.setBackgroundColor(defaultBackgroundColor);
            descrtiption = v.findViewById(R.id.description);
            button = v.findViewById(R.id.button);
            button.setOnClickListener(v1 -> getActivity(v1).onRepoButtonClick(button, repo));
        }

        private RepoListActivity getActivity(View v1) {
            return (RepoListActivity) v1.getContext();
        }

        public ImageView getIcon() {
            return icon;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDescrtiption() {
            return descrtiption;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return details.size();
    }
}
