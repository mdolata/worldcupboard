package org.example.worldcupboard.api;

import org.example.worldcupboard.internal.OperationsImpl;
import org.example.worldcupboard.internal.commands.CommandService;
import org.example.worldcupboard.internal.commands.GameIdGenerator;
import org.example.worldcupboard.internal.store.Store;
import org.example.worldcupboard.internal.commands.TimeProvider;

import java.time.Clock;
import java.util.HashMap;

public abstract class OperationsFactory {

    public static Operations operationsInMemory() {
        var store = storeBasedOnHashMap();
        var commandService =  commandService(store);

        return new OperationsImpl(commandService);
    }

    private static Store storeBasedOnHashMap() {
        return new Store(new HashMap<>());
    }

    private static CommandService commandService(Store store) {
        var gameIdGenerator = new GameIdGenerator();
        TimeProvider timeProvider = () -> Clock.systemDefaultZone().instant();

        return new CommandService(store, gameIdGenerator, timeProvider);
    }
}