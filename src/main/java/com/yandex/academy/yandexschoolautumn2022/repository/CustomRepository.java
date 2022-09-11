package com.yandex.academy.yandexschoolautumn2022.repository;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CustomRepository extends CrudRepository<SystemItemDB, String> {
    ArrayList<SystemItemDB> getAllByParentId(String parentId);
    ArrayList<SystemItemDB> getAllById(String id);
}
