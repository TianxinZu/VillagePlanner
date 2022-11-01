package com.villageplanner;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class Reminder {
    private String name;
    long unixTimestamp;
    private Store store;
    private Integer frequency;

    public Reminder() {
        this.name = "";
        this.unixTimestamp = 0;
        this.store = new Store();
        this.frequency = 0;
    }

    public Reminder(String name, long unixTimestamp, Store store, Integer frequency) {
        this.name = name;
        this.unixTimestamp = unixTimestamp;
        this.store = store;
        this.frequency = frequency;
    }

    public String getName() {
        return this.name;
    }

    public long getUnixTimestamp() {
        return this.unixTimestamp;
    }

    /*
    * @return  The LocalDateTime member in the below format:
    * Hour:Minute
    * Month Day of Month, Year
    */
    public String getDateTimeString() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTimestamp), TimeZone.getDefault().toZoneId());
        return String.format("%d:%d\n%s %d, %d", dateTime.getHour(), dateTime.getMinute(),
                dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime.getYear());
    }

    public Store getStore() {
        return this.store;
    }

    public Integer getFrequency() {
        return this.frequency;
    }

    /*
    * @param  arriveTime - number of minutes to walk to the store, calculated via Google Maps API
    * @param  queueTime  - number of minutes of the queue time
    * @return            - a Boolean representing whether the user should set out now
    */
    public Boolean shouldSetOut(Integer arriveTime,Integer queueTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTimestamp), TimeZone.getDefault().toZoneId());
        return Duration.between(LocalDateTime.now(), dateTime).toMinutes() <= arriveTime + queueTime;
    }
}
