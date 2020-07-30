package timeBot.Utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class Helper {


    public boolean checkBaseId(int intId) {
        if (intId == 4){
            return true;
        }
        if (intId > 10) {
            if (intId < 40){
                if (intId != 20 && intId != 30){
                    return true;
                }
            }
        }
        return false;
    }
}
