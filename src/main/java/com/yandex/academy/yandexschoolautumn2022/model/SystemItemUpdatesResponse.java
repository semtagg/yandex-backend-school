package com.yandex.academy.yandexschoolautumn2022.model;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb;

import java.util.ArrayList;

public class SystemItemUpdatesResponse {
    private ArrayList<SystemItemDb> items;

    public SystemItemUpdatesResponse( ) {
    }

    public ArrayList<SystemItemDb> getItems() {
        return items;
    }

    public void setItems(ArrayList<SystemItemDb> items) {
        this.items = items;
    }
}
