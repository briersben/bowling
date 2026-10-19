package com.bnpp.katas.bowling.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The total score for the bowling game.")
public record ScoreResponse(int score) {
}
