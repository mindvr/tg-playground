package dev.mindvr.tgplayground.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

@Component
public class UpdateContextFactory {

    public UpdateContext build(Update update, TgBot bot) {
        UpdateContext context = new UpdateContext();
        context.setBot(bot);
        context.setUpdate(update);
        context.setPrevious(Collections.emptyList());
        return context;
    }
}
