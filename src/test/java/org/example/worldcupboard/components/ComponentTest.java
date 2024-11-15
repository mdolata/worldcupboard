package org.example.worldcupboard.components;

import org.example.worldcupboard.api.Operations;
import org.example.worldcupboard.api.OperationsFactory;
import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Score;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.FinishResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentTest {
    private final Operations operations = OperationsFactory.operationsInMemory();

    @Test
    void shouldCreateGameAndShowOnScoreBoard() {
        // given
        var teamA = new Team("teamA");
        var teamB = new Team("teamB");

        // when
        GameId gameId = operations.createGame(teamA, teamB).gameId();

        // then
        assertInitialState(gameId, teamA, teamB);
    }

    @Disabled
    @Test
    void shouldCreateGameAndUpdateScoreAndShowOnScoreBoard() {
        // given
        var teamA = new Team("teamA");
        var teamB = new Team("teamB");

        // when
        GameId gameId = operations.createGame(teamA, teamB).gameId();

        // then
        assertInitialState(gameId, teamA, teamB);

        // and
        operations.updateGame(gameId, teamA);

        // then
        assertGameScore(gameId, teamA, 1, teamB, 0);

        // and
        operations.updateGame(gameId, teamB);

        // then
        assertGameScore(gameId, teamA, 1, teamB, 1);
    }

    @Disabled
    @Test
    void shouldCreateGameAndShowOnScoreBoardAndFinish() {
        // given
        var teamA = new Team("teamA");
        var teamB = new Team("teamB");

        // when
        GameId gameId = operations.createGame(teamA, teamB).gameId();

        // then
        assertInitialState(gameId, teamA, teamB);

        // and
        FinishResult finishResult = operations.finishGame(gameId);

        // then
        assertGameNotExists(gameId);
        assertFinalScore(finishResult.finalScore(), 0, 0);

    }

    @Disabled
    @Test
    void shouldCreateGameAndUpdateScoreAndShowOnScoreBoardAndFinish() {
        // given
        var teamA = new Team("teamA");
        var teamB = new Team("teamB");

        // when
        GameId gameId = operations.createGame(teamA, teamB).gameId();
        operations.updateGame(gameId, teamA);
        operations.updateGame(gameId, teamB);
        operations.updateGame(gameId, teamB);
        FinishResult finishResult = operations.finishGame(gameId);

        // then
        assertGameNotExists(gameId);
        assertFinalScore(finishResult.finalScore(), 1, 2);
    }

    private void assertInitialState(GameId gameId, Team teamA, Team teamB) {
        assertGameScore(gameId, teamA, 0, teamB, 0);
    }

    private void assertGameScore(GameId gameId, Team teamA, int teamAScore, Team teamB, int teamBScore) {
        assertThat(getScore(gameId).get())
                .isEqualTo(
                        new Score(gameId,
                                teamA, teamAScore,
                                teamB, teamBScore)
                );
    }

    private Optional<Score> getScore(GameId gameId) {
        return operations.getSummary().summaryList()
                .stream()
                .filter(score -> score.gameId().equals(gameId))
                .findFirst();
    }

    private void assertFinalScore(Score score, int teamAScore, int teamBScore) {
        assertThat(score.homeTeamScore()).isEqualTo(teamAScore);
        assertThat(score.awayTeamScore()).isEqualTo(teamBScore);
    }

    private void assertGameNotExists(GameId gameId) {
        assertThat(getScore(gameId)).isEmpty();
    }
}
