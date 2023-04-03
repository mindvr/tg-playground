package dev.mindvr.tgplayground.command;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class HelpCommand implements Command {
    @Setter
    private String help = """
            /help - send available command list
            """;

    @Override
    public boolean isApplicable(Update update) {
        return update.getMessage().getText().startsWith("/help");
    }

    @SneakyThrows
    @Override
    public void handle(Update update, TelegramLongPollingBot bot) {
        var reply = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(help)
                .build();
        bot.execute(reply);
    }
}
