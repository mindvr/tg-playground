package dev.mindvr.tgplayground.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;


/**
 * Limited facade to TelegramLongPollingBot.
 * <p>
 * Can be a basis for context in the future.
 */
@RequiredArgsConstructor
public class TgBot {
    private final TelegramLongPollingBot bot;

    @SneakyThrows
    public <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method) {
        bot.execute(method);
    }
}
