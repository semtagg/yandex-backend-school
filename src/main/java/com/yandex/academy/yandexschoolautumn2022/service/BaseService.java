package com.yandex.academy.yandexschoolautumn2022.service;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;
import com.yandex.academy.yandexschoolautumn2022.model.*;
import com.yandex.academy.yandexschoolautumn2022.model.Error;
import com.yandex.academy.yandexschoolautumn2022.repository.CustomRepository;
import com.yandex.academy.yandexschoolautumn2022.utils.Tuple2;
import com.yandex.academy.yandexschoolautumn2022.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseService {
    @Autowired
    private CustomRepository repository;

    private void update(String id, String date) {
        Optional<SystemItemDB> parent = repository.findById(id);
        if (parent.isPresent()) {
            TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(date);
            TemporalAccessor ta1 = DateTimeFormatter.ISO_INSTANT.parse(parent.get().getDate());
            Instant i = Instant.from(ta);
            Instant i1 = Instant.from(ta1);

            if (Date.from(i).compareTo(Date.from(i1)) >= 0)
                parent.get().setDate(date);

            if (parent.get().getParentId() != null) {
                update(parent.get().getParentId(), date);
            }
        }
    }

    public Error imports(List<SystemItemImport> items, String date) {
        Error result = new Error();

        if (!Utils.isIsoDate(date)) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        Set<String> hs = items.stream().map(SystemItemImport::getId).collect(Collectors.toSet());
        if (hs.size() != items.size()) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        for (SystemItemImport item : items) {
            if (item.getId() == null
                    || (item.getType() == SystemItemType.FOLDER && item.getUrl() != null)
                    || (item.getType() == SystemItemType.FILE && !(item.getUrl().length() <= 255))
                    || (item.getType() == SystemItemType.FOLDER && item.getSize() != null)
                    || (item.getType() == SystemItemType.FILE && (item.getSize() == null || item.getSize() <= 0))
            ) {
                result.setCode(400);
                result.setMessage("Validation Failed");
                return result;
            }
        }

        for (SystemItemImport item : items) {
            if (item.getType() == SystemItemType.FILE) {
                Optional<SystemItemImport> parent = items
                        .stream()
                        .filter(i -> i.getId().equals(item.getParentId()))
                        .findAny();

                if (parent.isPresent() && parent.get().getType() == SystemItemType.FILE) {
                    result.setCode(400);
                    result.setMessage("Validation Failed");
                    return result;
                } else {
                    Optional<SystemItemDB> parentDB = repository.findById(item.getParentId());

                    if (parentDB.isPresent() && parentDB.get().getType() == SystemItemType.FILE) {
                        result.setCode(400);
                        result.setMessage("Validation Failed");
                        return result;
                    }
                }
            }

            Optional<SystemItemDB> itemDB = repository.findById(item.getId());

            if (itemDB.isPresent() && itemDB.get().getType() != item.getType()) {
                result.setCode(400);
                result.setMessage("Validation Failed");
                return result;

            }
        }

        for (SystemItemImport item : items) {
            SystemItemDB model = SystemItemImport.ToSystemItemDB(item, date);
            if (item.getParentId() != null) {
                update(item.getParentId(), date);
            }
            repository.save(model);
        }

        result.setCode(200);
        return result;
    }

    private void deleteSubfolder(String id) {
        ArrayList<SystemItemDB> items = repository.getAllByParentId(id);

        for (SystemItemDB item : items) {
            if (item.getType() == SystemItemType.FOLDER)
                deleteSubfolder(item.getId());

            repository.deleteFromAud(item.getId());
            repository.delete(item);
        }
    }

    public Error delete(String id, String date) {
        Error result = new Error();

        if (!Utils.isIsoDate(date)) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        Optional<SystemItemDB> item = repository.findById(id);
        if (item.isEmpty()) {
            result.setCode(404);
            result.setMessage("Item not found");
            return result;
        }

        if (item.get().getType() == SystemItemType.FOLDER) {
            deleteSubfolder(item.get().getId());
        }
        repository.deleteFromAud(item.get().getId());
        repository.delete(item.get());

        result.setCode(200);
        return result;
    }

    private Tuple2<Long, ArrayList<SystemNodesResponse>> getChildren(String id) {
        ArrayList<SystemNodesResponse> children = new ArrayList<>();
        ArrayList<SystemItemDB> itemsDB = repository.getAllByParentId(id);
        Long size = 0L;

        for (SystemItemDB item : itemsDB) {
            SystemNodesResponse response = SystemItemDB.toSystemNodesResponse(item, null);
            if (item.getType() == SystemItemType.FOLDER) {
                Tuple2<Long, ArrayList<SystemNodesResponse>> tuple = getChildren(item.getId());
                size += tuple.getFirst();

                response.setSize(tuple.getFirst());
                response.setChildren(tuple.getSecond());
            } else {
                size += item.getSize();
            }

            children.add(response);
        }

        return new Tuple2<>(size, children);
    }

    public ErrorResponse<SystemNodesResponse> nodes(String id) {
        ErrorResponse<SystemNodesResponse> result = new ErrorResponse<>();

        Optional<SystemItemDB> item = repository.findById(id);
        if (item.isEmpty()) {
            result.setCode(404);
            result.setMessage("Item not found");
            return result;
        }

        SystemNodesResponse response = SystemItemDB.toSystemNodesResponse(item.get(), null);
        if (item.get().getType() == SystemItemType.FOLDER) {
            Tuple2<Long, ArrayList<SystemNodesResponse>> tuple = getChildren(item.get().getId());
            response.setChildren(tuple.getSecond());
            response.setSize(tuple.getFirst());
        }

        result.setCode(200);
        result.setResponse(response);
        return result;
    }


    public ErrorResponse<SystemUpdatesResponse> updates(String date){
        ErrorResponse<SystemUpdatesResponse> result = new ErrorResponse<>();

        if (!Utils.isIsoDate(date)) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        String dateBefore = Utils.removeOneDay(date);
        ArrayList<SystemItemDB> items = repository.getAllBetween(dateBefore, date);
        SystemUpdatesResponse response = new SystemUpdatesResponse();
        response.setItems(items);

        result.setCode(200);
        result.setResponse(response);
        return result;
    }
}
