package com.yandex.academy.yandexschoolautumn2022.repository;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface CustomRepository extends RevisionRepository<SystemItemDb, String, Integer>, CrudRepository<SystemItemDb, String> {
    ArrayList<SystemItemDb> getAllByParentid(String parentid);

    ArrayList<SystemItemDb> getAllById(String id);

    @Query("SELECT f FROM SystemItemDb f " +
            "WHERE (CAST(f.date AS date) BETWEEN CAST(:datebefore AS date) AND CAST(:dateafter AS date )) " +
            "AND f.type = com.yandex.academy.yandexschoolautumn2022.model.SystemItemType.FILE")
    ArrayList<SystemItemDb> getAllFilesBetween(@Param("datebefore") String dateBefore, @Param("dateafter") String dateAfter);

    @Modifying
    @Transactional
    @Query("delete from com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb_AUD f where f.originalId.id = :userid")
    void deleteFromAud(@Param("userid") String userId);

    @Query(value = "SELECT f from system_item_db f where f.parentid = :userid " +
            "AND (CAST(f.date AS date) BETWEEN CAST(:datebefore AS date) AND CAST(:dateafter AS date ))" +
            "ORDER BY f.date desc limit 1", nativeQuery = true)
    ArrayList<SystemItemDb> getAllBetween(@Param("userid") String userId, @Param("datebefore") String dateBefore, @Param("dateafter") String dateAfter);
}
