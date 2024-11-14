package org.example.worldcupboard.api.model;

public record Score(Team home, int homeTeamScore,
                    Team away, int awayTeamScore) {
}
