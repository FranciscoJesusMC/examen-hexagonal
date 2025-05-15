package com.spring.examenhexagonal.domain.aggregates.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class ResponseBase <T>{
    private String message;
    private int code;
    private Optional<T> data;
}
