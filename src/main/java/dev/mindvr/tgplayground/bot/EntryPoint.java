package dev.mindvr.tgplayground.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mindvr.tgplayground.command.Command;
import dev.mindvr.tgplayground.persistence.UpdateRepository;
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
    private final UpdateContextFactory contextFactory;
    private final UpdateRepository updateRepository;
    final TgBot wrapper;

    @Autowired
    public EntryPoint(
            @Value("${tg.token:}") String token,
            @Value("${tg.name:}") String name,
            ObjectMapper objectMapper,
            List<Command> commands,
            UpdateContextFactory contextFactory,
            UpdateRepository updateRepository) {
        this.token = token;
        this.name = name;
        this.objectMapper = objectMapper;
        this.commands = commands;
        this.contextFactory = contextFactory;
        this.updateRepository = updateRepository;
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
        update = updateRepository.save(update);
        UpdateContext context = contextFactory.build(update, this.wrapper);
        commands.stream()
                .filter(command -> command.isApplicable(context))
                .findAny()
                .ifPresent(command -> command.handle(context));
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
