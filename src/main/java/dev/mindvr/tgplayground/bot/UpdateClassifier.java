package dev.mindvr.tgplayground.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.function.Function;

/**
 * Extracts chatId for replies
 */
@Component
public class UpdateClassifier implements Function<Update, Long> {
    @Override
    public Long apply(Update update) {
        return Optional.of(update)
                .map(Update::getMessage)
                .map(Message::getChatId)
                .orElse(null);
    }
}
