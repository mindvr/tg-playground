package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.UpdateContext;

public interface Command {
    boolean isApplicable(UpdateContext update);

    void handle(UpdateContext update);
}
