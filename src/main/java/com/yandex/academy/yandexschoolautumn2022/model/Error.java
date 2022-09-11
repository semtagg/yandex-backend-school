package com.yandex.academy.yandexschoolautumn2022.model;

public class Error {
    private Integer code;

    private String message;

    public Error() {
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
