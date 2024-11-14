package org.example.worldcupboard.internal;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;

import java.util.HashMap;
import java.util.List;

public class Store {
    private final HashMap<GameId, List<Event>> store;

    public Store(HashMap<GameId, List<Event>> store) {
        this.store = store;
    }

    public void add(GameId gameId, Event event) {

    }


    public GameId verifyGameExists(Team home, Team away) {
        return null;
    }
}
