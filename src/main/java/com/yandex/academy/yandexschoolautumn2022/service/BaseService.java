package com.yandex.academy.yandexschoolautumn2022.service;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;
import com.yandex.academy.yandexschoolautumn2022.model.Error;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemImport;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;
import com.yandex.academy.yandexschoolautumn2022.repository.CustomRepository;
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
        var result = new Error();

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
                    || (item.getType() == SystemItemType.FILE && item.getUrl().length() > 255)
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
            deleteSubfolder(item.getId());
            repository.delete(item);
        }
    }

    public void delete(String id, Date date) {
        SystemItemDB item = repository.findById(id).get();

        deleteSubfolder(item.getId());
        if (item.getType() == SystemItemType.FOLDER) {
            deleteSubfolder(item.getId());
        }
        repository.delete(item);
    }

    private void getChildren(String id, ArrayList<SystemItemDB> items) {
        ArrayList<SystemItemDB> itemsDB = repository.getAllByParentId(id);
        if (itemsDB != null) items.addAll(itemsDB);

        for (SystemItemDB item : itemsDB) {
            getChildren(item.getId(), items);
        }
    }

    public ArrayList<SystemItemDB> nodes(String id) {
        SystemItemDB item = repository.findById(id).get();
        ArrayList<SystemItemDB> items = new ArrayList<>();
        items.add(item);
        getChildren(item.getId(), items);

        return items;
    }
}
