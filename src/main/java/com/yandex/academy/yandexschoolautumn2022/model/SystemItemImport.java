package com.yandex.academy.yandexschoolautumn2022.model;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

public class SystemItemImport {
    public static SystemItemDB ToSystemItemDB(SystemItemImport item, String date){
        SystemItemDB model = new SystemItemDB();
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(date);
        Instant i = Instant.from(ta);

        model.setId(item.getId());
        model.setDate(Date.from(i));
        model.setSize(item.getSize());
        model.setType(item.getType());
        model.setParentId(item.getParentId());
        model.setUrl(model.getUrl());

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
