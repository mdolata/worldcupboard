package org.example.worldcupboard.internal.commands;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Score;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.FinishResult;
import org.example.worldcupboard.internal.store.Event;
import org.example.worldcupboard.internal.store.Store;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FinishTest {
    private final Store store = mock(Store.class);
    private final GameIdGenerator gameIdGenerator = mock(GameIdGenerator.class);

    private final CommandService commandService = new CommandService(store, gameIdGenerator, null);

    private static final Instant instant = Instant.parse("2024-01-01T14:00:00Z");

    @Test
    void shouldFinishGameWhenExistsInInitialState() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        when(store.verifyGameExists(gameId)).thenReturn(gameId);
        when(store.remove(gameId)).thenReturn(List.of(Event.createEvent(List.of(teamHome, teamAway), instant)));

        // when
        FinishResult result = commandService.finish(gameId);

        // then
        verify(store).verifyGameExists(gameId);
        verify(store).remove(gameId);

        FinishResult expected = new FinishResult(true, gameId, new Score(gameId, instant, teamHome, 0, teamAway, 0));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotFinishGameWhenNotExists() {
        // given
        var gameId = new GameId(UUID.randomUUID());

        when(store.verifyGameExists(gameId)).thenReturn(null);

        // when
        FinishResult result = commandService.finish(gameId);

        // then
        verify(store).verifyGameExists(gameId);
        verifyNoMoreInteractions(store);

        FinishResult expected = new FinishResult(false, gameId, null);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldFinishGameAndReturnFinalScore() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        when(store.verifyGameExists(gameId)).thenReturn(gameId);
        when(store.remove(gameId)).thenReturn(List.of(
                Event.createEvent( List.of(teamHome, teamAway), instant),
                Event.updateEvent(teamHome, instant),
                Event.updateEvent(teamHome, instant)
        ));

        // when
        FinishResult result = commandService.finish(gameId);

        // then
        verify(store).verifyGameExists(gameId);
        verify(store).remove(gameId);

        FinishResult expected = new FinishResult(true, gameId, new Score(gameId, instant, teamHome, 2, teamAway, 0));
        assertThat(result).isEqualTo(expected);
    }

}
