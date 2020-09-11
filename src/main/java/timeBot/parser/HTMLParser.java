package timeBot.parser;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
@AllArgsConstructor
public class HTMLParser {

    private final HttpClientNetrika httpClientNetrika;


    public void parse() {
        try {
            httpClientNetrika.test();
        } catch (Exception e){
            // aassaa
        }

        Document doc;
        URL url;
        String htmlData = "";

        try {
            String a = "https://epic7x.com/category/updates";
            url = new URL(a);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                htmlData = inputLine;
            }

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        doc = Jsoup.parse(htmlData);
        String title = doc.title();

        System.out.println("Jsoup Can read HTML page from URL, title : " + title);

    }


}