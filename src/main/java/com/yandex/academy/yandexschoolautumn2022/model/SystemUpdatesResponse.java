package com.yandex.academy.yandexschoolautumn2022.model;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;

import java.util.ArrayList;

public class SystemUpdatesResponse {
    private ArrayList<SystemItemDB> items;

    public SystemUpdatesResponse( ) {
    }

    public ArrayList<SystemItemDB> getItems() {
        return items;
    }

    public void setItems(ArrayList<SystemItemDB> items) {
        this.items = items;
    }
}
