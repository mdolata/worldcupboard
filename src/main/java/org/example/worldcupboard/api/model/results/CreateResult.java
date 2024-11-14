package org.example.worldcupboard.api.model.results;

import org.example.worldcupboard.api.model.GameId;

public record CreateResult(boolean result, GameId gameId) {
}
