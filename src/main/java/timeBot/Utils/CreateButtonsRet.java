package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import timeBot.entity.GWInfoEntity;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateButtonsRet {

    private final CreateButtonsInline createButtonsInline;

    public InlineKeyboardMarkup gwButtonsCreate(List<GWInfoEntity> gwInfoEntities, String userId){
        List<String> callBackText = new ArrayList<>();
        List<String> text = new ArrayList<>();
        for(GWInfoEntity data : gwInfoEntities){
            callBackText.add("getGWInfoFromBase:" + data.getBNumber() + ":" + userId);
            text.add(String.valueOf(data.getBNumber()));
        }
        return createButtonsInline.createNewButtons(text, callBackText, 5);
    }
}
