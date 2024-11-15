package org.example.worldcupboard.internal.store;

import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.worldcupboard.internal.store.EventType.CREATE;

class StoreTest {

    private final HashMap<GameId, List<Event>> db = new HashMap<>();
    private final Store store = new Store(db);

    private static final Instant instant = Instant.parse("2024-01-01T14:00:00Z");

    @ParameterizedTest
    @EnumSource(EventType.class)
    void shouldAddEvent(EventType eventType) {
        // given
        var gameId = new GameId(UUID.randomUUID());
        var event = new Event(eventType, List.of(new Team("team")), instant);

        // when
        var result = store.add(gameId, event);

        // then
        assertThat(result).isTrue();
        assertThat(db.get(gameId)).containsExactly(event);
    }

    @Test
    void shouldAddAnotherEventForTheGame() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        var event = new Event(CREATE, List.of(new Team("team")), instant);

        // when
        var result1 = store.add(gameId, event);

        // then
        assertThat(result1).isTrue();
        assertThat(db.get(gameId)).containsExactly(event);

        // and
        var result2 = store.add(gameId, event);

        // then
        assertThat(result2).isTrue();
        assertThat(db.get(gameId)).containsExactlyElementsOf(List.of(event, event));
    }

    @Test
    void shouldVerifyGameExistsBasedOnTeamNames() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        Team home = new Team("home");
        Team away = new Team("away");

        var event1 = new Event(CREATE, List.of(home, away), instant);
        db.put(gameId, List.of(event1));

        // when
        var result = store.verifyGameExists(home, away);

        // then
        assertThat(result).isEqualTo(gameId);
    }

    @Test
    void shouldVerifyGameDoesNotExistWhenBothTeamsAreMissing() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        Team home = new Team("home");
        Team away = new Team("away");

        var event1 = new Event(CREATE, List.of(home, away), instant);
        db.put(gameId, List.of(event1));

        // when
        var result = store.verifyGameExists(new Team("anotherHome"), new Team("anotherAway"));

        // then
        assertThat(result).isEqualTo(null);
    }

    @Test
    void shouldVerifyGameDoesNotExistWhenHomeTeamIsMissing() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        Team home = new Team("home");
        Team away = new Team("away");

        var event1 = new Event(CREATE, List.of(home, away), instant);
        db.put(gameId, List.of(event1));

        // when
        var result = store.verifyGameExists(new Team("anotherHome"), away);

        // then
        assertThat(result).isEqualTo(null);
    }

    @Test
    void shouldVerifyGameDoesNotExistWhenAwayTeamIsMissing() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        Team home = new Team("home");
        Team away = new Team("away");

        var event1 = new Event(CREATE, List.of(home, away), instant);
        db.put(gameId, List.of(event1));

        // when
        var result = store.verifyGameExists(home, new Team("anotherAway"));

        // then
        assertThat(result).isEqualTo(null);
    }

    @Test
    void shouldVerifyGameDoesNotExistWhenTeamsAreSwitched() {
        // given
        var gameId = new GameId(UUID.randomUUID());
        Team home = new Team("home");
        Team away = new Team("away");

        var event1 = new Event(CREATE, List.of(home, away), instant);
        db.put(gameId, List.of(event1));

        // when
        var result = store.verifyGameExists(away, home);

        // then
        assertThat(result).isEqualTo(null);
    }

    @Test
    void shouldVerifyGameExistsBasedOnGameId() {
        // given
        var gameId = new GameId(UUID.randomUUID());

        var event1 = new Event(CREATE, List.of(new Team("home"), new Team("away")), instant);
        db.put(gameId, List.of(event1));

        // when
        var result = store.verifyGameExists(gameId);

        // then
        assertThat(result).isEqualTo(gameId);
    }

    @Test
    void shouldVerifyGameDoesNotExistsBasedOnGameId() {
        // given
        var event1 = new Event(CREATE, List.of(new Team("home"), new Team("away")), instant);
        db.put(new GameId(UUID.randomUUID()), List.of(event1));

        // when
        var result = store.verifyGameExists(new GameId(UUID.randomUUID()));

        // then
        assertThat(result).isNull();
    }

}