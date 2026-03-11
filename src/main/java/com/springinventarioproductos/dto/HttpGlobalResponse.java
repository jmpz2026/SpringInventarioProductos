package com.springinventarioproductos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpGlobalResponse<T> {
    private T data;

    private String message;
}
