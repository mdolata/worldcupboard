package org.example.worldcupboard.internal;

import org.example.worldcupboard.api.model.Team;

import java.time.Instant;

public record Event(EventType eventTyp, Team teamInvolved, Instant time) {
}
