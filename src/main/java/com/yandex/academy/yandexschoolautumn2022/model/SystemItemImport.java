package com.yandex.academy.yandexschoolautumn2022.model;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb;

public class SystemItemImport {
    public static SystemItemDb ToSystemItemDB(SystemItemImport item, String date){
        SystemItemDb model = new SystemItemDb();
        model.setId(item.getId());
        model.setDate(date);
        model.setSize(item.getSize());
        model.setType(item.getType());
        model.setParentid(item.getParentId());
        model.setUrl(item.getUrl());

        return model;
    }

    private String id;

    private String url;

    private String parentId;

    private SystemItemType type;

    private Long size;

    public SystemItemImport() {
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public SystemItemType getType() {
        return type;
    }

    public void setType(SystemItemType type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
