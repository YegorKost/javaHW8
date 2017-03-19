package com.yegor.cachedNews;

import com.yegor.news.ReadNews;
import com.yegor.outputNews.OutputNews;
import com.yegor.timing.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YegorKost on 15.03.2017.
 */
@Component
public class CachedNewsImpl implements CachedNews {

    private final ReadNews readNews;
    private final TimeStamp timeStamp;
    private final OutputNews outputNews;

    private List<String> listOfNews = new ArrayList<>();
    private List<String> allReadNews = new ArrayList<>();
    private int batchForOutput;
    private int batchForUpdate;

    @Autowired
    public CachedNewsImpl(ReadNews readNews, TimeStamp timeStamp, OutputNews outputNews) {
        this.readNews = readNews;
        this.timeStamp = timeStamp;
        this.outputNews = outputNews;
    }

    public void setBatchForOutput(int batchForOutput) {
        this.batchForOutput = batchForOutput;
    }

    public void setBatchForUpdate(int batchForUpdate) {
        this.batchForUpdate = batchForUpdate;
    }

    @Override
    public void cache() {
        List<String> news = readNews.readNews();
        if (news != null && news.size() > 0){
            if (batchForUpdate > news.size()) {
                batchForUpdate = news.size();
            }
            for (int n = 0; n < batchForUpdate; n++) {
                String c = news.get(n);
                if (!allReadNews.contains(c)) {
                    allReadNews.add(c);
                    c += " (" + timeStamp.getStamp() + ")";
                    listOfNews.add(c);
                    if (listOfNews.size() >= batchForOutput) {
                        if (outputNews.output(listOfNews)) {
                            listOfNews.clear();
                        }
                    }
                }
            }
        } else {
            System.out.println("The reading list of news is empty!");
        }
    }
}
