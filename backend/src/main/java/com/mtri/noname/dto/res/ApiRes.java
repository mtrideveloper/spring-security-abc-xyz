package com.mtri.noname.dto.res;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiRes<T> {
    private int code;
    private String message;
    private T result;
}
