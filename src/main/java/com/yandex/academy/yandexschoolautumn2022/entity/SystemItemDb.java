package com.yandex.academy.yandexschoolautumn2022.entity;

import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemNodesResponse;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class SystemItemDb {
    public static SystemItemNodesResponse toSystemNodesResponse(SystemItemDb itemDB, ArrayList<SystemItemNodesResponse> children) {
        SystemItemNodesResponse result = new SystemItemNodesResponse();
        result.setParentId(itemDB.getParentId());
        result.setSize(itemDB.getSize());
        result.setDate(itemDB.getDate());
        result.setId(itemDB.getId());
        result.setUrl(itemDB.getUrl());
        result.setType(itemDB.getType());
        result.setChildren(children);

        return result;
    }

    public static SystemItemDbAud toSystemItemDbAud(SystemItemDb itemDB) {
        SystemItemDbAud result = new SystemItemDbAud();
        result.setParentId(itemDB.getParentId());
        result.setSize(itemDB.getSize());
        result.setDate(itemDB.getDate());
        result.setId(itemDB.getId());
        result.setUrl(itemDB.getUrl());
        result.setType(itemDB.getType());

        return result;
    }

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    private String url;

    @Column(nullable = false)
    private String date;

    private String parentId;

    @Column(nullable = false)
    private SystemItemType type;

    private Long size;

    public SystemItemDb() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public SystemItemType getType() {
        return type;
    }

    public void setType(SystemItemType type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
