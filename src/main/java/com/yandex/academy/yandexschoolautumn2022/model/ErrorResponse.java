package com.yandex.academy.yandexschoolautumn2022.model;

public class ErrorResponse extends Error {

    private SystemNodesResponse response;

    public ErrorResponse() {
    }

    public SystemNodesResponse getResponse() {
        return response;
    }

    public void setResponse(SystemNodesResponse response) {
        this.response = response;
    }
}
