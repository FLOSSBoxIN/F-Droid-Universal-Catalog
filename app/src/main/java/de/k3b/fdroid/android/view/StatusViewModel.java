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

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.WorkInfo;

import de.k3b.fdroid.android.service.ImportV1AndroidWorker;

public class StatusViewModel extends ViewModel {
    private final MutableLiveData<String> status = new MutableLiveData<>();

    protected void onStatusChange(WorkInfo workInfo) {
        String progress = null;
        if (workInfo != null) {
            progress = workInfo.getProgress().getString(ImportV1AndroidWorker.KEY_PROGRESS);
        }
        getStatus().setValue(progress == null ? "" : progress);
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }
}
