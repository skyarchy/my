package timeBot.schedulers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import timeBot.Utils.RandomPoll;
import timeBot.entity.SystemTable;
import timeBot.repository.SystemTableRepository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class AllDayAwarness implements Runnable {

    @Value("${scheduler.alldayawa.work}")
    private boolean work;

    @Value("${scheduler.alldayawa.cron}")
    private String cron;

    private ScheduledFuture scheduledFuture;
    private TaskScheduler taskScheduler;
    private final SystemTableRepository systemTableRepository;
    private final RandomPoll randomPoll;


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
            String mamama = new Date().toString();
            SystemTable systemTable = systemTableRepository.getAllByCode("awCode");
            if ("06".equals(mamama.substring(11, 13))) {
                if (systemTable.getOnoff() == 0) {
                    randomPoll.getHeroForDayAwarness();
                    systemTable.setOnoff(1);
                    systemTableRepository.save(systemTable);
                }
            } else {
                if (systemTable.getOnoff() == 1) {
                    systemTable.setOnoff(0);
                    systemTableRepository.save(systemTable);
                }
            }
        }
    }

    @PostConstruct
    public void initializeScheduler() {
        this.reSchedule(cron);
    }

}