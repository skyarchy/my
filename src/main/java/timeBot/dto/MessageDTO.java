package timeBot.dto;

import lombok.Data;
import org.json.simple.JSONObject;

@Data
public class MessageDTO {

    private String skill;
    private String picture;
    private String name;
    private String jsonBdHero;
    private String firstAnswer;
    private JSONObject results;

}
