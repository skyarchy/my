package timeBot.mainbot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;

@Service
public interface BotService {

    void sendMessageBase(boolean html, boolean disableWebPreviw, Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup, String text);

    void editMessageBase(boolean html, boolean disableWebPreviw, Long chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup, String text);

    void sendPhotoBase(Long chatId, String imageCaption, String photo, File filePhoto, InlineKeyboardMarkup inlineKeyboardMarkup);

    void editPhotoMessageBase(Long chatId, Integer messageId, String imageCaption, String photo, InlineKeyboardMarkup inlineKeyboardMarkup);

    void callBackNotice(String id, String text);

    void deleteMsg(int messageId, String chatId);

    void sendReplyBase(boolean html, boolean disableWebPreviw, Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup, String text, String replyMessageId);

}
