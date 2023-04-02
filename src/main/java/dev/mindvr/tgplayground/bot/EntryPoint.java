package dev.mindvr.tgplayground.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class EntryPoint extends TelegramLongPollingBot {
    private final String token;
    private final String name;

    @Autowired
    public EntryPoint(
            @Value("${tg.token}") String token,
            @Value("${tg.name}") String name) {
        this.token = token;
        this.name = name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("{}", update);
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @PostConstruct
    public void registerBot() throws Exception {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(this);
    }
}
