package timeBot.mainbot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.dto.MessageDTO;
import timeBot.services.Retransliator;

import java.io.File;
import java.util.List;

@Component
public class OldBotRetransliator implements BotInterface {

    private final Retransliator retransliator;

    private final BotService botService;

    public OldBotRetransliator(Retransliator retransliator, BotService botService) {
        this.retransliator = retransliator;
        this.botService = botService;
    }


    @Override
    public void sendMsg(String message, String chatId) {
        botService.sendMessageBase(true, true, Long.parseLong(chatId), null, message);

    }

    @Override
    public void deleteMsg(int messageId, String chatId) {
        botService.deleteMsg(messageId, chatId);
    }

    @Override
    public void sendMsgWithKeysHeroDB(Message message, String text, MessageDTO data) {
        botService.sendPhotoBase(message.getChat().getId(), null, data.getPicture(), null, null);
        botService.sendMessageBase(true, false,
                message.getChat().getId(), retransliator.createSkillButtons(data.getName(),
                        message.getFrom().getId().toString()), text);
    }

    @Override
    public void editMsgForSkills(Message message, MessageDTO messageDTO, String userId) {
        botService.editMessageBase(true, false,
                message.getChat().getId(), message.getMessageId(), retransliator.createSkillButtons(messageDTO.getName(), userId),
                message.getText().split("\\.", 2)[0] + ". \n\n" + messageDTO.getSkill());
    }

    @Override
    public void callBackNotice(String id, String text) {
        botService.callBackNotice(id, text);
    }

    @Override
    public void sendPhoto(String message, String chatId) {
        botService.sendPhotoBase(Long.parseLong(chatId), null, message, null, null);
    }

    @Override
    public void sendPhotoFile(File file, String chatId) {
        botService.sendPhotoBase(Long.parseLong(chatId), null, null, file, null);
    }

    @Override
    public void sendMsgWithKeysArtDB(Message message, String text, MessageDTO messageDTO) {
        botService.sendPhotoBase(message.getChat().getId(), null, messageDTO.getPicture(), null, null);
        botService.sendMessageBase(true, false, message.getChat().getId(),
                retransliator.createArtButtons(messageDTO.getName(), message.getFrom().getId().toString()), text);

    }

    @Override
    public void editMsgForArts(Message message, MessageDTO messageDTO, String userId) {
        botService.editMessageBase(true, false, message.getChat().getId(), message.getMessageId(),
                retransliator.createArtButtons(messageDTO.getName(), userId), message.getText().split("\\.", 2)[0] + ". \n\n" + messageDTO.getSkill());

    }

    @Override
    public void sendMsgWithKeysHeroChoise(Message message, String text, List<String> names) {
        botService.sendMessageBase(true, false, message.getChat().getId(),
                retransliator.createHeroChoiseButtons(names, message.getFrom().getId().toString()), text);
    }

    @Override
    public void sendMsgWithPic(String message, String chatId, String pic) {
        botService.sendPhotoBase(Long.parseLong(chatId), null, pic, null, null);
        botService.sendMessageBase(true, false, Long.parseLong(chatId), null, message);
    }

    @Override
    public void sendPullMessageFirst(Message msg, String imageCaption, String photo) {
        botService.sendPhotoBase(msg.getChatId(), imageCaption, photo, null, retransliator.createPullButtons(msg.getFrom().getId().toString()));
    }

    @Override
    public void sendPullMessageLast(CallbackQuery msg, String imageCaption, String photo) {
        botService.editPhotoMessageBase(msg.getMessage().getChatId(), msg.getMessage().getMessageId(), imageCaption, photo,
                retransliator.createPullButtons(msg.getFrom().getId().toString()));
    }
}
