package org.example.worldcupboard.internal.commands;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.UpdateResult;
import org.example.worldcupboard.internal.commands.util.TestTimeProvider;
import org.example.worldcupboard.internal.store.Event;
import org.example.worldcupboard.internal.store.Store;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UpdateTest {
    private final Store store = mock(Store.class);
    private final GameIdGenerator gameIdGenerator = mock(GameIdGenerator.class);
    private final TimeProvider timeProvider = new TestTimeProvider();

    private final CommandService commandService = new CommandService(store, gameIdGenerator, timeProvider);

    @Test
    void shouldAddAnotherEventAsUpdate() {
        // given
        var team = new Team("home");
        var gameId = new GameId(UUID.randomUUID());
        when(store.verifyGameExists(gameId)).thenReturn(gameId);

        // when
        var result = commandService.update(gameId, team);

        // then
        verify(store).verifyGameExists(gameId);
        verify(store).add(gameId, Event.updateEvent(team, timeProvider.now()));

        UpdateResult expected = new UpdateResult(true);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotAddUpdateForNonExistingMatch() {
        // given
        var team = new Team("home");
        var gameId = new GameId(UUID.randomUUID());

        when(store.verifyGameExists(gameId)).thenReturn(null);

        // when
        var result = commandService.update(gameId, team);

        // then
        verify(store).verifyGameExists(gameId);
        verifyNoMoreInteractions(store);

        UpdateResult expected = new UpdateResult(false);
        assertThat(result).isEqualTo(expected);
    }
}
