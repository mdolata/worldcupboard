package org.example.worldcupboard.internal;

import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;

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

        var eventForHomeTeam = new Event(EventType.CREATE, home, time);
        var eventForAwayTeam = new Event(EventType.CREATE, away, time);
        store.add(gameId, eventForHomeTeam);
        store.add(gameId, eventForAwayTeam);

        return new CreateResult(true, gameId);
    }

}
