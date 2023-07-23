package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.UpdateContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Optional;

@Component
public class EchoStickerCommand implements Command {
    @Override
    public boolean isApplicable(UpdateContext update) {
        return Optional.of(update)
                .map(UpdateContext::getUpdate)
                .map(Update::getMessage)
                .map(Message::getSticker)
                .isPresent();
    }

    @Override
    public void handle(UpdateContext update) {
        Sticker s = update.getUpdate().getMessage().getSticker();
        String message = """
                %s sticker type: %s
                from %s file_unique_id: %s
                file_id: %s""".formatted(
                s.getEmoji(), s.getType(),
                s.getSetName(), s.getFileUniqueId(),
                s.getFileId());
        SendMessage reply = SendMessage.builder()
                .chatId(update.getUpdate().getMessage().getFrom().getId())
                .text(message)
                .build();
        update.getBot().execute(reply);
    }
}
