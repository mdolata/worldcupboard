package org.example.worldcupboard.internal.commands;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;
import org.example.worldcupboard.api.model.results.FinishResult;
import org.example.worldcupboard.api.model.results.UpdateResult;
import org.example.worldcupboard.internal.queries.EventReducer;
import org.example.worldcupboard.internal.queries.QueryService;
import org.example.worldcupboard.internal.store.Event;
import org.example.worldcupboard.internal.store.EventType;
import org.example.worldcupboard.internal.store.Store;

import java.util.List;

public class CommandService {

    private final Store store;
    private final GameIdGenerator gameIdGenerator;
    private final TimeProvider timeProvider;

    public CommandService(Store store, GameIdGenerator gameIdGenerator, TimeProvider timeProvider) {
        this.store = store;
        this.gameIdGenerator = gameIdGenerator;
        this.timeProvider = timeProvider;
    }

    public CreateResult add(Team home, Team away) {
        var existingGameId = store.verifyGameExists(home, away);
        if (existingGameId != null) {
            return new CreateResult(false, existingGameId);
        }

        var gameId = gameIdGenerator.generate();
        var time = timeProvider.now();

        var creationEvent = new Event(EventType.CREATE, List.of(home, away), time);
        store.add(gameId, creationEvent);

        return new CreateResult(true, gameId);
    }

    public UpdateResult update(GameId gameId, Team team) {
        var existingGameId = store.verifyGameExists(gameId);
        if (existingGameId == null) {
            return new UpdateResult(false);
        }

        var event = Event.updateEvent(team, timeProvider.now());
        store.add(gameId, event);

        return new UpdateResult(true);
    }

    public FinishResult finish(GameId gameId) {
        var existingGameId = store.verifyGameExists(gameId);
        if (existingGameId == null) {
            return new FinishResult(false, gameId, null);
        }

        List<Event> events = store.remove(gameId);
        var finalScore = EventReducer.calculateScore(gameId, events);

        return new FinishResult(true, gameId, finalScore);
    }
}
