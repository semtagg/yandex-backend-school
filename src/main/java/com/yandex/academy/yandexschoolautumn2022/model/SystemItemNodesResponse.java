package com.yandex.academy.yandexschoolautumn2022.model;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb;

import java.util.ArrayList;

public class SystemItemNodesResponse extends SystemItemDb {
    private ArrayList<SystemItemNodesResponse> children;

    public SystemItemNodesResponse() {
        //super();
    }

    public ArrayList<SystemItemNodesResponse> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SystemItemNodesResponse> children) {
        this.children = children;
    }
}
