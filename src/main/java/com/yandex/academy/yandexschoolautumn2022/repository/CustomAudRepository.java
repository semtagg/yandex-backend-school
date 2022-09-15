package com.yandex.academy.yandexschoolautumn2022.repository;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDbAud;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface CustomAudRepository extends CrudRepository<SystemItemDbAud, Long> {
    @Modifying
    @Transactional
    @Query("delete from SystemItemDbAud f where f.id = :userid")
    void deleteFromAud(@Param("userid") String userId);

    @Query("SELECT f FROM SystemItemDbAud f " +
            "WHERE CAST(f.date as timestamp) BETWEEN CAST(:datebefore as timestamp) AND CAST(:dateafter as timestamp) " +
            "AND f.type = com.yandex.academy.yandexschoolautumn2022.model.SystemItemType.FILE")
    ArrayList<SystemItemDbAud> getAllFilesBetween(@Param("datebefore") String dateBefore, @Param("dateafter") String dateAfter);

    @Query("SELECT f FROM SystemItemDbAud f WHERE f.id = :userid " +
            "AND CAST(f.date as timestamp) BETWEEN CAST(:datebefore as timestamp) AND CAST(:dateafter as timestamp)")
    ArrayList<SystemItemDbAud> getAllBetween(@Param("userid") String userId, @Param("datebefore") String dateBefore, @Param("dateafter") String dateAfter);

    @Query("SELECT f FROM SystemItemDbAud f where f.parentId = :userid " +
            "AND CAST(f.date as timestamp) BETWEEN CAST(:datebefore as timestamp) AND CAST(:dateafter as timestamp)" +
            "ORDER BY f.date desc")
    ArrayList<SystemItemDbAud> getAllChildrenBetween(@Param("userid") String userId, @Param("datebefore") String dateBefore, @Param("dateafter") String dateAfter);

}
