package ru.kenyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebTokenDTO(@JsonProperty("auth-token") String token) {
}
