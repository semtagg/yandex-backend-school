package com.yandex.academy.yandexschoolautumn2022.repository;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CustomRepository extends CrudRepository<SystemItemDb, String> {
    ArrayList<SystemItemDb> getAllByParentId(String parentId);
}
