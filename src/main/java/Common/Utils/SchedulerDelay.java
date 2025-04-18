package Common.Utils;

import java.time.Duration;
import java.time.LocalDateTime;

public final class SchedulerDelay {
    public static long calculateInitialDelay(int targetHour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstRun = now.withHour(targetHour).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(firstRun)) {
            firstRun = firstRun.plusDays(1);
        }
        return Duration.between(now, firstRun).toMillis();
    }

}
