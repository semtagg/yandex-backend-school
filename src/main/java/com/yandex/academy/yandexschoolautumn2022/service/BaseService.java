package com.yandex.academy.yandexschoolautumn2022.service;

import com.yandex.academy.yandexschoolautumn2022.entity.SystemItemDB;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemImport;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;
import com.yandex.academy.yandexschoolautumn2022.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BaseService {
    @Autowired
    private CustomRepository repository;

    public void imports(List<SystemItemImport> items, Date date) {
        for (SystemItemImport item : items) {
            SystemItemDB model = SystemItemImport.ToSystemItemDB(item, date);
            Optional<SystemItemDB> data = repository.findById(model.getId());
            if (data.isEmpty()) {
                repository.save(model);
            } else {
                SystemItemDB exist = data.get();
                if (exist.getType() == model.getType()) {
                    exist.setDate(model.getDate());
                    repository.save(exist);
                }
            }
        }
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

    private void getChildren(String id, ArrayList<SystemItemDB> items){
        ArrayList<SystemItemDB> itemsDB = repository.getAllByParentId(id);
        if (itemsDB != null)
            items.addAll(itemsDB);

        for (SystemItemDB item : itemsDB) {
            getChildren(item.getId(), items);
        }
    }

    public ArrayList<SystemItemDB> nodes(String id){
        SystemItemDB item = repository.findById(id).get();
        ArrayList<SystemItemDB> items = new ArrayList<>();
        items.add(item);
        getChildren(item.getId(), items);

        return items;
    }
}
