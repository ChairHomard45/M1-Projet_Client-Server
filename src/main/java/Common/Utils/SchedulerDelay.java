package Common.Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public final class SchedulerDelay {
    public static void sheduleDaily(String nameTask, Timer timer, TimerTask task, int targetHour){
        long delay = calculateInitialDelay(targetHour);
        long period = 24 * 60 * 60 * 1000L;

        timer.scheduleAtFixedRate(task, delay, period);
        System.out.println("Scheduled "+ nameTask +"() tous les jours pour " + targetHour + ":00");
    }

    public static long calculateInitialDelay(int targetHour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstRun = now.withHour(targetHour).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(firstRun)) {
            firstRun = firstRun.plusDays(1);
        }
        return Duration.between(now, firstRun).toMillis();
    }

}
