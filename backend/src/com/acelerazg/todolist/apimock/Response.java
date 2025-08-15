package com.acelerazg.todolist.apimock;

public class Response<T> {
    private int statusCode;
    private String message;
    private T data;

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
        return "{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
