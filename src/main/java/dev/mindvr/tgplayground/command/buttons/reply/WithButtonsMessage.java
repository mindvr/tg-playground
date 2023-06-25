package dev.mindvr.tgplayground.command.buttons.reply;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Data
class WithButtonsMessage {
    private static final String prefix = "/with_buttons:";
    private int total = 0;
    private int plusOneCount = 0;
    private int plusTwoCount = 0;

    private String text() {
        return "Count: " + total;
    }

    private String left() {
        return "+1 (%s)".formatted(plusOneCount);
    }

    private String right() {
        return "+2 (%s)".formatted(plusTwoCount);
    }

    public SendMessage asSendMessage() {
        SendMessage message = new SendMessage();

        message.setText(text());
        message.setReplyMarkup(of(
                button(left(), prefix + "+1"), button(right(), prefix + "+2")
        ));

        return message;
    }

    public EditMessageText asEditMessage() {
        EditMessageText message = new EditMessageText();

        message.setText(text());
        message.setReplyMarkup(of(
                button(left(), prefix + "+1"), button(right(), prefix + "+2")
        ));

        return message;
    }


    public static WithButtonsMessage fromMessage(Message message) {
        int total = Integer.parseInt(message.getText().substring("Count: ".length()));
        var buttons = message.getReplyMarkup().getKeyboard().get(0);
        int plusOne = fromButton(buttons.get(0));
        int plusTwo = fromButton(buttons.get(1));
        WithButtonsMessage parsed = new WithButtonsMessage();
        parsed.setTotal(total);
        parsed.setPlusOneCount(plusOne);
        parsed.setPlusTwoCount(plusTwo);
        return parsed;
    }

    public void plusOne() {
        this.plusOneCount++;
        this.total += 1;
    }

    public void plusTwo() {
        this.plusTwoCount++;
        this.total += 2;
    }

    private static int fromButton(InlineKeyboardButton button) {
        String text = button.getText();
        return Integer.parseInt(text.substring("+n (".length(), text.length() - 1));
    }

    private InlineKeyboardMarkup of(InlineKeyboardButton... buttons) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(List.of(buttons)));
        return keyboard;
    }

    private InlineKeyboardButton button(String text, String data) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(data)
                .build();
    }
}
