package org.example.worldcupboard.internal;

import org.example.worldcupboard.api.Operations;
import org.example.worldcupboard.api.model.GameId;
import org.example.worldcupboard.api.model.Team;
import org.example.worldcupboard.api.model.results.CreateResult;
import org.example.worldcupboard.api.model.results.FinishResult;
import org.example.worldcupboard.api.model.results.Summary;
import org.example.worldcupboard.api.model.results.UpdateResult;
import org.example.worldcupboard.internal.commands.CommandService;

public class OperationsImpl implements Operations {

    private final CommandService commandService;

    public OperationsImpl(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public CreateResult createGame(Team home, Team away) {
        return commandService.add(home, away);
    }

    @Override
    public FinishResult finishGame(GameId gameId) {
        return null;
    }

    @Override
    public UpdateResult updateGame(GameId gameId, Team team) {
        return null;
    }

    @Override
    public Summary getSummary() {
        return null;
    }
}
