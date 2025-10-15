package com.acelerazg.common;

public class Response<T> {
    private final int statusCode;
    private final String message;
    private final T data;

    private Response(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(int statusCode, String message, T data) {
        return new Response<>(statusCode, message, data);
    }

    public static <T> Response<T> error(int statusCode, String message) {
        return new Response<>(statusCode, message, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{" + "statusCode=" + statusCode + ", message='" + message + '\'' + ", data=" + data + '}';
    }
}