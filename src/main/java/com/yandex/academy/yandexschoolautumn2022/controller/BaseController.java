package com.yandex.academy.yandexschoolautumn2022.controller;

import com.yandex.academy.yandexschoolautumn2022.model.Error;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemImportRequest;
import com.yandex.academy.yandexschoolautumn2022.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/base")
public class BaseController {
    @Autowired
    private BaseService baseService;

    @PostMapping("/imports")
    public ResponseEntity imports(@RequestBody SystemItemImportRequest request) {
        var result = new Error();
        result.setCode(200);
        result.setMessage("Ok");


        baseService.imports(request.getItems(), request.getUpdateDate());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id, @RequestParam String date) {
        Date date1 = Date.from(Instant.parse(date));
        baseService.delete(id, date1);

        var result = new Error();
        result.setCode(200);
        result.setMessage("Ok");
        return ResponseEntity.ok(result);
    }


    @GetMapping("/nodes/{id}")
    public ResponseEntity nodes(@PathVariable String id) {
        baseService.nodes(id);

        var result = new Error();
        result.setCode(200);
        result.setMessage("Ok");
        return ResponseEntity.ok(result);
    }
}
