package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.UpdateContext;
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
    public boolean isApplicable(UpdateContext update) {
        return Optional.of(update)
                .map(UpdateContext::getUpdate)
                .map(Update::getMessage)
                .map(Message::getText)
                .filter(text -> text.startsWith("/help"))
                .isPresent();
    }

    @Override
    public void handle(UpdateContext update) {
        var reply = SendMessage.builder()
                .chatId(update.getUpdate().getMessage().getChatId().toString())
                .text(help)
                .build();
        update.getBot().execute(reply);
    }
}
