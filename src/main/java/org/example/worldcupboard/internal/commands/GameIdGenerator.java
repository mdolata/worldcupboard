package org.example.worldcupboard.internal.commands;

import org.example.worldcupboard.api.model.GameId;

import java.util.UUID;

public class GameIdGenerator {
    public GameId generate() {
        return new GameId(UUID.randomUUID());
    }
}
