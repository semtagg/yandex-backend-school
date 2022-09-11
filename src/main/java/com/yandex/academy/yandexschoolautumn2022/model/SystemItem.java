package com.yandex.academy.yandexschoolautumn2022.model;

import java.util.Date;
import java.util.List;

public class SystemItem {
    private String id;

    private String url;

    private Date date;

    private String parentId;

    private SystemItemType type;

    private Long size;

    private List<SystemItem> children;
}
