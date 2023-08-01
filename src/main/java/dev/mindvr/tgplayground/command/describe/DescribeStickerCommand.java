package dev.mindvr.tgplayground.command.describe;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mindvr.tgplayground.bot.UpdateContext;
import dev.mindvr.tgplayground.command.Command;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DescribeStickerCommand implements Command {
    private static final String prefix = "/describe";
    private final ObjectMapper objectMapper;

    @Override
    public boolean isApplicable(UpdateContext update) {
        return previousDescribeCommand(update).isPresent() && isSticker(update.getUpdate());
    }

    private enum Format {DEFAULT, JSON, REMINDER}

    private Optional<String> previousDescribeCommand(UpdateContext update) {
        return Optional.of(update)
                .map(UpdateContext::getPrevious)
                .filter(not(List::isEmpty))
                .map(u -> u.get(0))
                .map(Update::getMessage)
                .map(Message::getText)
                .filter(text -> text.startsWith(prefix))
                .map(text -> text.substring(prefix.length()));
    }

    private boolean isSticker(Update update) {
        return Optional.of(update)
                .map(Update::getMessage)
                .map(Message::getSticker)
                .isPresent();
    }

    private Format getFormat(UpdateContext update) {
        String reminder = previousDescribeCommand(update).orElseThrow().trim().toUpperCase();
        return Arrays.stream(Format.values())
                .filter(fmt -> fmt.name().equals(reminder))
                .findAny().orElse(Format.DEFAULT);
    }


    @Override
    public void handle(UpdateContext update) {
        Format format = getFormat(update);
        Sticker s = update.getUpdate().getMessage().getSticker();
        SendMessage reply = SendMessage.builder()
                .chatId(update.getUpdate().getMessage().getFrom().getId())
                .text(formatReply(s, format))
                .build();
        update.getBot().execute(reply);
    }

    @SneakyThrows
    private String formatReply(Sticker s, Format fmt) {
        return switch (fmt) {
            case JSON -> objectMapper.writeValueAsString(s);
            case REMINDER -> objectMapper.writeValueAsString(new Reminder(s));
            case DEFAULT -> """
                    %s sticker type: %s
                    from %s file_unique_id: %s
                    file_id: %s""".formatted(
                    s.getEmoji(), s.getType(),
                    s.getSetName(), s.getFileUniqueId(),
                    s.getFileId());
        };
    }

    @Data
    public static class Reminder {
        private String name;
        private String file;
        private int weight = 1;

        public Reminder(Sticker s) {
            this.name = s.getSetName() + s.getFileUniqueId();
            this.file = s.getFileId();
        }
    }
}
