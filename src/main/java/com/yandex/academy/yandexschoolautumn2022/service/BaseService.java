package com.yandex.academy.yandexschoolautumn2022.service;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDb;
import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDbAud;
import com.yandex.academy.yandexschoolautumn2022.model.*;
import com.yandex.academy.yandexschoolautumn2022.model.Error;
import com.yandex.academy.yandexschoolautumn2022.repository.CustomAudRepository;
import com.yandex.academy.yandexschoolautumn2022.repository.CustomRepository;
import com.yandex.academy.yandexschoolautumn2022.utils.Tuple2;
import com.yandex.academy.yandexschoolautumn2022.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseService {
    @Autowired
    private CustomRepository repository;
    @Autowired
    private CustomAudRepository repositoryAud;

    private void update(String id, String date) {
        Optional<SystemItemDb> parent = repository.findById(id);
        if (parent.isPresent()) {
            TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(date);
            TemporalAccessor ta1 = DateTimeFormatter.ISO_INSTANT.parse(parent.get().getDate());
            Instant i = Instant.from(ta);
            Instant i1 = Instant.from(ta1);

            if (Date.from(i).compareTo(Date.from(i1)) > 0) {
                parent.get().setDate(date);

                repositoryAud.save(SystemItemDb.toSystemItemDbAud(parent.get()));
                repository.save(parent.get());
            }

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
                    Optional<SystemItemDb> parentDB = repository.findById(item.getParentId());

                    if (parentDB.isPresent() && parentDB.get().getType() == SystemItemType.FILE) {
                        result.setCode(400);
                        result.setMessage("Validation Failed");
                        return result;
                    }
                }
            }

            Optional<SystemItemDb> itemDB = repository.findById(item.getId());

            if (itemDB.isPresent() && itemDB.get().getType() != item.getType()) {
                result.setCode(400);
                result.setMessage("Validation Failed");
                return result;

            }
        }

        for (SystemItemImport item : items) {
            SystemItemDb model = SystemItemImport.ToSystemItemDB(item, date);

            if (item.getParentId() != null) {
                update(item.getParentId(), date);
            }

            repositoryAud.save(SystemItemDb.toSystemItemDbAud(model));
            repository.save(model);
        }

        result.setCode(200);
        return result;
    }

    private void deleteSubfolder(String id) {
        ArrayList<SystemItemDb> items = repository.getAllByParentId(id);

        for (SystemItemDb item : items) {
            if (item.getType() == SystemItemType.FOLDER)
                deleteSubfolder(item.getId());

            repository.delete(item);
            repositoryAud.deleteFromAud(item.getId());
        }
    }

    public Error delete(String id, String date) {
        Error result = new Error();

        if (!Utils.isIsoDate(date)) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        Optional<SystemItemDb> item = repository.findById(id);
        if (item.isEmpty()) {
            result.setCode(404);
            result.setMessage("Item not found");
            return result;
        }

        if (item.get().getType() == SystemItemType.FOLDER) {
            deleteSubfolder(item.get().getId());
        }

        repository.delete(item.get());
        repositoryAud.deleteFromAud(item.get().getId());

        result.setCode(200);
        return result;
    }

    private Tuple2<Long, ArrayList<SystemItemNodesResponse>> getChildren(String id) {
        ArrayList<SystemItemNodesResponse> children = new ArrayList<>();
        ArrayList<SystemItemDb> itemsDB = repository.getAllByParentId(id);
        Long size = 0L;

        for (SystemItemDb item : itemsDB) {
            SystemItemNodesResponse response = SystemItemDb.toSystemNodesResponse(item, null);
            if (item.getType() == SystemItemType.FOLDER) {
                Tuple2<Long, ArrayList<SystemItemNodesResponse>> tuple = getChildren(item.getId());
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

    public ErrorResponse<SystemItemNodesResponse> nodes(String id) {
        ErrorResponse<SystemItemNodesResponse> result = new ErrorResponse<>();

        Optional<SystemItemDb> item = repository.findById(id);
        if (item.isEmpty()) {
            result.setCode(404);
            result.setMessage("Item not found");
            return result;
        }

        SystemItemNodesResponse response = SystemItemDb.toSystemNodesResponse(item.get(), null);
        if (item.get().getType() == SystemItemType.FOLDER) {
            Tuple2<Long, ArrayList<SystemItemNodesResponse>> tuple = getChildren(item.get().getId());
            response.setChildren(tuple.getSecond());
            response.setSize(tuple.getFirst());
        }

        result.setCode(200);
        result.setResponse(response);
        return result;
    }


    public ErrorResponse<SystemItemUpdatesResponse> updates(String date) {
        ErrorResponse<SystemItemUpdatesResponse> result = new ErrorResponse<>();

        if (!Utils.isIsoDate(date)) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        String dateBefore = Utils.removeOneDay(date);
        ArrayList<SystemItemDbAud> itemsAud = repositoryAud.getAllFilesBetween(dateBefore, date);
        ArrayList<SystemItemDb> items = new ArrayList<>();
        for (SystemItemDbAud item : itemsAud) {
            items.add(SystemItemDbAud.toSystemItemDb(item));
        }
        SystemItemUpdatesResponse response = new SystemItemUpdatesResponse();
        response.setItems(items);

        result.setCode(200);
        result.setResponse(response);
        return result;
    }

    private Long getFolderSize(String id, String dateStart, String dateEnd) {
        Long size = 0L;
        ArrayList<SystemItemDbAud> items = repositoryAud.getAllChildrenBetween(id, dateStart, dateEnd);
        HashSet<String> used = new HashSet<>();

        for (SystemItemDbAud item : items) {
            if (!used.contains(item.getId())){
                used.add(item.getId());

                if (item.getType() == SystemItemType.FOLDER) {
                    size += getFolderSize(item.getId(), dateStart, item.getDate());
                } else {
                    size += item.getSize();
                }
            }
        }

        return size;
    }

    public ErrorResponse<SystemItemUpdatesResponse> history(String id, String dateStart, String dateEnd) {
        ErrorResponse<SystemItemUpdatesResponse> result = new ErrorResponse<>();

        if (!Utils.isIsoDate(dateStart) || !Utils.isIsoDate(dateEnd)) {
            result.setCode(400);
            result.setMessage("Validation Failed");
            return result;
        }

        ArrayList<SystemItemDbAud> itemsAud = repositoryAud.getAllBetween(id, dateStart, dateEnd);

        if (itemsAud.isEmpty()) {
            result.setCode(404);
            result.setMessage("Item not found");
            return result;
        }

        for (SystemItemDbAud item : itemsAud) {
            if (item.getType() == SystemItemType.FOLDER) {
                item.setSize(getFolderSize(item.getId(), dateStart, item.getDate()));
            }
        }

        ArrayList<SystemItemDb> items = new ArrayList<>();
        for (SystemItemDbAud aud : itemsAud) {
            items.add(SystemItemDbAud.toSystemItemDb(aud));
        }

        SystemItemUpdatesResponse response = new SystemItemUpdatesResponse();
        response.setItems(items);

        result.setCode(200);
        result.setResponse(response);
        return result;
    }
}
