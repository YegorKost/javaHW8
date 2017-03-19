package com.yegor.timing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by YegorKost on 15.03.2017.
 */
@Component
public class TimeStampImpl implements TimeStamp{
    private final DateFormat dateFormat;

    @Autowired
    public TimeStampImpl(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getStamp() {
        return dateFormat.format(new Date());
    }
}

