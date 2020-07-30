package timeBot.mainbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timeBot.Utils.MessageReceived;

import java.io.File;


@Component
public class Bot extends TelegramLongPollingBot implements BotService {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.name}")
    private String name;

    private final MessageReceived messageReceived;

    public Bot(MessageReceived messageReceived) {
        this.messageReceived = messageReceived;
    }


    @Override
    public synchronized void sendMessageBase(boolean html, boolean disableWebPreviw, Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(html);
        if (disableWebPreviw)
            sendMessage.disableWebPagePreview();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    @Override
    public synchronized void editMessageBase(boolean html, boolean disableWebPreviw, Long chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup, String text) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.enableHtml(html);
        if (disableWebPreviw)
            editMessage.disableWebPagePreview();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        editMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }

    }

    @Override
    public synchronized void sendPhotoBase(Long chatId, String imageCaption, String photo, File filePhoto, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        sendPhoto.setPhoto(photo);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        if (filePhoto != null)
            sendPhoto.setPhoto(filePhoto);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    @Override
    public synchronized void editPhotoMessageBase(Long chatId, Integer messageId, String imageCaption, String photo, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
        inputMediaPhoto.setMedia(photo).setCaption(imageCaption);
        editMessageMedia.setMedia(inputMediaPhoto);
        editMessageMedia.setChatId(chatId);
        editMessageMedia.setMessageId(messageId);
        editMessageMedia.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(editMessageMedia);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    @Override
    public synchronized void callBackNotice(String id, String text) {
        AnswerCallbackQuery sendMessage = new AnswerCallbackQuery();
        sendMessage.setShowAlert(false);
        sendMessage.setText(text);
        sendMessage.setCallbackQueryId(id);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    @Override
    public synchronized void deleteMsg(int messageId, String chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    @Override
    public synchronized void sendReplyBase(boolean html, boolean disableWebPreviw, Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup, String text, String replyMessageId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyToMessageId(Integer.valueOf(replyMessageId));
        sendMessage.enableHtml(html);
        if (disableWebPreviw)
            sendMessage.disableWebPagePreview();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public void onUpdateReceived(Update update) {
        messageReceived.updatedMessage(update);
    }

    public String getBotUsername() {
        return name;
    }

    public String getBotToken() {
        return token;
    }


}
