package org.example.worldcupboard.api.model.results;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Score;

public record FinishResult(boolean result, GameId gameId, Score finalScore) {
}
