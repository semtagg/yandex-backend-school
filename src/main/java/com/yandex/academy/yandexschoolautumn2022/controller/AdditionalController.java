package com.yandex.academy.yandexschoolautumn2022.controller;

import com.yandex.academy.yandexschoolautumn2022.service.AdditionalService;
import com.yandex.academy.yandexschoolautumn2022.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AdditionalController {
    @Autowired
    private AdditionalService additionalService;

    /*@GetMapping("/updates")
    public ResponseEntity updates(@RequestParam String date) {

    }*/
}
