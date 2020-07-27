package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.entity.PollEntity;
import timeBot.mainbot.BotInterface;
import timeBot.repository.PollRollRepository;

import java.sql.Timestamp;

@Component
@AllArgsConstructor
public class StartPolling {

    private final PollRollRepository pollRollRepository;
    private final RandomPoll randomPoll;
    private final BotInterface bot;


    public void newPoll(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split(":");
        String userid = data[2];
        if (userid.equals(callbackQuery.getFrom().getId().toString())) {
            Message message = new Message();
            randomPoll.getRandomHero(message, callbackQuery, false);
        } else {
            bot.callBackNotice(callbackQuery.getId(), "Не тебе тут жмакать падаван");
        }
    }

    public boolean usersPollTest(Message message) {
        if (pollRollRepository.getAllByUserId(message.getFrom().getId().toString()) == null) {
            PollEntity newPoll = new PollEntity();
            newPoll.setUserId(message.getFrom().getId().toString());
            newPoll.setDate(new Timestamp(System.currentTimeMillis()));
            pollRollRepository.save(newPoll);
            return true;
        } else {
            PollEntity pollEntity = pollRollRepository.getAllByUserId(message.getFrom().getId().toString());
            long razn = new Timestamp(System.currentTimeMillis()).getTime() - pollEntity.getDate().getTime();
            long waitingTime = 720 - razn/(60*1000);
            if (waitingTime <= 0) {
                pollEntity.setDate(new Timestamp(System.currentTimeMillis()));
                pollRollRepository.save(pollEntity);
                return true;
            }
        }
        return false;
    }


}
