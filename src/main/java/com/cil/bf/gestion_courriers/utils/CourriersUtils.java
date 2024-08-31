package com.cil.bf.gestion_courriers.utils;

import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;

public class CourriersUtils {
    private CourriersUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(String responseMassage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMassage + "\"}", httpStatus);
    }

}
