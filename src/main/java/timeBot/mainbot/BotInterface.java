package timeBot.mainbot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.dto.MessageDTO;

import java.io.File;
import java.util.List;

@Service
public interface BotInterface {


    void sendMsg(String message, String chatId);

    void deleteMsg(int messageId, String chatId);

    void sendMsgWithKeysHeroDB(Message message, String text, MessageDTO data);

    void editMsgForSkills(Message message, MessageDTO messageDTO, String userId);

    void callBackNotice(String id, String text);

    void sendPhoto(String message, String chatId);

    void sendPhotoFile(File file, String chatId);

    void sendMsgWithKeysArtDB(Message message, String text, MessageDTO messageDTO);

    void editMsgForArts(Message message, MessageDTO messageDTO, String userId);

    void sendMsgWithKeysHeroChoise(Message message, String text, List<String> names);

    void sendMsgWithPic(String message, String chatId, String pic);

    void sendPullMessageFirst(Message msg, String imageCaption, String photo);

    void sendPullMessageLast(CallbackQuery msg, String imageCaption, String photo);
}
