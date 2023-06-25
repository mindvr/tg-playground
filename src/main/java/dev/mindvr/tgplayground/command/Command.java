package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.TgBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    boolean isApplicable(Update update);

    void handle(Update update, TgBot bot);
}
