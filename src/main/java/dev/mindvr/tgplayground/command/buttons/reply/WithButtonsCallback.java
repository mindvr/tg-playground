package dev.mindvr.tgplayground.command.buttons.reply;

import dev.mindvr.tgplayground.bot.UpdateContext;
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
    public boolean isApplicable(UpdateContext update) {
        return Optional.of(update)
                .map(UpdateContext::getUpdate)
                .map(Update::getCallbackQuery)
                .map(CallbackQuery::getData)
                .filter(data -> data.startsWith("/with_buttons:"))
                .isPresent();
    }

    @Override
    public void handle(UpdateContext update) {
        CallbackQuery callback = update.getUpdate().getCallbackQuery();

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

        update.getBot().execute(editMessage);
    }

}
