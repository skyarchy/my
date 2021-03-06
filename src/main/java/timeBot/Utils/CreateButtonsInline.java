package timeBot.Utils;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import timeBot.Utils.exception.BotAllException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CreateButtonsInline {

    public InlineKeyboardMarkup createSkillButtons(String name, String userId) {

        InlineKeyboardButton skill1button = new InlineKeyboardButton().setText("Скилл1").setCallbackData("getSkill1frombot:" + name + ":" + userId);
        InlineKeyboardButton skill2button = new InlineKeyboardButton().setText("Скилл2").setCallbackData("getSkill2frombot:" + name + ":" + userId);
        InlineKeyboardButton skill3button = new InlineKeyboardButton().setText("Скилл3").setCallbackData("getSkill3frombot:" + name + ":" + userId);
        InlineKeyboardButton lvl60notAwake = new InlineKeyboardButton().setText("lvl 60 notAwake").setCallbackData("awakenolvl60:" + name + ":" + userId);
        InlineKeyboardButton lvl60awake = new InlineKeyboardButton().setText("lvl 60 Awake").setCallbackData("awakeyeslvl60:" + name + ":" + userId);


        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        rowInline.add(skill1button);
        rowInline.add(skill2button);
        rowInline.add(skill3button);

        rowInline2.add(lvl60notAwake);
        rowInline2.add(lvl60awake);

        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);


        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup createArtButtons(String name, String userId) {
        InlineKeyboardButton skill1button = new InlineKeyboardButton().setText("1 лвл").setCallbackData("getArtFirstLVLfrombot:" + name + ":" + userId);
        InlineKeyboardButton skill2button = new InlineKeyboardButton().setText("30 лвл").setCallbackData("getArtSecondLVLfrombot:" + name + ":" + userId);


        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        rowInline.add(skill1button);
        rowInline.add(skill2button);

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup createHeroButtons(List<String> names, String userId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        if (names.size() <= 2) {
            for (String name : names) {
                rowInline.add(new InlineKeyboardButton().setText(name).setCallbackData("getHeroNamefrombot:" + name + ":" + userId));
            }
            rowsInline.add(rowInline);
        } else {
            for (int i = 0; i < names.size(); i++) {
                if (i < 2) {
                    rowInline.add(new InlineKeyboardButton().setText(names.get(i)).setCallbackData("getHeroNamefrombot:" + names.get(i) + ":" + userId));
                } else {
                    rowInline2.add(new InlineKeyboardButton().setText(names.get(i)).setCallbackData("getHeroNamefrombot:" + names.get(i) + ":" + userId));
                }
            }
            rowsInline.add(rowInline);
            rowsInline.add(rowInline2);
        }

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup createPollButtons(String name, String userId) {
        InlineKeyboardButton skill1button = new InlineKeyboardButton().setText("Normal pull").setCallbackData("getNextNormalPoll:" + name + ":" + userId);
        InlineKeyboardButton skill2button = new InlineKeyboardButton().setText("ML pull").setCallbackData("getNextMLPoll:" + name + ":" + userId);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        rowInline.add(skill1button);
        rowInline.add(skill2button);

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }


    public InlineKeyboardMarkup createNewButtons(@NotNull List<String> text, @NotNull List<String> callBackText, int buttonsInRow) {
        if (text.size() != callBackText.size()) {
            throw new BotAllException("Списки кнопок и ответов не равны");
        }
        if (buttonsInRow == 0) {
            throw new BotAllException("Не может быть 0 кнопок в ряду");
        }

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        int call = 0;
        for (String data : text) {
            rowInline.add(new InlineKeyboardButton().setText(data).setCallbackData(callBackText.get(call)));
            call++;
        }

        int buttonCounter = 1;
        int buttonCounterInList = 0;
        int lastButtonInList = 0;
        boolean check;
        while (buttonCounterInList != call + 1) {
            check = true;
            if (buttonCounter == buttonsInRow - 1) {
                if (buttonCounterInList == buttonCounter) {
                    rowsInline.add(rowInline.subList(lastButtonInList, buttonCounterInList));
                    lastButtonInList = buttonCounterInList;
                } else {
                    rowsInline.add(rowInline.subList(lastButtonInList, buttonCounterInList));
                    lastButtonInList = buttonCounterInList;
                }
                buttonCounter = -1;
                check = false;
            }
            buttonCounter++;
            buttonCounterInList++;
            if (buttonCounterInList == call + 1) {
                if (check) {
                    rowsInline.add(rowInline.subList(lastButtonInList, buttonCounterInList - 1));
                }
            }
        }
        return new InlineKeyboardMarkup().setKeyboard(rowsInline);
    }

}
