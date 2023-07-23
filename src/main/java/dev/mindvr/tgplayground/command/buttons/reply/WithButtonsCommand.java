package dev.mindvr.tgplayground.command.buttons.reply;

import dev.mindvr.tgplayground.bot.UpdateContext;
import dev.mindvr.tgplayground.command.Command;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@Slf4j
public class WithButtonsCommand implements Command {

    @Override
    public boolean isApplicable(UpdateContext update) {
        return Optional.of(update)
                .map(UpdateContext::getUpdate)
                .map(Update::getMessage)
                .map(Message::getText)
                .map(String::trim)
                .filter("/with_buttons"::equals)
                .isPresent();
    }

    @SneakyThrows
    @Override
    public void handle(UpdateContext update) {
        long chatId = update.getUpdate().getMessage().getChatId();

        SendMessage message = new WithButtonsMessage().asSendMessage();
        message.setChatId(chatId);

        update.getBot().execute(message);// Sending our message object to user
    }


}
