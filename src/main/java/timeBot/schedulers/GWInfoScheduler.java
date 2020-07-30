package timeBot.schedulers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import timeBot.dto.StreamsDTO;
import timeBot.entity.GWInfoEntity;
import timeBot.entity.OnlineEntity;
import timeBot.entity.StreamersDataEntity;
import timeBot.mainbot.BotService;
import timeBot.repository.GWInfoRepository;
import timeBot.repository.OnlineCheckRepository;
import timeBot.repository.StreamersDataRepository;
import timeBot.twitchapi.GetStream;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class GWInfoScheduler implements Runnable {

    @Value("${scheduler.gwinfo.work}")
    private boolean work;

    @Value("${scheduler.gwinfo.cron}")
    private String cron;

    private ScheduledFuture scheduledFuture;
    private TaskScheduler taskScheduler;
    private final GWInfoRepository gwInfoRepository;



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
        if (work) {
            List<GWInfoEntity> gwInfoEntities = gwInfoRepository.getAll();
            for(GWInfoEntity data : gwInfoEntities){
                long razn = new Timestamp(System.currentTimeMillis()).getTime() - data.getDate().getTime();
                long waiting = 1440 - razn/(60*1000);
                if (waiting < 0){
                    gwInfoRepository.delete(data);
                }
            }
        }
    }

    @PostConstruct
    public void initializeScheduler() {
        this.reSchedule(cron);
    }

}