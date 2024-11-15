package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.Team;

import java.time.Instant;
import java.util.List;

// todo make default constructor private and use static methods for creation
public record Event(EventType eventTyp, List<Team> teamsInvolved, Instant time) {

    public static Event createEvent(List<Team> teamsInvolved, Instant time) {
        assert teamsInvolved.size() == 2;
        return new Event(EventType.CREATE, teamsInvolved, time);
    }

    public static Event updateEvent(Team teamInvolved, Instant time) {
        return new Event(EventType.UPDATE, List.of(teamInvolved), time);
    }
}
