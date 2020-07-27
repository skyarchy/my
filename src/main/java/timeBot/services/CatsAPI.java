package timeBot.services;


import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
@AllArgsConstructor
public class CatsAPI {


    public String getCatPic() {

        String response = "";

        try {
            String urlString = "https://api.thecatapi.com/v1/images/search";
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("x-api-key", "f35671bc-5ebc-4cc7-a571-74795c1a5816");
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode < 299) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String output;
                StringBuffer data = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    data.append(output);
                }
                response = data.toString();
                in.close();
            }

        } catch (Exception e) {
            //
        }
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = (JSONArray) jsonParser.parse(response);
        } catch (Exception e) {
            //
        }
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        return (String) jsonObject.get("url");
    }


}
