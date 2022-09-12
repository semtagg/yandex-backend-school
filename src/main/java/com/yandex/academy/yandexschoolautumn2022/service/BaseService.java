package com.yandex.academy.yandexschoolautumn2022.service;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;
import com.yandex.academy.yandexschoolautumn2022.model.*;
import com.yandex.academy.yandexschoolautumn2022.model.Error;
import com.yandex.academy.yandexschoolautumn2022.repository.CustomRepository;
import com.yandex.academy.yandexschoolautumn2022.utils.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
public class BaseService {
    @Autowired
    private CustomRepository repository;

    private boolean isIsoDate(String date) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void update(SystemItemDB newModel, SystemItemDB oldModel) {

    }

    public Error imports(List<SystemItemImport> items, String date) {
        Error result = new Error();

        if (!isIsoDate(date)) {
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

                if (parent.isPresent()) {
                    result.setCode(400);
                    result.setMessage("Validation Failed");
                    return result;
                } else {
                    Optional<SystemItemDB> parentDB = repository.findById(item.getParentId());

                    if (parentDB.isPresent()) {
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

            repository.delete(item);
        }
    }

    public Error delete(String id, String date) {
        Error result = new Error();

        if (!isIsoDate(date)) {
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
                Tuple2<Long, ArrayList<SystemNodesResponse>> tuple = getChildren(item.getParentId());
                size += tuple.getFirst();

                response.setSize(tuple.getFirst());
                response.setChildren(tuple.getSecond());
            }

            children.add(response);
        }

        return new Tuple2<>(size, children);
    }

    public ErrorResponse nodes(String id) {
        ErrorResponse result = new ErrorResponse();

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
}
