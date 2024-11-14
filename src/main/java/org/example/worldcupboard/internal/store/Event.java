package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.Team;

import java.time.Instant;

public record Event(EventType eventTyp, Team teamInvolved, Instant time) {
}
