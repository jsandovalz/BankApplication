package com.devsu.hackerearth.backend.client.exceptions;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
   
}
