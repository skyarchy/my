package timeBot.Utils;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import timeBot.entity.AllArtsEntity;
import timeBot.entity.AllHeroesEntity;
import timeBot.repository.AllArtsDbRepository;
import timeBot.repository.AllHeroDbRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class RandomPoll {

    private final Random random = new Random();
    private final AllHeroDbRepository allHeroDbRepository;
    private final AllArtsDbRepository allArtsDbRepository;
    private final BaseMessagesEpic baseMessagesEpic;

    private String allRandomPull() {
        int hero3normal = 430000000;
        int hero4normal = 465000000;
        int art4 = 520000000;
        int hero3ml = 563500000;
        int art5 = 571000000;
        int hero5normal = 583500000;
        int hero4ml = 598500000;
        int hero5ml = 599000000;
        int art3 = 1000000000;

        int hero = randomMath();
        if (hero < hero3normal)
            return "hero3normal";
        if (hero < hero4normal)
            return "hero4normal";
        if (hero < art4)
            return "art4";
        if (hero < hero3ml)
            return "hero3ml";
        if (hero < art5)
            return "art5";
        if (hero < hero5normal)
            return "hero5normal";
        if (hero < hero4ml)
            return "hero4ml";
        if (hero < hero5ml)
            return "hero5ml";
        return "art3";
    }

    private String mlRandomPull() {
        int hero3ml = 800000000;
        int hero4ml = 1000000000;
        int hero5ml = 10000000;

        int hero = randomMath();
        if (hero < hero5ml)
            return "hero5ml";
        if (hero < hero3ml)
            return "hero3ml";
        return "hero4ml";
    }

    private String randomAwaPull() {
        int hero3 = 650000000;
        int hero4 = 1000000000;
        int hero5 = 200000000;

        int hero = randomMath();
        if (hero < hero5)
            return "hero5";
        if (hero < hero3)
            return "hero3";
        return "hero4";
    }


    public void getRandomHero(Message message, CallbackQuery callbackQuery, boolean flag) {
        String choise = "nooo";
        if (flag) {
            choise = "/pullnormal";
        } else {
            String[] data = callbackQuery.getData().split(":");
            if (data[0].contains("getNextNormal"))
                choise = "/pullnormal";
        }

        List<AllHeroesEntity> heroData = new ArrayList<>();
        List<AllArtsEntity> artData = new ArrayList<>();

        String group;
        if (choise.contains("/pullnormal")) {
            group = allRandomPull();
        } else {
            group = mlRandomPull();
        }

        switch (group) {
            case ("art3"):
                artData = allArtsDbRepository.getAllByStars(3);
                break;
            case ("art4"):
                artData = allArtsDbRepository.getAllByStars(4);
                break;
            case ("art5"):
                artData = allArtsDbRepository.getAllByStars(5);
                break;
            case ("hero3normal"):
                heroData = allHeroDbRepository.getAllByStars(3);
                break;
            case ("hero4normal"):
                heroData = allHeroDbRepository.getAllByStars(4);
                break;
            case ("hero5normal"):
                heroData = allHeroDbRepository.getAllByStars(5);
                break;
            case ("hero3ml"):
                heroData = allHeroDbRepository.getAllByStars(3);
                break;
            case ("hero4ml"):
                heroData = allHeroDbRepository.getAllByStars(4);
                break;
            case ("hero5ml"):
                heroData = allHeroDbRepository.getAllByStars(5);
                break;
            default:
                Exception exception;
                break;
        }


        int i = 0;
        boolean work = true;
        if (choise.contains("/pullnormal")) {
            if (group.contains("art")) {
                i = random.nextInt(artData.size());
                baseMessagesEpic.getPollArt(artData.get(i).getName(), message, callbackQuery, flag);
            } else {
                while (work) {
                    i = random.nextInt(heroData.size());
                    if (!exceptions(heroData.get(i).getName())) {
                        work = false;
                    }
                }
                baseMessagesEpic.getPollHero(heroData.get(i).getName(), message, callbackQuery, flag);
            }
        } else {
            List<AllHeroesEntity> mlList = new ArrayList<>();
            for (AllHeroesEntity allHeroesEntity : heroData) {
                if (allHeroesEntity.getColor().equals("light") || allHeroesEntity.getColor().equals("dark")) {
                    mlList.add(allHeroesEntity);
                }
            }
            while (work) {
                i = random.nextInt(mlList.size());
                if (!exceptions(mlList.get(i).getName())) {
                    work = false;
                }
            }
            baseMessagesEpic.getPollHero(mlList.get(i).getName(), message, callbackQuery, flag);
        }
    }

    private boolean exceptions(String name) {
        List<String> names = new ArrayList<>();
        names.add("Adventurer Ras");
        names.add("Angelic Montmorancy");
        names.add("Bask");
        names.add("Captain Rikoris");
        names.add("Chaos Inquisitor");
        names.add("Chaos Sect Axe");
        names.add("Commander Lorina");
        names.add("Falconer Kluri");
        names.add("Kikirat v2");
        names.add("Mascot Hazel");
        names.add("Researcher Carrot");
        names.add("Righteous Thief Roozid");
        names.add("All-Rounder Wanda");
        for (String data : names) {
            if (data.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void getHeroForDayAwarness() {

        List<AllHeroesEntity> heroData = new ArrayList<>();

        String group = randomAwaPull();

        switch (group) {
            case ("hero3"):
                heroData = allHeroDbRepository.getAllByStars(3);
                break;
            case ("hero4"):
                heroData = allHeroDbRepository.getAllByStars(4);
                break;
            case ("hero5"):
                heroData = allHeroDbRepository.getAllByStars(5);
                break;
        }
        int i = random.nextInt(heroData.size());
        baseMessagesEpic.getPollHeroForAwarness(heroData.get(i).getName());
    }


    private int randomMath() {
        return random.nextInt(1000000000) + 1;
    }
}
