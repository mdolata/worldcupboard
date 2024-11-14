package org.example.worldcupboard.operations;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;
import org.example.worldcupboard.internal.*;
import org.example.worldcupboard.operations.util.TestTimeProvider;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.example.worldcupboard.internal.EventType.CREATE;
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
        verify(store).add(gameId, new Event(CREATE, teamHome, timeProvider.now()));
        verify(store).add(gameId, new Event(CREATE, teamAway, timeProvider.now()));

        CreateResult expected = new CreateResult(true, gameId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotAddNewGameWhenAlreadyExists() {
        // given
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        GameId gameId = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId);

        // when
        commandService.add(teamHome, teamAway);
        var result = commandService.add(teamHome, teamAway);

        // then
        verify(store).add(gameId, new Event(CREATE, teamHome, timeProvider.now()));
        verify(store).add(gameId, new Event(CREATE, teamAway, timeProvider.now()));
        verifyNoMoreInteractions(store);

        CreateResult expected = new CreateResult(false, gameId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddNewGameWhenAnotherExists() {
        // given
        var teamHome1 = new Team("home1");
        var teamAway1 = new Team("away1");

        var teamHome2 = new Team("home2");
        var teamAway2 = new Team("away2");

        GameId gameId1 = new GameId(UUID.randomUUID());
        GameId gameId2 = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId1);
        when(gameIdGenerator.generate()).thenReturn(gameId2);

        // when
        commandService.add(teamHome1, teamAway1);
        var result = commandService.add(teamHome2, teamAway2);

        // then
        verify(store).add(gameId1, new Event(CREATE, teamHome1, timeProvider.now()));
        verify(store).add(gameId1, new Event(CREATE, teamAway1, timeProvider.now()));
        verify(store).add(gameId2, new Event(CREATE, teamHome2, timeProvider.now()));
        verify(store).add(gameId2, new Event(CREATE, teamAway2, timeProvider.now()));

        CreateResult expected = new CreateResult(true, gameId2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddNewGameWhenTheTeamsAreTheSameButInAnotherOrder() {
        // given
        var teamHome1 = new Team("home");
        var teamAway1 = new Team("away");

        var teamHome2 = new Team("away");
        var teamAway2 = new Team("home");

        GameId gameId1 = new GameId(UUID.randomUUID());
        GameId gameId2 = new GameId(UUID.randomUUID());
        when(gameIdGenerator.generate()).thenReturn(gameId1);
        when(gameIdGenerator.generate()).thenReturn(gameId2);

        // when
        commandService.add(teamHome1, teamAway1);
        var result = commandService.add(teamHome2, teamAway2);

        // then
        verify(store).add(gameId1, new Event(CREATE, teamHome1, timeProvider.now()));
        verify(store).add(gameId1, new Event(CREATE, teamAway1, timeProvider.now()));
        verify(store).add(gameId2, new Event(CREATE, teamHome2, timeProvider.now()));
        verify(store).add(gameId2, new Event(CREATE, teamAway2, timeProvider.now()));

        CreateResult expected = new CreateResult(true, new GameId(UUID.randomUUID()));
        assertThat(result).isEqualTo(expected);
    }
}
