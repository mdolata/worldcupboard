package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    private final HashMap<GameId, List<Event>> store;

    public Store(HashMap<GameId, List<Event>> store) {
        this.store = store;
    }

    public boolean add(GameId gameId, Event event) {
        var events = store.getOrDefault(gameId, new ArrayList<>());
        events.add(event);
        store.put(gameId, events);
        return true;
    }

    public GameId verifyGameExists(Team home, Team away) {
        for (Map.Entry<GameId, List<Event>> entry : store.entrySet()) {
            GameId gameId = entry.getKey();
            List<Event> events = entry.getValue();
            long count = events.stream()
                    .filter(e -> e.eventTyp().equals(EventType.CREATE))
                    .filter(e -> e.teamsInvolved().getFirst() == home && e.teamsInvolved().getLast() == away)
                    .count();

            if (count != 0) {
                return gameId;
            }
        }

        return null;
    }

    public Map<GameId, List<Event>> getAll() {
        return store; // it's not safe
    }

}
