package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@AllArgsConstructor
public class NewChatMembersInOut {


    private final String HI = "Приветствуем ";

    private final String EPICMESSAGE = " в нашем чате по епику (при спорах не переходи на личности, " +
            "\n никакой политики, религии и все будет хорошо =) )\n" +
            " для общения с ботом /help";


    public void newMember(Message message) {

//        String newMemberName = message.getNewChatMembers().get(0).getFirstName();
//        if (message.getChat().getId().toString().equals("-1001425195630"))
//            bot.sendMsg(HI + newMemberName + EPICMESSAGE, message.getChatId().toString());
//        if (message.getChat().getId().toString().equals("-1001429712030"))
//            bot.sendMsg(HI + newMemberName + EPICMESSAGE, message.getChatId().toString());
    }

}
