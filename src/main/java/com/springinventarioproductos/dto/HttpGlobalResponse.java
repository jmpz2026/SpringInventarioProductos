package com.springinventarioproductos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpGlobalResponse<T> {
    private T data;

    private String message;
}
