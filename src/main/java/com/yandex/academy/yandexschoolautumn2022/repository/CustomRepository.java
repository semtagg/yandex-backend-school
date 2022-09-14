package com.yandex.academy.yandexschoolautumn2022.repository;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;
import org.hibernate.annotations.Subselect;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface CustomRepository extends RevisionRepository<SystemItemDB, String, Integer>, CrudRepository<SystemItemDB, String> {
    ArrayList<SystemItemDB> getAllByParentId(String parentId);
    ArrayList<SystemItemDB> getAllById(String id);
    @Query("SELECT f FROM SystemItemDB f " +
            "WHERE (CAST(f.date AS date) BETWEEN CAST(:datebefore AS date) AND CAST(:dateafter AS date )) " +
            "AND f.type = com.yandex.academy.yandexschoolautumn2022.model.SystemItemType.FILE")
    ArrayList<SystemItemDB> getAllBetween(@Param("datebefore") String dateBefore, @Param("dateafter") String dateAfter);
    @Modifying
    @Transactional
    @Query("delete from com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB_AUD u where u.originalId.id = :userid")
    void deleteFromAud(@Param("userid") String userId);


}
