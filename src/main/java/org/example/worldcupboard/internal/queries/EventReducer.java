package org.example.worldcupboard.internal.queries;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Score;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.internal.store.Event;

import java.util.List;

public class EventReducer {

    public static Score calculateScore(GameId gameId, List<Event> events){
        var homeCounter = 0;
        var awayCounter = 0;
        Team home = null;
        Team away = null;
        for (Event e : events) {
            switch (e.eventTyp()) {
                case CREATE -> {
                    home = e.teamsInvolved().getFirst();
                    away = e.teamsInvolved().getLast();
                }
                case UPDATE -> {
                    if (e.teamsInvolved().getFirst().equals(home)) {
                        homeCounter++;
                    } else {
                        awayCounter++;
                    }
                }
            }
        }
        return new Score(gameId, home, homeCounter, away, awayCounter);
    }
}
