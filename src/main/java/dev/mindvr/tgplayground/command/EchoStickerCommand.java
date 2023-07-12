package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.TgBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Optional;

@Component
public class EchoStickerCommand implements Command {
    @Override
    public boolean isApplicable(Update update) {
        return Optional.of(update)
                .map(Update::getMessage)
                .map(Message::getSticker)
                .isPresent();
    }

    @Override
    public void handle(Update update, TgBot bot) {
        Sticker s = update.getMessage().getSticker();
        String message = """
                %s sticker type: %s
                from %s file_unique_id: %s
                file_id: %s""".formatted(
                s.getEmoji(), s.getType(),
                s.getSetName(), s.getFileUniqueId(),
                s.getFileId());
        SendMessage reply = SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text(message)
                .build();
        bot.execute(reply);
    }
}
