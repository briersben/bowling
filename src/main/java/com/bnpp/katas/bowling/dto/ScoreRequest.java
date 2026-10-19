package com.bnpp.katas.bowling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Bowling roll notation for one complete game.")
public record ScoreRequest(
		@Schema(
				description = "Rolls use X or x for a strike, 1-9 for pins, - for a miss, and / for a spare. Whitespace is ignored.",
				example = "X X X X X X X X X X X X")
		@NotBlank String rolls) {
}
