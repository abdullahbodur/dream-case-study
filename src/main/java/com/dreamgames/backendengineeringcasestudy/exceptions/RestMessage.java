package com.dreamgames.backendengineeringcasestudy.exceptions;

public record RestMessage(
    int status,
    String message,
    String path,
    String error) {

}