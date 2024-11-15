package org.example.worldcupboard.api.model;

public record Score(GameId gameId,
                    Team home, int homeTeamScore,
                    Team away, int awayTeamScore) {
}
