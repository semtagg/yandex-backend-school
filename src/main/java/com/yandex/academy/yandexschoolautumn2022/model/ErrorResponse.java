package com.yandex.academy.yandexschoolautumn2022.model;

public class ErrorResponse<T> extends Error {
    private T response;

    public ErrorResponse() {
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
