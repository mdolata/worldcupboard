package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.Team;

import java.time.Instant;
import java.util.List;

public record Event(EventType eventTyp, List<Team> teamsInvolved, Instant time) {
}
