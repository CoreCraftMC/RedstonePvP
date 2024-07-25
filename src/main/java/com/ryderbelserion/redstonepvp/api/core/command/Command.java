package com.ryderbelserion.redstonepvp.api.core.command;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class Command {

    public abstract void execute(CommandData data);

    public abstract String getPermission();

    public abstract LiteralCommandNode<CommandSourceStack> literal();

    public abstract Command registerPermission();

    public CompletableFuture<Suggestions> suggestNames(final SuggestionsBuilder builder) {
        for (int count = 1; count <= 7; count++) builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8));

        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> suggestIntegers(final SuggestionsBuilder builder) {
        for (int count = 1; count <= 100; count++) builder.suggest(count);

        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> suggestDoubles(final SuggestionsBuilder builder) {
        int count = 0;

        while (count <= 1000) {
            double x = count / 10.0;

            builder.suggest(String.valueOf(x));

            count++;
        }

        return builder.buildFuture();
    }
}