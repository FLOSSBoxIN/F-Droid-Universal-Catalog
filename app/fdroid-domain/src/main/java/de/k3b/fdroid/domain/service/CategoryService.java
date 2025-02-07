/*
 * Copyright (c) 2022-2023 by k3b.
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
package de.k3b.fdroid.domain.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.repository.CategoryRepository;

/**
 * Service to cache/find/insert Category info.
 */

public class CategoryService extends CacheServiceInteger<Category> {
    @Nullable
    private final CategoryRepository categoryRepository;

    Map<String, Category> name2Category;

    // used to generate fake category-id-s if categoryRepository is null
    private int mockId = 12100;

    public CategoryService(@Nullable CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryService init() {
        List<Category> all = (categoryRepository == null) ? null : categoryRepository.findAll();
        init(all);
        return this;
    }

    protected void init(@Nullable List<Category> itemList) {
        name2Category = new HashMap<>();
        super.init(itemList);
    }

    protected void init(Category category) {
        super.init(category);
        name2Category.put(category.getName(), category);
    }

    public String getCategoryName(int categoryId) {
        Category category = (categoryId == 0) ? null : getItemById(categoryId);
        return (category == null) ? null : category.getName();
    }

    @NotNull
    public Category getOrCreateCategory(@NotNull String categoryName) {
        Category category = name2Category.get(categoryName);
        if (category == null) {
            // create on demand
            category = new Category();
            category.setName(categoryName);
            if (categoryRepository != null) {
                categoryRepository.insert(category);
            } else {
                category.setId(mockId++);
            }
            init(category);
        }
        return category;
    }

    public int getOrCreateCategoryId(@NotNull String categoryName) {
        return getOrCreateCategory(categoryName).getId();
    }

    @Override
    public String toString() {
        return "CategoryService{" +
                "name2Category=" + name2Category +
                ", mockId=" + mockId +
                '}';
    }
}
