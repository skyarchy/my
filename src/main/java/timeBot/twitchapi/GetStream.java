package timeBot.twitchapi;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timeBot.dto.StreamsDTO;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class GetStream {
    private static final TypeReference<HashMap<String, Object>> TYPE_REFERENCE = new TypeReference<HashMap<String, Object>>() {
    };
    private final ObjectMapper objectMapper;


    public StreamsDTO getStream(@NotNull String id) throws Exception {

        StreamsDTO returnData = new StreamsDTO();
        String urlString = "https://api.twitch.tv/kraken/streams/" + id;
        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
        con.setRequestProperty("Client-ID", "bbs5puzfoabphnfx4pjhx8h6up5oo0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("Sending get request : " + url);
        System.out.println("Response code : " + responseCode);
        String otao = "";

//        Reading response from input Stream
        if (responseCode < 299) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            otao = response.toString();
            in.close();
        }
        //printing result from response
        System.out.println(otao);
        final Map<String, Object> map = objectMapper.readValue(otao, TYPE_REFERENCE);

        if (map.get("stream") != null) {
            Map<String, Object> streamS = (Map<String, Object>) map.get("stream");
            returnData.setGameName(streamS.get("game").toString());
            returnData.setTime(streamS.get("created_at").toString());
            Map<String, Object> channel = (Map<String, Object>) streamS.get("channel");
            returnData.setChannelUrl(channel.get("url").toString());
            return returnData;

        } else {
            return null;
        }
    }


}

