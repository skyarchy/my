package timeBot.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timeBot.entity.OnlineEntity;
import timeBot.entity.StreamersDataEntity;
import timeBot.repository.OnlineCheckRepository;
import timeBot.repository.StreamersDataRepository;

@Service
@AllArgsConstructor
public class AddDeleteNewStreamer {

    private final StreamersDataRepository streamersDataRepository;
    private final OnlineCheckRepository onlineCheckRepository;


    public void addStreamer(String data) {
        StreamersDataEntity streamersData = new StreamersDataEntity();
        String[] newData = data.split(";");
        streamersData.setName(newData[0]);
        streamersData.setCode(newData[1]);
        streamersDataRepository.save(streamersData);

        OnlineEntity onlineEntity = new OnlineEntity();
        onlineEntity.setOnoff(0);
        onlineEntity.setCode(newData[1]);
        onlineCheckRepository.save(onlineEntity);


    }

    public void deleteStreamer(String data) {
        String[] newData = data.split(";");
        StreamersDataEntity streamersData = streamersDataRepository.getStreamer(newData[0], newData[1]);
        streamersDataRepository.delete(streamersData);
    }

    public String getStreamer(String name) {
        return streamersDataRepository.getAllByName(name).toString();
    }
}
