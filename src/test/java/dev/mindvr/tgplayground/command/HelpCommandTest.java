package dev.mindvr.tgplayground.command;

import dev.mindvr.tgplayground.bot.TgBot;
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
    HelpCommand command = new HelpCommand();
    String text = "help-message";
    Update update = new Update();
    Message message = new Message();
    Chat chat = new Chat();


    @BeforeEach
    void setup() {
        command.setHelp(text);
        update.setMessage(message);
        message.setChat(chat);
    }

    @Test
    void testIsApplicable_not() {
        new UpdateCollection().regularUpdates()
                .forEach(update -> assertFalse(command.isApplicable(update)));
    }

    @Test
    void testIsApplicable() {
        message.setText("hello");
        assertFalse(command.isApplicable(update));
        message.setText("/help");
        assertTrue(command.isApplicable(update));
    }

    @Test
    void testHandle() {
        chat.setId(42L);

        TgBot bot = mock(TgBot.class);
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        command.handle(update, bot);
        verify(bot).execute(captor.capture());
        SendMessage sentMessage = captor.getValue();

        assertEquals("42", sentMessage.getChatId());
        assertEquals(text, sentMessage.getText());
    }

}