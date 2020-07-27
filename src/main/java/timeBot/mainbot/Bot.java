package timeBot.mainbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timeBot.dto.MessageDTO;
import timeBot.services.Retransliator;

import java.io.File;
import java.util.List;


@Component
public class Bot extends TelegramLongPollingBot implements BotInterface {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.name}")
    private String name;

    private final Retransliator retransliator;

    public Bot(Retransliator retransliator) {
        this.retransliator = retransliator;
    }


    public synchronized void pinMsg(int messageId, String chatId) {
        PinChatMessage pinMessage = new PinChatMessage();
        pinMessage.setChatId(chatId);
        pinMessage.setMessageId(messageId);
        try {
            execute(pinMessage);
        } catch (TelegramApiException e) {
            //ignored
        }

    }

    //"-1001425195630"
    public synchronized void sendMediaGroup(List<InputMedia> message, String chatId) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        sendMediaGroup.setMedia(message);
        try {
            execute(sendMediaGroup);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendPhoto(String message, String chatId) {
        SendPhoto sendMessage = new SendPhoto();
        sendMessage.setChatId(chatId);
        sendMessage.setPhoto(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendPhotoWithCaption(String message, String imageCaption, String chatId) {
        SendPhoto sendMessage = new SendPhoto();
        sendMessage.setChatId(chatId);
        sendMessage.setPhoto(message);
        sendMessage.setCaption(imageCaption);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void editMessageCaption(String caption, String chatId, Message message) {
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setCaption(caption);
        editMessageCaption.setChatId(chatId);
        editMessageCaption.setMessageId(message.getMessageId());

        try {
            execute(editMessageCaption);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendPhotoFile(File file, String chatId) {

        SendPhoto sendMessage = new SendPhoto();
        sendMessage.setChatId(chatId);
        sendMessage.setPhoto(file);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendMsg(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.disableWebPagePreview();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }
    public synchronized void sendPullMessageFirst(Message msg, String imageCaption, String photo) {
        SendPhoto sendMessage = new SendPhoto();
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setPhoto(photo);
        sendMessage.setCaption(imageCaption);
        sendMessage.setReplyMarkup(retransliator.createPullButtons(msg.getFrom().getId().toString()));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendPullMessageLast(CallbackQuery msg, String imageCaption, String photo) {
        EditMessageMedia sendMessage = new EditMessageMedia();
        InputMediaPhoto phoooto = new InputMediaPhoto();
        phoooto.setMedia(photo).setCaption(imageCaption);
        sendMessage.setChatId(msg.getMessage().getChatId());
        sendMessage.setMessageId(msg.getMessage().getMessageId());
        sendMessage.setMedia(phoooto);
        sendMessage.setReplyMarkup(retransliator.createPullButtons(msg.getFrom().getId().toString()));

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendMsgWithPic(String message, String chatId, String pic) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.disableWebPagePreview();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendPhoto(pic, chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendStreamMsg(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }


    public synchronized void sendMsgWithKeysHeroDB(Message message, String text, MessageDTO messageDTO) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(message.getChat().getId());

        sendMessage.setReplyMarkup(retransliator.createSkillButtons(messageDTO.getName(), message.getFrom().getId().toString()));

        sendMessage.setText(text);
        sendPhoto(messageDTO.getPicture(), message.getChat().getId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendMsgWithKeysArtDB(Message message, String text, MessageDTO messageDTO) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(message.getChat().getId());

        sendMessage.setReplyMarkup(retransliator.createArtButtons(messageDTO.getName(), message.getFrom().getId().toString()));

        sendMessage.setText(text);
        sendPhoto(messageDTO.getPicture(), message.getChat().getId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

    public synchronized void sendMsgWithKeysHeroChoise(Message message, String text, List<String> names) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(message.getChat().getId());

        sendMessage.setReplyMarkup(retransliator.createHeroChoiseButtons(names, message.getFrom().getId().toString()));

        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }


    public synchronized void editMsgForSkills(Message message, MessageDTO messageDTO, String userId) {
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setMessageId(message.getMessageId());
        sendMessage.setText(message.getText().split("\\.", 2)[0] + ". \n\n" + messageDTO.getSkill());
        sendMessage.setReplyMarkup(retransliator.createSkillButtons(messageDTO.getName(), userId));
        sendMessage.enableHtml(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }

    }

    public synchronized void editMsgForArts(Message message, MessageDTO messageDTO, String userId) {
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setMessageId(message.getMessageId());
        sendMessage.setText(message.getText().split("\\.", 2)[0] + ". \n\n" + messageDTO.getSkill());
        sendMessage.setReplyMarkup(retransliator.createArtButtons(messageDTO.getName(), userId));
        sendMessage.enableHtml(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }

    }

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

    public synchronized void answerCollBackQuery(AnswerCallbackQuery answerCallbackQuery) {
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
//            System.out.println(e);
        }
    }

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

    public void onUpdateReceived(Update update) {

//        update.getCallbackQuery().getData();
        if (update.getCallbackQuery() != null) {
            String text = update.getCallbackQuery().getData();
            if (text.contains("getSkill") || text.contains("awake")) {
                retransliator.getNewSkill(update.getCallbackQuery());
            }
            if (text.contains("getArt")) {
                retransliator.getArtMinMax(update.getCallbackQuery());
            }
            if (text.contains("getHeroName")) {
                retransliator.getNewSkill(update.getCallbackQuery());
            }
            if (text.contains("getNext")) {
                retransliator.startPoll(update.getCallbackQuery());
            }
        }
        Message msg = update.getMessage();
        if (msg != null) {
            if (msg.getText().length() > 4) {

                if (msg.getNewChatMembers() != null && !msg.getNewChatMembers().isEmpty()) {
                    retransliator.addNewMember(msg);
                }

                if (msg.getText().equals("/hero") || msg.getText().equals("/hero@tguardians_bot")) {
                    deleteMsg(msg.getMessageId(), msg.getChat().getId().toString());
                } else {
                    if (msg.getText().substring(1, 5).equals("hero")) {
                        retransliator.getNewChar(msg);
                    }
                }

                if (msg.getText().equals("/art") || msg.getText().equals("/art@tguardians_bot")) {
                    deleteMsg(msg.getMessageId(), msg.getChat().getId().toString());
                } else {
                    if (msg.getText().substring(1, 4).equals("art")) {
                        retransliator.getNewArt(msg);
                    }
                }
                retransliator.connector(msg);
            }
        }

    }

    public String getBotUsername() {
        return name;
    }

    public String getBotToken() {
        return token;
    }


}
