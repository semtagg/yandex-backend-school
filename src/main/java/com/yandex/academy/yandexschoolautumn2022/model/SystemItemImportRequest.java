package com.yandex.academy.yandexschoolautumn2022.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class SystemItemImportRequest {
    private List<SystemItemImport> items;

    private String updateDate;

    public SystemItemImportRequest() {
    }

    public List<SystemItemImport> getItems() {
        return items;
    }

    public void setItems(List<SystemItemImport> items) {
        this.items = items;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
