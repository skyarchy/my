package timeBot.Utils;


import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.dto.MessageDTO;
import timeBot.entity.AllArtsEntity;
import timeBot.entity.AllHeroesEntity;
import timeBot.mainbot.BotInterface;
import timeBot.repository.AllArtsDbRepository;
import timeBot.repository.AllHeroDbRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BaseMessagesEpic {


    private final AllHeroDbRepository allHeroDbRepository;
    private final AllArtsDbRepository allArtsDbRepository;
    private final BotInterface bot;


    public boolean getHeroMessage(Message message) {
        String lastHeroName = getName(message);

        if (lastHeroName.length() < 3) {
            bot.sendMsg("МАЛА БУКАВ добавЬ ", message.getChat().getId().toString());
            bot.deleteMsg(message.getMessageId(), message.getChat().getId().toString());
            return false;
        }

        List<AllHeroesEntity> listAll = allHeroDbRepository.getAllByNotFullName(lastHeroName);

        if (listAll.size() > 4) {
            bot.sendMsg("Слишком много совподений забахай запрос точнее! плез", message.getChat().getId().toString());
            bot.deleteMsg(message.getMessageId(), message.getChat().getId().toString());
            return false;
        }
        if (listAll.size() != 1 && !listAll.isEmpty()) {
            String massHeroText = "Несколько совподений сделай выбор ->";
            List<String> heroNames = new ArrayList<>();
            for (AllHeroesEntity oneHeroForName : listAll) {
                heroNames.add(oneHeroForName.getName());
            }

            bot.sendMsgWithKeysHeroChoise(message, massHeroText, heroNames);
            return true;
        }
        if (listAll.size() == 1) {
            lastHeroName = listAll.get(0).getName();
        }

        if (allHeroDbRepository.getAllByName(lastHeroName) == null) {
            bot.sendMsg("Что-то пошло не так, нет в базе персонажа с именем: " + lastHeroName, message.getChat().getId().toString());
            bot.deleteMsg(message.getMessageId(), message.getChat().getId().toString());
        } else {
            AllHeroesEntity allHeroesEntity = allHeroDbRepository.getAllByName(lastHeroName);
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setName(allHeroesEntity.getName());
            messageDTO.setPicture(allHeroesEntity.getImage());

            String attribute = allHeroesEntity.getColor();

            if (attribute.equals("fire"))
                attribute = "\uD83D\uDD25";

            if (attribute.equals("ice"))
                attribute = "\uD83D\uDCA7";

            if (attribute.equals("wind"))
                attribute = "\uD83C\uDF33";

            if (attribute.equals("light"))
                attribute = "☀️";

            if (attribute.equals("dark"))
                attribute = "🌑";

            String role = allHeroesEntity.getHeroClass();
            if (role.equals("manauser"))
                role = "supprot";

            String star = "⭐️";
            String rarityStr = StringUtils.repeat(star, allHeroesEntity.getStars());

            String text = ("<i>" + allHeroesEntity.getName() + "</i>" + " " + attribute + " " + role + " " + rarityStr + ". \n");

            bot.sendMsgWithKeysHeroDB(message, text, messageDTO);
        }
        return true;
    }

    public boolean getSkill(CallbackQuery mes) {

        Message message = mes.getMessage();

        String[] newData = mes.getData().split(":");
        String userid = newData[2];

        MessageDTO messageDTO = new MessageDTO();
        AllHeroesEntity allHeroesEntity = allHeroDbRepository.getAllByName(newData[1]);
        if (newData[0].contains("getHeroName")) {
            messageDTO.setName(allHeroesEntity.getName());
            messageDTO.setPicture(allHeroesEntity.getImage());

            String attribute = allHeroesEntity.getColor();

            if (attribute.equals("fire"))
                attribute = "\uD83D\uDD25";

            if (attribute.equals("ice"))
                attribute = "\uD83D\uDCA7";

            if (attribute.equals("wind"))
                attribute = "\uD83C\uDF33";

            if (attribute.equals("light"))
                attribute = "☀️";

            if (attribute.equals("dark"))
                attribute = "🌑";

            String role = allHeroesEntity.getHeroClass();
            if (role.equals("manauser"))
                role = "supprot";

            String star = "⭐️";
            String rarityStr = StringUtils.repeat(star, allHeroesEntity.getStars());

            String text = ("<i>" + allHeroesEntity.getName() + "</i>" + " " + attribute + " " + role + " " + rarityStr + ". \n");

            bot.sendMsgWithKeysHeroDB(message, text, messageDTO);
            bot.deleteMsg(message.getMessageId(), message.getChatId().toString());
            return true;

        }

        if (newData[0].contains("getSkill1")) {
            messageDTO.setSkill(allHeroesEntity.getSkillOne());
        }
        if (newData[0].contains("getSkill2")) {
            messageDTO.setSkill(allHeroesEntity.getSkillTwo());
        }
        if (newData[0].contains("getSkill3")) {
            messageDTO.setSkill(allHeroesEntity.getSkillThree());
        }
        if (newData[0].contains("awakeno")) {
            messageDTO.setSkill(allHeroesEntity.getAwakeNo());
        }
        if (newData[0].contains("awakeyes")) {
            messageDTO.setSkill(allHeroesEntity.getAwakeYes());
        }
        messageDTO.setName(allHeroesEntity.getName());
        bot.editMsgForSkills(message, messageDTO, userid);

        return true;
    }

    public String getName(Message message) {
        String heroName = message.getText();
        String lastHeroName = "";
        if (heroName.contains("/")) {
            if (heroName.contains("/art")) {
                heroName = message.getText().substring(5).toLowerCase();
            } else {
                heroName = message.getText().substring(6).toLowerCase();
            }
            heroName = heroName.toLowerCase();
            heroName = heroName.replaceAll(" ", ":");

            if (heroName.contains(":")) {
                String[] heroNameMass = heroName.split(":");
                if (heroNameMass.length > 1) {
                    for (String hm : heroNameMass) {
                        if (lastHeroName == "") {
                            lastHeroName = hm.substring(0, 1).toUpperCase() + hm.substring(1);
                        } else {
                            lastHeroName = lastHeroName + " " + hm.substring(0, 1).toUpperCase() + hm.substring(1);
                        }
                    }
                }
            } else {
                lastHeroName = heroName.substring(0, 1).toUpperCase() + heroName.substring(1);
            }
        }
        return lastHeroName;
    }


    public void getArtMin(Message message) {
        String name = getName(message);

        if (name.contains("Of")) {
            name = name.replace("Of", "of");
        }

        if (name.contains("The")) {
            name = name.replace("The", "the");
        }

        if (allArtsDbRepository.getAllByName(name) == null) {
            bot.sendMsg("Что-то пошло не так, нет в базе арта с именем: " + name, message.getChat().getId().toString());
            bot.deleteMsg(message.getMessageId(), message.getChat().getId().toString());
        } else {
            AllArtsEntity allArtsEntity = allArtsDbRepository.getAllByName(name);

            String star = "⭐️";
            String rarityStr = StringUtils.repeat(star, allArtsEntity.getStars());

            String text = ("<i>" + allArtsEntity.getName() + "</i>" + " " + allArtsEntity.getArtClass() + " " + rarityStr + ". \n\n");

            String firstDesc = allArtsEntity.getSkill();
            String[] desList = firstDesc.split("\\{\\{variable}}");
            String[] variables = allArtsEntity.getMin().split(";");
            String description = "";

            if (desList.length > 2) {
                for (int b = 0; b < desList.length - 1; b++) {
                    description = description + desList[b] + "<b>" + (variables[b].split("\\.")[0]) + "%" + "</b>";
                }
            } else {
                String variable = allArtsEntity.getMin().split("\\.")[0];
                description = firstDesc.replace("{{variable}}", "<b>" + (variable) + "%" + "</b>");
            }


            text = text + description;

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setName(allArtsEntity.getName());
            messageDTO.setPicture(allArtsEntity.getImage());

            bot.sendMsgWithKeysArtDB(message, text, messageDTO);
        }
    }


    public void getMinMaxArtButton(CallbackQuery mes) {
        Message message = mes.getMessage();

        String[] newData = mes.getData().split(":");
        String userid = newData[2];
        if (userid.equals(mes.getFrom().getId().toString())) {
            MessageDTO messageDTO = new MessageDTO();
            AllArtsEntity allArtsEntity = allArtsDbRepository.getAllByName(newData[1]);
            if (newData[0].contains("getArtFirstLVL")) {
                String firstDesc = allArtsEntity.getSkill();
                String description = "";
                String[] desList = firstDesc.split("\\{\\{variable}}");
                String[] variables = allArtsEntity.getMin().split(";");
                if (desList.length > 2) {
                    for (int b = 0; b < desList.length - 1; b++) {
                        description = description + desList[b] + "<b>" + (variables[b].split("\\.")[0]) + "%" + "</b>";
                    }
                } else {
                    String variable = allArtsEntity.getMin().split("\\.")[0];
                    description = firstDesc.replace("{{variable}}", "<b>" + (variable) + "%" + "</b>");
                }

                messageDTO.setSkill(description);
            }
            if (newData[0].contains("getArtSecondLVL")) {
                String firstDesc = allArtsEntity.getSkill();
                String description = "";
                String[] desList = firstDesc.split("\\{\\{variable}}");
                String[] variables = allArtsEntity.getMax().split(";");
                if (desList.length > 2) {
                    for (int b = 0; b < desList.length - 1; b++) {
                        description = description + desList[b] + "<b>" + (variables[b].split("\\.")[0]) + "%" + "</b>";
                    }
                } else {
                    String variable = allArtsEntity.getMax().split("\\.")[0];
                    description = firstDesc.replace("{{variable}}", "<b>" + (variable) + "%" + "</b>");
                }


                messageDTO.setSkill(description);
            }
            messageDTO.setName(allArtsEntity.getName());
            bot.editMsgForArts(message, messageDTO, userid);

        } else {
            bot.callBackNotice(mes.getId(), "Жмакать кнопки может только тот кто сделал запрос");
        }


    }

    public void getPollArt(String name, Message message, CallbackQuery callbackQuery, boolean flag) {
        AllArtsEntity allArtsEntity = allArtsDbRepository.getAllByName(name);

        String star = "⭐️";
        String rarityStr = StringUtils.repeat(star, allArtsEntity.getStars());

        String userName = "";
        if (flag){
            userName = message.getFrom().getUserName();
        } else {
            userName = callbackQuery.getFrom().getUserName();
        }

        String text = userName + " получил это:" + "\n";

        text = text + (allArtsEntity.getName() + " " + allArtsEntity.getArtClass() + " " + rarityStr);

        if (flag) {
            bot.sendPullMessageFirst(message, text, allArtsEntity.getImage());
        } else {
            bot.sendPullMessageLast(callbackQuery, text, allArtsEntity.getImage());
        }
    }

    public void getPollHero(String name, Message message, CallbackQuery callbackQuery, boolean flag) {
        AllHeroesEntity allHeroesEntity = allHeroDbRepository.getAllByName(name);

        String attribute = allHeroesEntity.getColor();

        if (attribute.equals("fire"))
            attribute = "\uD83D\uDD25";

        if (attribute.equals("ice"))
            attribute = "\uD83D\uDCA7";

        if (attribute.equals("wind"))
            attribute = "\uD83C\uDF33";

        if (attribute.equals("light"))
            attribute = "☀️";

        if (attribute.equals("dark"))
            attribute = "🌑";

        String role = allHeroesEntity.getHeroClass();
        if (role.equals("manauser"))
            role = "supprot";

        String star = "⭐️";
        String rarityStr = StringUtils.repeat(star, allHeroesEntity.getStars());


        String userName = "";
        if (flag){
            userName = message.getFrom().getUserName();
        } else {
            userName = callbackQuery.getFrom().getUserName();
        }

        String text = userName + " получил это:" + "\n";

        text = text + (allHeroesEntity.getName() + " " + attribute + " " + role + " " + rarityStr);

        if (flag) {
            bot.sendPullMessageFirst(message, text, allHeroesEntity.getImage());
        } else {
            bot.sendPullMessageLast(callbackQuery, text, allHeroesEntity.getImage());
        }
    }
}
