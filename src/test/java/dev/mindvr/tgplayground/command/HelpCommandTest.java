package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.TgBot;
import dev.mindvr.tgplayground.bot.UpdateContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HelpCommandTest {
    UpdateContext context = new UpdateContext();
    HelpCommand command = new HelpCommand();
    String text = "help-message";
    Update update = new Update();
    Message message = new Message();
    Chat chat = new Chat();


    @BeforeEach
    void setup() {
        context.setUpdate(update);
        command.setHelp(text);
        update.setMessage(message);
        message.setChat(chat);
    }

    @Test
    void testIsApplicable_not() {
        new UpdateCollection().regularUpdates()
                .stream()
                .map(update -> {
                    var context = new UpdateContext();
                    context.setUpdate(update);
                    return context;
                })
                .forEach(update -> assertFalse(command.isApplicable(update)));
    }

    @Test
    void testIsApplicable() {
        message.setText("hello");
        assertFalse(command.isApplicable(context));
        message.setText("/help");
        assertTrue(command.isApplicable(context));
    }

    @Test
    void testHandle() {
        chat.setId(42L);

        TgBot bot = mock(TgBot.class);
        context.setBot(bot);
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        command.handle(context);
        verify(bot).execute(captor.capture());
        SendMessage sentMessage = captor.getValue();

        assertEquals("42", sentMessage.getChatId());
        assertEquals(text, sentMessage.getText());
    }

}