package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;

import java.util.HashMap;
import java.util.List;

public class Store {
    private final HashMap<GameId, List<Event>> store;

    public Store(HashMap<GameId, List<Event>> store) {
        this.store = store;
    }

    public boolean add(GameId gameId, Event event) {
        return false;
    }


    public GameId verifyGameExists(Team home, Team away) {
        return null;
    }
}
