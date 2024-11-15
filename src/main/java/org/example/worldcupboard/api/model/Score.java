package org.example.worldcupboard.api.model;

import java.time.Instant;

public record Score(GameId gameId, Instant startTime,
                    Team home, int homeTeamScore,
                    Team away, int awayTeamScore) {
}
