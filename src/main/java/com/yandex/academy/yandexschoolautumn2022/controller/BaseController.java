package com.yandex.academy.yandexschoolautumn2022.controller;

import com.yandex.academy.yandexschoolautumn2022.model.Error;
import com.yandex.academy.yandexschoolautumn2022.model.ErrorNodesResponse;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemImportRequest;
import com.yandex.academy.yandexschoolautumn2022.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class BaseController {
    @Autowired
    private BaseService baseService;

    @PostMapping("/imports")
    public ResponseEntity imports(@RequestBody SystemItemImportRequest request) {
        Error result = baseService.imports(request.getItems(), request.getUpdateDate());

        if (result.getCode() == 400) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id, @RequestParam String date) {
        Error result = baseService.delete(id, date);

        if (result.getCode() == 400) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        if (result.getCode() == 404) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/nodes/{id}")
    public ResponseEntity nodes(@PathVariable String id) {
        ErrorNodesResponse result = baseService.nodes(id);

        if (result.getCode() == 404) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result.getResponse(), HttpStatus.OK);
    }
}
