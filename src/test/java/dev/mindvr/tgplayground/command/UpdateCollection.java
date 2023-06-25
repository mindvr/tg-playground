package dev.mindvr.tgplayground.command;

import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.List;

public class UpdateCollection {

    public List<Update> regularUpdates() {
        return List.of(
                regularIncomingTextMessage(),
                stickerFromStickerPack(),
                callbackFromTextMessage()
        );
    }

    protected Update regularIncomingTextMessage() {
        Update update = updateWithMessage();

        update.getMessage().setText("55098928-cecd-4e95-91bd-56b2e6043089");
        return update;
    }

    protected Update stickerFromStickerPack() {
        Update update = updateWithMessage();

        PhotoSize thumb = new PhotoSize();
        thumb.setFileId("AAMCAgADGQEAAytkj0Kc4jXCOnvFLiep7iuOD0a0ngACzRwAAm8GeUpbi3wwsRTg1wEAB20AAy8E");
        thumb.setFileUniqueId("AQADzRwAAm8GeUpy");
        thumb.setWidth(320);
        thumb.setHeight(320);
        thumb.setFileSize(6728);

        Sticker sticker = new Sticker();
        sticker.setFileId("CAACAgIAAxkBAAMrZI9CnOI1wjp7xS4nqe4rjg9GtJ4AAs0cAAJvBnlKW4t8MLEU4NcvBA");
        sticker.setFileUniqueId("AgADzRwAAm8GeUo");
        sticker.setType("regular");
        sticker.setWidth(512);
        sticker.setHeight(512);
        sticker.setThumb(thumb);
        sticker.setFileSize(13402);
        sticker.setEmoji("🦆");
        sticker.setSetName("example");
        sticker.setIsAnimated(false);
        sticker.setIsVideo(false);

        update.getMessage().setSticker(sticker);

        return update;
    }

    protected Update callbackFromTextMessage() {
        Update update = update();
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("424196472871521082");
        callbackQuery.setFrom(fromUser());
        callbackQuery.setMessage(message());
        callbackQuery.getMessage().setText("asdfasdf");
        callbackQuery.setData("2fde2d14-e53c-494a-b715-28398031ac85");
        callbackQuery.setChatInstance("7030984829918810764");
        return update;
    }


    // contains update id, message
    // the message contains from and chat (same), date and id
    private Update updateWithMessage() {
        Update update = update();

        update.setMessage(message());

        return update;
    }

    private Update update() {
        Update update = new Update();
        update.setUpdateId(725892759);
        return update;
    }

    private User fromUser() {
        User fromUser = new User();
        fromUser.setId(1L);
        return fromUser;
    }

    private Message message() {
        Message message = new Message();

        message.setMessageId(43);
        message.setFrom(fromUser());
        message.setDate(1687107936);

        Chat chat = new Chat();
        chat.setId(1L);
        message.setChat(chat);
        return message;
    }
}
