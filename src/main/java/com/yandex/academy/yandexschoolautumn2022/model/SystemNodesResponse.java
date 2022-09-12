package com.yandex.academy.yandexschoolautumn2022.model;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;

import java.util.ArrayList;

public class SystemNodesResponse extends SystemItemDB {
    private ArrayList<SystemNodesResponse> children;

    public SystemNodesResponse() {
        //super();
    }

    public ArrayList<SystemNodesResponse> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SystemNodesResponse> children) {
        this.children = children;
    }
}
