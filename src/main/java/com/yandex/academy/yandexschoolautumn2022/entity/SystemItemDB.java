package com.yandex.academy.yandexschoolautumn2022.entity;

import com.yandex.academy.yandexschoolautumn2022.model.SystemItem;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;
import com.yandex.academy.yandexschoolautumn2022.model.SystemNodesResponse;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Audited
public class SystemItemDB {
    public static SystemNodesResponse toSystemNodesResponse(SystemItemDB itemDB, ArrayList<SystemNodesResponse> children) {
        SystemNodesResponse result = new SystemNodesResponse();
        result.setParentId(itemDB.getParentId());
        result.setSize(itemDB.getSize());
        result.setDate(itemDB.getDate());
        result.setId(itemDB.getId());
        result.setUrl(itemDB.getUrl());
        result.setType(itemDB.getType());
        result.setChildren(children);

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

    public SystemItemDB() {
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
