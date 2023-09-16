package com.daelim.trinitycommunitybackend.dto;

import lombok.Data;

@Data
public class Response<E> {
    Error error = null;
    E data = null;
}
