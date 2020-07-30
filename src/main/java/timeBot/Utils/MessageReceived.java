package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import timeBot.services.LazyForBot;

@Service
@AllArgsConstructor
public class MessageReceived {

    private final LazyForBot retransliator;

    public void updatedMessage(Update update) {

        if (update.getCallbackQuery() != null) {
            String text = update.getCallbackQuery().getData();

            if (text.contains("getGWInfoFromBase")) {
                retransliator.gwGetInfoFromButton(update.getCallbackQuery());
            }

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
                retransliator.connector(msg);
            }
        }
    }
}
