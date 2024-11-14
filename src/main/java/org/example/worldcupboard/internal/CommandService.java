package org.example.worldcupboard.internal;

import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;

public class CommandService {

    private final Store store;

    public CommandService(Store store) {
        this.store = store;
    }

    public CreateResult add(Team home, Team away) {
        return null;
    }
}
