package org.example.worldcupboard.internal.queries;

import org.example.worldcupboard.api.model.results.Summary;
import org.example.worldcupboard.internal.store.Store;

import java.util.Comparator;

public class QueryService {
    private final Store store;

    public QueryService(Store store) {
        this.store = store;
    }

    public Summary getSummary() {
        return new Summary(store.getAll()
                .entrySet()
                .stream()
                .map(value -> EventReducer.calculateScore(value.getKey(), value.getValue()))
                .sorted(Comparator.comparing(score -> score.homeTeamScore() + score.awayTeamScore()))
                .toList());
    }
}
