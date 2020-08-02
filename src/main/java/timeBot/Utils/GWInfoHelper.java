package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.entity.GWInfoEntity;
import timeBot.mainbot.BotService;
import timeBot.repository.GWInfoRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class GWInfoHelper {

    private final GWInfoRepository gwInfoRepository;
    private final Helper helper;
    private final CreateButtonsInline createButtonsInline;
    private final BotService botService;


    public void gwNewInfoSave(Message msg) {
        String message = msg.getText();
        String baseId = message.substring(5, 7);
        baseId = baseId.replace(" ", "");
        Integer intId = Integer.valueOf(baseId);
        if (helper.checkBaseId(intId)) {
            GWInfoEntity gwInfoEntity = gwInfoRepository.getAllByBNumberAndChatId(intId, msg.getChatId().toString());
            if (gwInfoEntity == null) {
                GWInfoEntity gwInfoEntity1 = new GWInfoEntity();
                gwInfoEntity1.setBNumber(intId);
                gwInfoEntity1.setChatId(msg.getChatId().toString());
                gwInfoEntity1.setDate(new Timestamp(System.currentTimeMillis()));
                gwInfoEntity1.setInfo(message.substring(8));
                gwInfoEntity1.setMsgId(msg.getMessageId().toString());
                gwInfoRepository.save(gwInfoEntity1);
                botService.sendMessageBase(true, false, msg.getChatId(), null, "Записала инфу!");
            } else {
                gwInfoEntity.setDate(new Timestamp(System.currentTimeMillis()));
                String firstInfo = gwInfoEntity.getInfo();
                gwInfoEntity.setInfo(firstInfo + " Added info: " + message.substring(8));
                String messageId = gwInfoEntity.getMsgId();
                gwInfoEntity.setMsgId(messageId+":"+msg.getMessageId());
                gwInfoRepository.save(gwInfoEntity);


                botService.sendMessageBase(true, false, msg.getChatId(), null, "Добавила инфу!");
            }
        } else {
            botService.sendMessageBase(false, false, msg.getChatId(), null, "Не знаю я башни которую ты пытаешся вписать не буду записывать!");
        }
    }


    public void gwGetInfo(Message msg) {
        List<GWInfoEntity> gwInfoEntityList = gwInfoRepository.getAll();
        botService.sendMessageBase(true, false, msg.getChatId(),
                createButtonsInline.getBaseGWInfoButtons(gwInfoEntityList, msg.getFrom().getId().toString()), "Вся инфа на сегодня :");

    }

    public void gwGetInfoFromButton(CallbackQuery mes) {

        String[] newData = mes.getData().split(":");
        String userid = newData[2];
        if (userid.equals(mes.getFrom().getId().toString())) {
            GWInfoEntity gwInfoEntity = gwInfoRepository.getAllByBNumberAndChatId(Integer.valueOf(newData[1]), mes.getMessage().getChatId().toString());
            botService.editMessageBase(true, false, mes.getMessage().getChatId(), mes.getMessage().getMessageId(), null, gwInfoEntity.getInfo());
            String[] messageIds = gwInfoEntity.getMsgId().split(":");
            for (String mId: messageIds) {
                botService.sendReplyBase(true, false, mes.getMessage().getChatId(), null, "Репли сообщения с инфой", mId);
            }
        } else {
            botService.callBackNotice(mes.getId(), "Жмакать кнопки может только тот кто сделал запрос");
        }

    }

    public void deleteFromAGwBaseOneTower(int tower, String chatId){
        GWInfoEntity gwInfoEntitie = gwInfoRepository.getAllByBNumberAndChatId(tower, chatId);
        gwInfoRepository.delete(gwInfoEntitie);
    }

}
