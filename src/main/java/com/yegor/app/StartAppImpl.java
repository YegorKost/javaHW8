package com.yegor.app;

import com.yegor.cachedNews.CachedNews;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by YegorKost on 17.03.2017.
 */
public class StartAppImpl {
    private CachedNews cachedNews;
    private int rangeOfUpdateSeconds;
    private long durationOfWorkMinutes;

    @Autowired
    public StartAppImpl(CachedNews cachedNews) {
        this.cachedNews = cachedNews;
    }

    public void setRangeOfUpdateSeconds(int rangeOfUpdateSeconds) {
        this.rangeOfUpdateSeconds = rangeOfUpdateSeconds;
    }

    public void setDurationOfWorkMinutes(long durationOfWorkMinutes) {
        this.durationOfWorkMinutes = durationOfWorkMinutes;
    }

    public void start() {
        long startTime = System.currentTimeMillis();
        while (isStart(startTime)) {
            cachedNews.cache();
            try {
                Thread.sleep(rangeOfUpdateSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isStart(long startTime) {
        return durationOfWorkMinutes <= 0 || (System.currentTimeMillis() - startTime) < durationOfWorkMinutes * 60 * 1000;
    }
}
