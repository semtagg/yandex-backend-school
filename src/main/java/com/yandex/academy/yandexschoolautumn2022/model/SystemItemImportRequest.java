package com.yandex.academy.yandexschoolautumn2022.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class SystemItemImportRequest {
    private List<SystemItemImport> items;

    private Date updateDate;

    public SystemItemImportRequest() {
    }

    public List<SystemItemImport> getItems() {
        return items;
    }

    public void setItems(List<SystemItemImport> items) {
        this.items = items;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
