package org.example.worldcupboard.api.model;

import java.util.UUID;

public record GameId(UUID value) {
    public static final GameId NONE = new GameId(null);
}
