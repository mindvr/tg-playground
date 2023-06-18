package dev.mindvr.tgplayground.command.buttons.reply;

import dev.mindvr.tgplayground.bot.TgBot;
import dev.mindvr.tgplayground.command.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class WithButtonsCallback implements Command {
    @Override
    public boolean isApplicable(Update update) {
        return Optional.of(update)
                .map(Update::getCallbackQuery)
                .map(CallbackQuery::getData)
                .filter(data -> data.startsWith("/with_buttons:"))
                .isPresent();
    }

    @Override
    public void handle(Update update, TgBot bot) {
        CallbackQuery callback = update.getCallbackQuery();

        Message message = callback.getMessage();
        WithButtonsMessage parsed = WithButtonsMessage.fromMessage(message);
        String payload = callback.getData().substring("/with_buttons:".length());
        if (payload.equals("+1")) {
            parsed.plusOne();
        } else if (payload.equals("+2")) {
            parsed.plusTwo();
        } else {
            throw new IllegalArgumentException(update.toString());
        }

        EditMessageText editMessage = parsed.asEditMessage();
        editMessage.setChatId(callback.getMessage().getChatId());
        editMessage.setMessageId(callback.getMessage().getMessageId());

        bot.execute(editMessage);
    }

}
