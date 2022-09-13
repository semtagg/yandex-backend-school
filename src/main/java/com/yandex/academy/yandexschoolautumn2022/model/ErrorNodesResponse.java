package com.yandex.academy.yandexschoolautumn2022.model;

public class ErrorNodesResponse extends Error {

    private SystemNodesResponse response;

    public ErrorNodesResponse() {
    }

    public SystemNodesResponse getResponse() {
        return response;
    }

    public void setResponse(SystemNodesResponse response) {
        this.response = response;
    }
}
