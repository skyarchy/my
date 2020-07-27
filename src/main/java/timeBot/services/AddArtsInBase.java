package timeBot.services;


import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import timeBot.entity.AllArtsEntity;
import timeBot.repository.AllArtsDbRepository;

@Service
@AllArgsConstructor
public class AddArtsInBase {

    private final AllArtsDbRepository allArtsDbRepository;
    private final AddHeroesInBase addHeroesInBase;

    public void addArtsInDb() {

        String data = addHeroesInBase.getInfoFromAPI("https://api.epicsevendb.com/artifact");

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) jsonParser.parse(data);
        } catch (ParseException e) {
            //
        }

        JSONArray allArts = (JSONArray) jsonObject.get("results");
        for (int i = 0; i < allArts.size(); i++) {
            JSONObject art = (JSONObject) allArts.get(i);
            if ((allArtsDbRepository.getAllByName((String) art.get("name"))) == null) {
                AllArtsEntity allArtsEntity = new AllArtsEntity();

                allArtsEntity.setName((String) art.get("name"));

                String role = (String) art.get("role");
                if (role.equals(""))
                    role = "any";
                allArtsEntity.setArtClass(role);

                JSONObject image = (JSONObject) art.get("assets");
                allArtsEntity.setImage((String) image.get("image"));

                Long stars = (Long) art.get("rarity");
                allArtsEntity.setStars(stars.intValue());

                String artId = (String) art.get("_id");
                String artInfo = addHeroesInBase.getInfoFromAPI("https://api.epicsevendb.com/artifact/" + artId);

                JSONObject jsonObjectArt = new JSONObject();
                try {
                    jsonObjectArt = (JSONObject) jsonParser.parse(artInfo);
                } catch (ParseException e) {
                    //
                }
                JSONArray oneArt = (JSONArray) jsonObjectArt.get("results");

                JSONObject fullArt = (JSONObject) oneArt.get(0);
                JSONObject skill = (JSONObject) fullArt.get("skill");

                allArtsEntity.setSkill((String) skill.get("description"));

                JSONArray enh = (JSONArray) skill.get("enhancements");
                if (!enh.isEmpty()) {
                    JSONArray first = (JSONArray) enh.get(0);
                    String first1 = "";
                    String empty = "";
                    for(int a = 0; a<first.size();a++){
                        if (a == 0) {
                            first1 = String.valueOf(first.get(a));
                            first1 = String.valueOf(Double.valueOf(first1) * 100);
                        } else {
                            empty = String.valueOf(first.get(a));
                            empty = String.valueOf(Double.valueOf(empty) * 100);
                            first1 = first1 + ";" + empty;
                        }
                    }
                    allArtsEntity.setMin(first1);
                    JSONArray last = (JSONArray) enh.get(enh.size() - 1);
                    String last1 = String.valueOf(last.get(0));
                    for(int a = 0; a<last.size();a++){
                        if (a == 0) {
                            last1 = String.valueOf(last.get(a));
                            last1 = String.valueOf(Double.valueOf(last1) * 100);
                        } else {
                            empty = String.valueOf(last.get(a));
                            empty = String.valueOf(Double.valueOf(empty) * 100);
                            last1 = last1 + ";" + empty;
                        }
                    }
                    allArtsEntity.setMax(last1);

                    allArtsDbRepository.save(allArtsEntity);
                }
            }
        }
    }
}
