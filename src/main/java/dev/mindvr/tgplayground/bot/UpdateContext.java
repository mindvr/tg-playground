package dev.mindvr.tgplayground.bot;


import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Data
public class UpdateContext {
    private Update update;
    // from current chat, newer first
    private List<Update> previous;
    private TgBot bot;
}
