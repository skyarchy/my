package timeBot.services;


import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import timeBot.repository.StreamersDataRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GoogleApi {

    private final StreamersDataRepository streamersDataRepository;

    public String getRandomPicFromGoogle(String data) {

        String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
        String url = "https://www.google.com/search?site=imghp&tbm=isch&source=hp&q=" + data;

        List<String> resultUrls = new ArrayList<String>();

        try {
            Document doc = Jsoup.connect(url).userAgent(userAgent).referrer("https://www.google.com/").get();

            Elements elements = doc.select("div.rg_meta");

            String[] dattddd = doc.toString().split("\n");
            List<String> ops = new ArrayList<>();
            for (int i = 1800; i < dattddd.length; i++) {
                ops.add(dattddd[i]);
            }
            List<String> newOps = new ArrayList<>();
            for (String dato : ops) {
                if (dato.contains("[https:")) {
                    if (dato.contains(".jpg"))
                        newOps.add(dato);
                }
            }

            JSONObject jsonObject;
            for (Element element : elements) {
                if (element.childNodeSize() > 0) {
                    jsonObject = (JSONObject) new JSONParser().parse(element.childNode(0).toString());
                    resultUrls.add((String) jsonObject.get("ou"));
                }
            }
            System.out.println("number of results: " + resultUrls.size());

            for (String imageUrl : resultUrls) {
                System.out.println(imageUrl);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
