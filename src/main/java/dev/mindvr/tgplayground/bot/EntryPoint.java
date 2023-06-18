package dev.mindvr.tgplayground.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mindvr.tgplayground.command.Command;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Component
@Slf4j
public class EntryPoint extends TelegramLongPollingBot {
    private final String token;
    private final String name;
    private final ObjectMapper objectMapper;
    @Setter
    private List<Command> commands;
    final TgBot wrapper;

    @Autowired
    public EntryPoint(
            @Value("${tg.token:}") String token,
            @Value("${tg.name:}") String name,
            List<Command> commands,
            ObjectMapper objectMapper) {
        this.token = token;
        this.name = name;
        this.commands = commands;
        this.objectMapper = objectMapper;
        this.wrapper = new TgBot(this);
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        log.info("{}", objectMapper.writeValueAsString(update));
        commands.stream()
                .filter(command -> command.isApplicable(update))
                .findAny()
                .ifPresent(command -> command.handle(update, wrapper));
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @PostConstruct
    public void registerBot() throws Exception {
        if (token.isBlank()) return;
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(this);
    }
}
