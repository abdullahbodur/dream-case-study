package com.dreamgames.backendengineeringcasestudy.exceptions;

import java.util.List;

public record RestMessage(String message,
                          List<String> error) {

}