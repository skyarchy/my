package timeBot.parser;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@AllArgsConstructor
public class HttpClientNetrika {


    public void test() throws Exception {
        String url = "https://epic7x.com/category/updates/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");

        con.setRequestMethod("GET");

        con.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String xmlR = response.toString();
        String assa = (xmlR.split("<s:Body>"))[1];
        String last = assa.split("</s:Body>")[0];


        int a = 10;
    }

}

