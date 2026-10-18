package com.bnpp.katas.bowling.dto;

import jakarta.validation.constraints.NotBlank;

public record ScoreRequest(@NotBlank String rolls) {
}
