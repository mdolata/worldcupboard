package org.example.worldcupboard.internal.queries;

import org.example.worldcupboard.api.model.Score;
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
                .sorted(new Comparator<>() {
                    @Override
                    public int compare(Score score1, Score score2) {
                        int byTotalScore = compareByTotalScore(score1, score2);
                        if (byTotalScore != 0) return byTotalScore;
                        else return compareByStartTime(score1, score2);
                    }

                    private int compareByStartTime(Score score1, Score score2) {
                        if (score1.startTime().isBefore(score2.startTime())) return 1;
                        else return -1;
                    }

                    private int compareByTotalScore(Score score1, Score score2) {
                        return Integer.compare(totalScore(score2), totalScore(score1));
                    }

                    private int totalScore(Score score) {
                        return score.homeTeamScore() + score.awayTeamScore();
                    }
                })
                .toList());
    }
}
