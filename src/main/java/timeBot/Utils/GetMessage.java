package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.dto.StreamsDTO;
import timeBot.entity.PollEntity;
import timeBot.entity.StreamersDataEntity;
import timeBot.mainbot.BotInterface;
import timeBot.repository.PollRollRepository;
import timeBot.repository.StreamersDataRepository;
import timeBot.services.AddArtsInBase;
import timeBot.services.AddHeroesInBase;
import timeBot.services.CatsAPI;
import timeBot.services.Retransliator;
import timeBot.twitchapi.GetStream;

import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class GetMessage implements GetMessageEpicSevenInterface {

    private final BotInterface bot;
    private final GetStream getStream;
    private final StreamersDataRepository repository;
    private final Retransliator retransliator;
    private final CatsAPI catsAPI;
    private final AddHeroesInBase addHeroesInBase;
    private final AddArtsInBase addArtsInBase;
    private final PollRollRepository pollRollRepository;
    private final RandomPoll randomPoll;


    public void msgMap(Message msg) {

        String message = msg.getText();
        if (message.equals("/help") || message.equals("/help@tguardians_bot")) {
            bot.sendMsg("<b>Все команды</b> - \n " +
                            "/streamers - показывает зарегестрированных стримеров \n" +
                            "/onlineStreamers - показывет включены ли стримы из тех что зарегестрированы \n" +
                            "/randomCat - показывает рандомную картиночку котика из интернетав \n" +
                            "/pull - рандом призыв \n" +
                            "\n" +
                            "Для того чтобы пользоваться базой персонажей нужно ввести \"/hero имя персонажа\"" +
                            "\n" +
                            "Для того чтобы пользоваться базой артов нужно ввести \"/art имя арта\""
                    , msg.getChatId().toString());
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.equals("/help1") || message.equals("/help1@tguardians_bot")) {
            bot.sendMsg("<b>Полезные ссылки</b> - \n" +
                            "<a href=\"https://page.onstove.com/epicseven/global/list/e7en001?listType=2&direction=latest&page=1\">Ссыль на главную страницу с новостями по епику</a> \n" +
                            "<a href=\"https://epic7x.com/\">Cсыль на вики по епику</a> \n" +
                            "<a href=\"https://maphe.github.io/e7-damage-calc/\">Ccыль на калькулятор дамага</a> \n" +
                            "/katal - показывает где фармить каталы"
                    , msg.getChatId().toString());
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.equals("/adminHelp")) {
            if (adminTest(msg.getFrom().getId().toString())) {
                bot.sendMsg("<b>Все команды</b> - \n " +
                                "addStreamer%name;code \n" +
                                "deleteStreamer%name;code \n" +
                                "getStreamer%name \n" +
                                "refreshherodb \n" +
                                "refreshartsdb"
                        , msg.getChatId().toString());
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.contains("/deleteStreamer")) {
            if (adminTest(msg.getFrom().getId().toString())) {
                retransliator.deleteStreamer(message.split("%")[1]);
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.contains("/refreshherodb")) {
            if (adminTest(msg.getFrom().getId().toString())) {
                addHeroesInBase.addHeroesInDB();
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.contains("/refreshartsdb")) {
            if (adminTest(msg.getFrom().getId().toString())) {
                addArtsInBase.addArtsInDb();
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.contains("/addStreamer")) {
            if (adminTest(msg.getFrom().getId().toString())) {
                retransliator.addStreamer(message.split("%")[1]);
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.contains("/getStreamer")) {
            if (adminTest(msg.getFrom().getId().toString())) {
                bot.sendMsg(retransliator.getStreamer(message.split("%")[1]), msg.getChatId().toString());
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }
//        "292174947"; я
        //   408041591 тима

        message = message.toLowerCase();


        if (message.equals("/test")) {

            bot.sendMsg("test", msg.getChatId().toString());
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());

        }


        if (message.equals("/pull") || message.equals("/pull@tguardians_bot")) {
            CallbackQuery callbackQuery = new CallbackQuery();
            if (msg.getFrom().getId().toString().equals("292174947")) {
                randomPoll.getRandomHero(msg, callbackQuery, true);
            } else {
                if (retransliator.testUserPoll(msg)) {
                    randomPoll.getRandomHero(msg, callbackQuery, true);
                } else {
                    PollEntity pollEntity = pollRollRepository.getAllByUserId(msg.getFrom().getId().toString());
                    long razn = new Timestamp(System.currentTimeMillis()).getTime() - pollEntity.getDate().getTime();
                    long waitingTime = 720 - razn / (60 * 1000);
                    String name = msg.getFrom().getFirstName();
                    String text = name + " твое время еще не пришло до пулла ждать - " + waitingTime + "мин.";
                    bot.sendMsg(text, msg.getChatId().toString());
                }
                bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.equals("/katal") || message.equals("/katal@tguardians_bot")) {
            String path = System.getProperty("user.dir") + File.separator + "katatal.png";
            File file = new File(path);
            bot.sendPhotoFile(file, msg.getChatId().toString());
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }


        if (message.equals("/randomcat") || message.equals("/randomcat@tguardians_bot")) {

            bot.sendPhoto(catsAPI.getCatPic(), msg.getChat().getId().toString());
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

        if (message.equals("/streamers") || message.equals("/streamers@tguardians_bot")) {
            try {
                for (StreamersDataEntity data : repository.getAll()) {

                    bot.sendMsg(data.getName(), msg.getChatId().toString());
                }
            } catch (Exception e) {
                // ignored
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }
        if (message.equals("/onlinestreamers") || message.equals("/onlinestreamers@tguardians_bot")) {
            AtomicBoolean yes = new AtomicBoolean(true);
            try {
                for (StreamersDataEntity ids : repository.getAll()) {
                    try {
                        StreamsDTO data = getStream.getStream(ids.getCode());
                        if (data != null) {
                            bot.sendMsg("Стрим идет. Game - " + data.getGameName() + "\n" +
                                    " <a href=\"" + data.getChannelUrl() + "\">" + ids.getName() + "</a>", msg.getChatId().toString());
                            yes.set(false);
                        }
                    } catch (Exception e) {
                        //ignored
                    }
                }
            } catch (Exception e) {
                // ignored
            }
            if (yes.get()) {
                bot.sendMsg("No Streams up", msg.getChatId().toString());
            }
            bot.deleteMsg(msg.getMessageId(), msg.getChatId().toString());
        }

    }

    private boolean arrayCheck(Object t) {
        return t instanceof JSONArray;
    }

    private boolean adminTest(String id) {
        return "292174947;408041591".contains(id);
    }


}
