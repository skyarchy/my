package timeBot.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import timeBot.Utils.*;

import java.util.List;

@Component
public class LazyForBot {

    private GetMessageEpicSevenInterface getMessageEpicSevenInterface;
    private NewChatMembersInOut newChatMembersIn;
    private CreateButtonsInline createButtonsInline;
    private AddDeleteNewStreamer addDeleteNewStreamer;
    private BaseMessagesEpic newCharBaseMessagesEpic;
    private StartPolling startPolling;

    public LazyForBot(@Lazy GetMessageEpicSevenInterface getMessageEpicSevenInterface, @Lazy NewChatMembersInOut newChatMembersIn,
                      @Lazy CreateButtonsInline createButtonsInline,
                      @Lazy AddDeleteNewStreamer addDeleteNewStreamer, @Lazy BaseMessagesEpic newCharBaseMessagesEpic, @Lazy StartPolling startPolling) {
        this.getMessageEpicSevenInterface = getMessageEpicSevenInterface;
        this.newChatMembersIn = newChatMembersIn;
        this.createButtonsInline = createButtonsInline;
        this.addDeleteNewStreamer = addDeleteNewStreamer;
        this.newCharBaseMessagesEpic = newCharBaseMessagesEpic;
        this.startPolling = startPolling;

    }

    public void connector(@Lazy Message msg) {
        getMessageEpicSevenInterface.msgMap(msg);
    }

    public void addNewMember(@Lazy Message msg) {
        newChatMembersIn.newMember(msg);
    }

    public InlineKeyboardMarkup createSkillButtons(String name, String userId) {
        return createButtonsInline.createSkillButtons(name, userId);
    }
    public InlineKeyboardMarkup createArtButtons(String name, String userId) {
        return createButtonsInline.createArtButtons(name, userId);
    }

    public InlineKeyboardMarkup createHeroChoiseButtons(List<String> names, String userId) {
        return createButtonsInline.createHeroButtons(names, userId);
    }


    public void addStreamer(String data) {
        addDeleteNewStreamer.addStreamer(data);
    }

    public void deleteStreamer(String data) {
        addDeleteNewStreamer.deleteStreamer(data);
    }

    public String getStreamer(String data) {
        return addDeleteNewStreamer.getStreamer(data);
    }

    public void getNewChar(@Lazy Message msg) {
        newCharBaseMessagesEpic.getHeroMessage(msg);
    }

    public void getNewSkill(@Lazy CallbackQuery msg) {
        newCharBaseMessagesEpic.getSkill(msg);
    }

    public void getNewArt(@Lazy Message msg){
        newCharBaseMessagesEpic.getArtMin(msg);
    }

    public void getArtMinMax(@Lazy CallbackQuery msg){
        newCharBaseMessagesEpic.getMinMaxArtButton(msg);
    }

    public void startPoll(CallbackQuery callbackQuery){
        startPolling.newPoll(callbackQuery);
    }

    public boolean testUserPoll(Message message){
        return startPolling.usersPollTest(message);
    }

    public InlineKeyboardMarkup createPullButtons(String userId) {
        return createButtonsInline.createPollButtons("tata", userId);
    }

}
