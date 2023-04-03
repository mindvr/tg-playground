package dev.mindvr.tgplayground.command;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    boolean isApplicable(Update update);

    void handle(Update update, TelegramLongPollingBot bot);
}
