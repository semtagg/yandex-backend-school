package com.yandex.academy.yandexschoolautumn2022.entity;

import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "yandex_files")
public class SystemItemDB {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String parentId;

    @Column(nullable = false)
    private SystemItemType type;

    private Long size;

    public SystemItemDB(){
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
