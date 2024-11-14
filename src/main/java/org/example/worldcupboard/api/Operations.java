package org.example.worldcupboard.api;

import org.example.worldcupboard.api.model.*;
import org.example.worldcupboard.api.model.results.CreateResult;
import org.example.worldcupboard.api.model.results.FinishResult;
import org.example.worldcupboard.api.model.results.Summary;
import org.example.worldcupboard.api.model.results.UpdateResult;

public interface Operations {

    CreateResult createGame(Team home, Team away);

    FinishResult finishGame(GameId gameId);

    UpdateResult updateGame(GameId gameId, Score score);

    Summary getSummary();

}
