package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import timeBot.services.Retransliator;

@Service
@AllArgsConstructor
public class MessageReceived {

    private final Retransliator retransliator;

    public void updatedMessage(Update update) {

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
                } else {
                    if (msg.getText().substring(1, 5).equals("hero")) {
                        retransliator.getNewChar(msg);
                    }
                }

                if (msg.getText().equals("/art") || msg.getText().equals("/art@tguardians_bot")) {
                } else {
                    if (msg.getText().substring(1, 4).equals("art")) {
                        retransliator.getNewArt(msg);
                    }
                }
                retransliator.connector(msg);
            }
        }
    }
}
