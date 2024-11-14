package org.example.worldcupboard.internal;

import java.time.Instant;

public interface TimeProvider {
    Instant now();
}
