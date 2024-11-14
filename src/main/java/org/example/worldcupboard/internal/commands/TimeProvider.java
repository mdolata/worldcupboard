package org.example.worldcupboard.internal.commands;

import java.time.Instant;

public interface TimeProvider {
    Instant now();
}
