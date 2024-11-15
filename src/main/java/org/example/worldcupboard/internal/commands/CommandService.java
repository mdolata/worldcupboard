package org.example.worldcupboard.internal.commands;

import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;
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

}
