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
    Boolean sented = false;

    public Reminder() {
        this.name = "";
        this.unixTimestamp = 0;
        this.store = new Store();
        this.frequency = 0;
    }

    public Reminder(String name, long unixTimestamp, Store store, Integer frequency, Boolean sented) {
        this.name = name;
        this.unixTimestamp = unixTimestamp;
        this.store = store;
        this.frequency = frequency;
        this.sented = sented;
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
    public String toDateTimeString() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), TimeZone.getDefault().toZoneId());
        String hourString = String.valueOf(dateTime.getHour());
        hourString = hourString.length() == 1 ? "0" + hourString : hourString;
        String minuteString = String.valueOf(dateTime.getMinute());
        minuteString = minuteString.length() == 1 ? "0" + minuteString : minuteString;
        return String.format("%s:%s\n%s %d, %d", hourString, minuteString, dateTime.getMonth(),
                dateTime.getDayOfMonth(), dateTime.getYear());
    }

    public Store getStore() {
        return this.store;
    }

    public Integer getFrequency() {
        return this.frequency;
    }

    public Boolean getSented() { return this.sented;}

    /*
    * @param  arriveTime - number of minutes to walk to the store, calculated via Google Maps API
    * @param  queueTime  - number of minutes of the queue time
    * @return            - a Boolean representing whether the user should set out now
    */
    public Boolean shouldSentOut(Integer arriveTime,Integer queueTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), TimeZone.getDefault().toZoneId());
        if(LocalDateTime.now().isAfter(dateTime)) return false;
        return Duration.between(LocalDateTime.now(), dateTime).toMinutes() <= arriveTime + queueTime;
    }
}
