package org.example.worldcupboard.internal.queries;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Score;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.Summary;
import org.example.worldcupboard.internal.store.Event;
import org.example.worldcupboard.internal.store.Store;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class QueryServiceTest {

    Store store = mock(Store.class);
    QueryService queryService = new QueryService(store);

    private static final Instant instant = Instant.parse("2024-01-01T14:00:00Z");

    @Test
    public void shouldReturnEmptySummaryWhenZeroMatchesStared() {
        // given
        when(store.getAll()).thenReturn(Collections.emptyMap());

        // when
        Summary summary = queryService.getSummary();

        // then
        assertThat(summary.summaryList()).isEmpty();
    }

    @Test
    public void shouldReturnSummaryForStartedMatch() {
        // given
        GameId gameId = new GameId(UUID.randomUUID());
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        when(store.getAll()).thenReturn(Map.of(
                        gameId,
                        List.of(Event.createEvent(List.of(teamA, teamB), instant))
                )
        );

        // when
        Summary summary = queryService.getSummary();

        // then
        assertThat(summary.summaryList()).containsExactly(new Score(gameId, instant, teamA, 0, teamB, 0));
    }

    @Test
    public void shouldReturnSummaryForMatchWithMultipleUpdates() {
        // given
        GameId gameId = new GameId(UUID.randomUUID());
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        when(store.getAll()).thenReturn(Map.of(
                        gameId,
                        List.of(
                                Event.createEvent(List.of(teamA, teamB), instant),
                                Event.updateEvent(teamA, instant),
                                Event.updateEvent(teamB, instant),
                                Event.updateEvent(teamA, instant)
                        )
                )
        );

        // when
        Summary summary = queryService.getSummary();

        // then
        assertThat(summary.summaryList()).containsExactly(new Score(gameId, instant, teamA, 2, teamB, 1));
    }

    @Test
    public void shouldReturnSummaryInOrderByTotalScore() {
        // given
        GameId gameId1 = new GameId(UUID.randomUUID());
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        GameId gameId2 = new GameId(UUID.randomUUID());
        Team teamC = new Team("teamC");
        Team teamD = new Team("teamD");
        GameId gameId3 = new GameId(UUID.randomUUID());
        Team teamX = new Team("teamX");
        Team teamZ = new Team("teamZ");
        Instant game2StartTime = instant.plus(1L, ChronoUnit.MINUTES);
        when(store.getAll()).thenReturn(Map.of(
                        gameId1,
                        List.of(Event.createEvent(List.of(teamA, teamB), instant)),
                        gameId2,
                        List.of(Event.createEvent(List.of(teamC, teamD), game2StartTime),
                                Event.updateEvent(teamC, instant),
                                Event.updateEvent(teamC, instant)),
                        gameId3,
                        List.of(Event.createEvent(List.of(teamX, teamZ), instant),
                                Event.updateEvent(teamZ, instant))
                )
        );

        // when
        Summary summary = queryService.getSummary();

        // then
        assertThat(summary.summaryList()).containsExactly(
                new Score(gameId2, game2StartTime, teamC, 2, teamD, 0),
                new Score(gameId3, instant, teamX, 0, teamZ, 1),
                new Score(gameId1, instant, teamA, 0, teamB, 0)
        );
    }


    @Test
    public void shouldReturnSummaryInOrderByTotalScoreAndByTime() {
        // given
        GameId gameId1 = new GameId(UUID.randomUUID());
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        GameId gameId2 = new GameId(UUID.randomUUID());
        Team teamC = new Team("teamC");
        Team teamD = new Team("teamD");
        GameId gameId3 = new GameId(UUID.randomUUID());
        Team teamX = new Team("teamX");
        Team teamZ = new Team("teamZ");
        Instant game2StartTime = instant.plus(5L, ChronoUnit.MINUTES);
        Instant game3StartTime = instant.plus(1L, ChronoUnit.MINUTES);
        when(store.getAll()).thenReturn(Map.of(
                        gameId1,
                        List.of(Event.createEvent(List.of(teamA, teamB), instant),
                                Event.updateEvent(teamA, instant)),
                        gameId2,
                        List.of(Event.createEvent(List.of(teamC, teamD), game2StartTime),
                                Event.updateEvent(teamC, instant)),
                        gameId3,
                        List.of(Event.createEvent(List.of(teamX, teamZ), game3StartTime),
                                Event.updateEvent(teamX, instant))
                )
        );

        // when
        Summary summary = queryService.getSummary();

        // then
        assertThat(summary.summaryList()).containsExactly(
                new Score(gameId2, game2StartTime, teamC, 1, teamD, 0),
                new Score(gameId3, game3StartTime, teamX, 1, teamZ, 0),
                new Score(gameId1, instant, teamA, 1, teamB, 0)
        );
    }
}