package timeBot.Utils;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public interface GetMessageEpicSevenInterface {

    void msgMap(Message msg);
}
