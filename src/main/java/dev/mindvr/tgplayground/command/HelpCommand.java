package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.TgBot;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@Slf4j
public class HelpCommand implements Command {
    @Setter
    private String help = """
            /help - send available command list
            """;

    @Override
    public boolean isApplicable(Update update) {
        return Optional.of(update)
                .map(Update::getMessage)
                .map(Message::getText)
                .filter(text -> text.startsWith("/help"))
                .isPresent();
    }

    @Override
    public void handle(Update update, TgBot bot) {
        var reply = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(help)
                .build();
        bot.execute(reply);
    }
}
