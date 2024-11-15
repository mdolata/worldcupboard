package org.example.worldcupboard.internal.queries;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Score;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.Summary;
import org.example.worldcupboard.internal.store.Event;
import org.example.worldcupboard.internal.store.Store;

import java.util.List;
import java.util.Map;

public class QueryService {
    private final Store store;

    public QueryService(Store store) {
        this.store = store;
    }

    public Summary getSummary() {
        return new Summary(store.getAll()
                .entrySet()
                .stream()
                .map(this::calculateScore)
                .toList());
    }

    private Score calculateScore(Map.Entry<GameId, List<Event>> value) {
        var gameId = value.getKey();
        var homeCounter = 0;
        var awayCounter = 0;
        Team home = null;
        Team away = null;
        for(Event e : value.getValue()) {
            switch (e.eventTyp()) {
                case CREATE -> {
                    home = e.teamsInvolved().getFirst();
                    away = e.teamsInvolved().getLast();
                }
                case UPDATE -> throw new UnsupportedOperationException();
            }
        }
        return new Score(gameId, home, homeCounter, away, awayCounter);
    }
}
