package timeBot.services;

import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import timeBot.entity.AllHeroesEntity;
import timeBot.repository.AllHeroDbRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@AllArgsConstructor
public class AddHeroesInBase {

    private final AllHeroDbRepository allHeroDbRepository;


    public void addHeroesInDB() {


        String data = getInfoFromAPI("https://api.epicsevendb.com/hero");

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) jsonParser.parse(data);
        } catch (ParseException e) {
            //
        }
        JSONArray allHeroes = (JSONArray) jsonObject.get("results");

        for (int i = 0; i < allHeroes.size(); i++) {
            JSONObject hero = (JSONObject) allHeroes.get(i);
            if ((allHeroDbRepository.getAllByName((String) hero.get("name"))) == null) {
                AllHeroesEntity heroesEntity = new AllHeroesEntity();
                heroesEntity.setName((String) hero.get("name"));
                heroesEntity.setColor((String) hero.get("attribute"));
                heroesEntity.setHeroClass((String) hero.get("role"));

                JSONObject assets = (JSONObject) hero.get("assets");
                heroesEntity.setImage((String) assets.get("image"));

                Long stars = (Long) hero.get("rarity");
                heroesEntity.setStars(stars.intValue());

                String heroId = (String) hero.get("_id");
                String heroInfo = getInfoFromAPI("https://api.epicsevendb.com/hero/" + heroId);

                JSONObject jsonObjectHero = new JSONObject();
                try {
                    jsonObjectHero = (JSONObject) jsonParser.parse(heroInfo);
                } catch (ParseException e) {
                    //
                }
                JSONArray oneHero = (JSONArray) jsonObjectHero.get("results");
                if (oneHero != null && !heroId.equals("ains")) {
                    JSONObject resultsHero = (JSONObject) oneHero.get(0);
                    JSONArray skills = (JSONArray) resultsHero.get("skills");

                    heroesEntity.setSkillOne(getSkillMessage((JSONObject) skills.get(0)));
                    heroesEntity.setSkillTwo(getSkillMessage((JSONObject) skills.get(1)));
                    heroesEntity.setSkillThree(getSkillMessage((JSONObject) skills.get(2)));

                    JSONObject calculatedStatus = (JSONObject) resultsHero.get("calculatedStatus");
                    JSONObject lv60SixStarNoAwaken = (JSONObject) calculatedStatus.get("lv60SixStarNoAwaken");
                    JSONObject lv60SixStarFullyAwakened = (JSONObject) calculatedStatus.get("lv60SixStarFullyAwakened");

                    heroesEntity.setAwakeNo(getAwake(lv60SixStarNoAwaken));
                    heroesEntity.setAwakeYes(getAwake(lv60SixStarFullyAwakened));

                    allHeroDbRepository.save(heroesEntity);
                }
            }
        }
    }

    private String getAwake(JSONObject data) {
        String effect = String.valueOf(data.get("eff"));
        effect = String.valueOf(Double.valueOf(effect) * 100);

        String chc = String.valueOf(data.get("chc"));
        chc = String.valueOf(Double.valueOf(chc) * 100);

        String chd = String.valueOf(data.get("chd"));
        chd = String.valueOf(Double.valueOf(chd) * 100);

        String efr = String.valueOf(data.get("efr"));
        efr = String.valueOf(Double.valueOf(efr) * 100);

        String dac = String.valueOf(data.get("dac"));
        dac = String.valueOf(Double.valueOf(dac) * 100);

        return ("Combat power  - " + data.get("cp") + "\n" +
                "Attack  - " + data.get("atk") + "\n" +
                "Defense  - " + data.get("def") + "\n" +
                "Health  - " + data.get("hp") + "\n" +
                "<b>Speed</b>  - " + data.get("spd") + "\n" +
                "Critical Hit Chance  - " + chc.split("\\.")[0] + "\n" +
                "Critical Hit Damage  - " + chd.split("\\.")[0] + "\n" +
                "Effectiveness  - " + effect.split("\\.")[0] + "\n" +
                "Effect Resistance  - " + efr.split("\\.")[0] + "\n" +
                "Dual Attack Chance  - " + dac.split("\\.")[0] + "\n"
        );
    }

    private String getSkillMessage(JSONObject skill) {
        String description = "";
        JSONArray values = (JSONArray) skill.get("values");
        description = (String) skill.get("description");
        if (!values.isEmpty()) {
            String value = String.valueOf(values.get(0));
            value = String.valueOf(Double.valueOf(value) * 100);
            description = description.replace("{{variable}}", "<b>" + (value) + "%" + "</b>");
        }
        JSONArray enhancements = (JSONArray) skill.get("enhancements");
        String enhancementsStr = "\n";
        for (int i = 0; i < enhancements.size(); i++) {
            JSONObject data = (JSONObject) enhancements.get(i);
            enhancementsStr = enhancementsStr + (i + 1) + " improve: " + data.get("string") + "\n";
        }

        return ("<b>Skill name</b> : " + skill.get("name") + " \n" + description + " Cooldown : " + skill.get("cooldown") + " \n" + enhancementsStr);
    }


    public String getInfoFromAPI(String url) {

        String data = "";
        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String output;
            StringBuffer someData = new StringBuffer();

            while ((output = rd.readLine()) != null) {
                someData.append(output);
            }
            data = someData.toString();
            rd.close();

        } catch (
                Exception e) {

        }
        return data;
    }

}
