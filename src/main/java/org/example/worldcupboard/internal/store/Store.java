package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.worldcupboard.internal.store.EventType.CREATE;

public class Store {
    private final HashMap<GameId, List<Event>> store;
    private final HashMap<String, GameId> mapping;

    public Store(HashMap<GameId, List<Event>> store) {
        this.store = store;
        mapping = new HashMap<>();
    }

    // hack for calculation of the existing pairs -> should be refactored
    public boolean add(GameId gameId, Event event1, Event event2) {
        mapping.put(event1.teamInvolved() + "" + event2.teamInvolved(), gameId);
        add(gameId, event1);
        add(gameId, event2);
        return true;
    }

    public boolean add(GameId gameId, Event event) {
        var events = store.getOrDefault(gameId, new ArrayList<>());
        events.add(event);
        store.put(gameId, events);
        return true;
    }


    public GameId verifyGameExists(Team home, Team away) {
        return mapping.get(home + "" + away);
    }
}
