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
        return null;
    }
}
