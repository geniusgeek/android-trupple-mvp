/*
 * Copyright (c) 2016.  Lukaround Inc ;This program is free software: you can &#10;
 * redistribute it and/or modify;it under the terms of the Lukaround Inc Public License as &#10;
 * published by Lukaround Software Foundation, either version 3 of the License or (at your option) any later version ;&#10;This program is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; &#10;without even the implied warranty of;MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.&#10;  See www.lukaround.org/developer/licence
 */

package com.github.geniusgeek.trupple_mvp.utils;

/**
 * @architect: Samuel Ekpe(Team Lead)
 * @author: Genius
 * @reviewers: Lukaround Android Team
 * Date: 5/26/16
 * Time: 4:59 AM
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Methods for dealing with timestamps
 */
public final class TimeStampUtils {


    /**
     * Private constructor: class cannot be instantiated
     */
    private TimeStampUtils() {
    }

    /**
     * factory method to return a date, try to make it as immutable as possible
     *
     * @return
     * @throws ParseException
     */
    public static final String getDate() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForCurrentDate() {
        Date now = new Date();
        return getISO8601StringForDate(now);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
}
