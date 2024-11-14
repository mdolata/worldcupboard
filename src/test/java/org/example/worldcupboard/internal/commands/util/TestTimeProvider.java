package org.example.worldcupboard.internal.commands.util;

import org.example.worldcupboard.internal.commands.TimeProvider;

import java.time.Instant;

public class TestTimeProvider implements TimeProvider {
    public static final Instant instant = Instant.parse("2024-01-01T14:00:00Z");

    @Override
    public Instant now() {
        return instant;
    }
}
