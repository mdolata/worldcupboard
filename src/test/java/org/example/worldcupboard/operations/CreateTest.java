package org.example.worldcupboard.operations;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;
import org.example.worldcupboard.internal.CommandService;
import org.example.worldcupboard.internal.Event;
import org.example.worldcupboard.internal.Store;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateTest {

    private final Store store = mock(Store.class);
    private final CommandService commandService = new CommandService(store);

    @Test
    void shouldAddNewGame(){
        // given
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        // when
        var result = commandService.add(teamHome, teamAway);

        // then
        verify(store).add(new GameId(UUID.randomUUID()), new Event());
        CreateResult expected = new CreateResult(true, new GameId(UUID.randomUUID()));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotAddNewGameWhenAlreadyExists() {
        // given
        var teamHome = new Team("home");
        var teamAway = new Team("away");

        // when
        commandService.add(teamHome, teamAway);
        var result = commandService.add(teamHome, teamAway);

        // then
        verify(store).add(new GameId(UUID.randomUUID()), new Event());
        CreateResult expected = new CreateResult(false, GameId.NONE);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddNewGameWhenAnotherExists() {
        // given
        var teamHome1 = new Team("home1");
        var teamAway1 = new Team("away1");

        var teamHome2 = new Team("home2");
        var teamAway2 = new Team("away2");

        // when
        commandService.add(teamHome1, teamAway1);
        var result = commandService.add(teamHome2, teamAway2);

        // then
        verify(store).add(new GameId(UUID.randomUUID()), new Event());
        CreateResult expected = new CreateResult(true, new GameId(UUID.randomUUID()));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddNewGameWhenTheTeamsAreTheSameButInAnotherOrder() {
        // given
        var teamHome1 = new Team("home");
        var teamAway1 = new Team("away");

        var teamHome2 = new Team("away");
        var teamAway2 = new Team("home");

        // when
        commandService.add(teamHome1, teamAway1);
        var result = commandService.add(teamHome2, teamAway2);

        // then
        verify(store).add(new GameId(UUID.randomUUID()), new Event());
        CreateResult expected = new CreateResult(true, new GameId(UUID.randomUUID()));
        assertThat(result).isEqualTo(expected);
    }
}
