package org.example.worldcupboard.internal.commands;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;
import org.example.worldcupboard.internal.store.Event;
import org.example.worldcupboard.internal.store.Store;
import org.example.worldcupboard.internal.commands.util.TestTimeProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CreateTest {

    private final Store store = mock(Store.class);
    private final GameIdGenerator gameIdGenerator = mock(GameIdGenerator.class);
    private final TimeProvider timeProvider = new TestTimeProvider();

    private final CommandService commandService = new CommandService(store, gameIdGenerator, timeProvider);

    @Test
    void shouldAddNewGame() {
        // given
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        GameId gameId = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId);

        // when
        var result = commandService.add(teamHome, teamAway);

        // then
        verify(store).add(gameId, Event.createEvent(List.of(teamHome, teamAway), timeProvider.now()));

        CreateResult expected = new CreateResult(true, gameId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotAddNewGameWhenAlreadyExistsAndReturnsTheExistingGameId() {
        // given
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        GameId gameId = new GameId(UUID.randomUUID());
        GameId unusedGameId = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId)
                .thenReturn(unusedGameId);

        // when
        commandService.add(teamHome, teamAway);

        // and
        when(store.verifyGameExists(teamHome, teamAway)).thenReturn(gameId);

        // when
        var result = commandService.add(teamHome, teamAway);

        // then
        verify(store).add(gameId, Event.createEvent(List.of(teamHome, teamAway), timeProvider.now()));
        verify(store, times(2)).verifyGameExists(teamHome, teamAway);
        verifyNoMoreInteractions(store);

        CreateResult expected = new CreateResult(false, gameId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddNewGameWhenAnotherExists() {
        // given
        var teamHome1 = new Team("home1");
        var teamAway1 = new Team("away1");

        GameId gameId1 = new GameId(UUID.randomUUID());
        GameId gameId2 = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId1)
                .thenReturn(gameId2);

        // when
        commandService.add(teamHome1, teamAway1);

        // and
        var teamHome2 = new Team("home2");
        var teamAway2 = new Team("away2");

        // when
        var result = commandService.add(teamHome2, teamAway2);

        // then
        verify(store).add(gameId1, Event.createEvent(List.of(teamHome1, teamAway1), timeProvider.now()));
        verify(store).add(gameId2, Event.createEvent(List.of(teamHome2, teamAway2), timeProvider.now()));

        CreateResult expected = new CreateResult(true, gameId2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddNewGameWhenTheTeamsAreTheSameButInAnotherOrder() {
        // given
        var teamHome1 = new Team("home");
        var teamAway1 = new Team("away");

        GameId gameId1 = new GameId(UUID.randomUUID());
        GameId gameId2 = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId1)
                .thenReturn(gameId2);

        // when
        commandService.add(teamHome1, teamAway1);

        // and
        var teamHome2 = new Team("away");
        var teamAway2 = new Team("home");

        // when
        var result = commandService.add(teamHome2, teamAway2);

        // then
        verify(store).add(gameId1, Event.createEvent(List.of(teamHome1, teamAway1), timeProvider.now()));
        verify(store).add(gameId2, Event.createEvent(List.of(teamHome2, teamAway2), timeProvider.now()));

        CreateResult expected = new CreateResult(true, gameId2);
        assertThat(result).isEqualTo(expected);
    }
}
