package com.yandex.academy.yandexschoolautumn2022.entity;

import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SystemItemDbAud {
    public static SystemItemDb toSystemItemDb(SystemItemDbAud itemAud) {
        SystemItemDb result = new SystemItemDb();
        result.setParentId(itemAud.getParentId());
        result.setSize(itemAud.getSize());
        result.setDate(itemAud.getDate());
        result.setId(itemAud.getId());
        result.setUrl(itemAud.getUrl());
        result.setType(itemAud.getType());

        return result;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    private String id;

    private String url;

    private String date;

    private String parentId;

    private SystemItemType type;

    private Long size;

    public SystemItemDbAud() {
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SystemItemDbAud other = (SystemItemDbAud) obj;
        var result = Objects.equals(id, other.id);
        return result;
    }
}
