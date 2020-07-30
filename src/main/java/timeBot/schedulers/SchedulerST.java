package timeBot.schedulers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import timeBot.dto.StreamsDTO;
import timeBot.entity.OnlineEntity;
import timeBot.entity.StreamersDataEntity;
import timeBot.mainbot.Bot;
import timeBot.mainbot.BotService;
import timeBot.repository.OnlineCheckRepository;
import timeBot.repository.StreamersDataRepository;
import timeBot.twitchapi.GetStream;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class SchedulerST implements Runnable {

    @Value("${scheduler.stream.work}")
    private boolean work;

    @Value("${scheduler.stream.cron}")
    private String cron;

    private ScheduledFuture scheduledFuture;
    private TaskScheduler taskScheduler;
    private final GetStream getStream;
    private final BotService botService;
    private final StreamersDataRepository repository;
    private final OnlineCheckRepository onlineRepository;


    private void reSchedule(String cronExpressionStr) {
        if (taskScheduler == null) {
            taskScheduler = new ConcurrentTaskScheduler();
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
        this.scheduledFuture = this.taskScheduler.schedule(this, new CronTrigger(cronExpressionStr));
    }

    @Override
    public void run() {
        //epicseven chat
        if (work) {
            List<StreamersDataEntity> dstreamers = repository.getAll();
            for (StreamersDataEntity data : dstreamers) {
                try {
                    StreamsDTO streamsDTO = getStream.getStream(data.getCode());
                    OnlineEntity onlineEntity = onlineRepository.getRow(data.getCode());
                    if (streamsDTO != null) {
                        if (onlineEntity.getOnoff() == 0) {
                            botService.sendMessageBase(true, false, Long.parseLong("-1001425195630"),null, "Стрим начался. Game - " + streamsDTO.getGameName() + "\n" +
                                    " <a href=\"" + streamsDTO.getChannelUrl() + "\">" + data.getName() + "</a>");
                            onlineEntity.setOnoff(1);
                            onlineRepository.save(onlineEntity);
                        }
                    } else {
                        if (onlineEntity.getOnoff() == 1) {
                            onlineEntity.setOnoff(0);
                            onlineRepository.save(onlineEntity);
                        }
                    }
                } catch (Exception e) {
                    //ignored
                }
            }
        }
    }

    @PostConstruct
    public void initializeScheduler() {
        this.reSchedule(cron);
    }

}