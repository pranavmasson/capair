package com.capair.api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.sql.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DateUtils {

    public static LocalDateTime createDateTimeFromDateString(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("uuuu-MM-dd HH:mm:ss")
            .withResolverStyle(ResolverStyle.STRICT);
        LocalDateTime dateTime = null;
        if(StringUtils.hasText(dateString)){
            try{
                dateTime = LocalDateTime.parse(dateString, formatter);
            }
            catch(DateTimeParseException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                dateTime = LocalDateTime.now();
            }
        }
        return dateTime;
    }

    public static Date createDateFromDateString(String dateString){
        Date date = null;
        if(StringUtils.hasText(dateString)){
            try{
                date = Date.valueOf(dateString);
            }
            catch(IllegalArgumentException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        return date;
    }

    public static boolean isCheckinOpen(LocalDateTime date){
        return date.isBefore(LocalDateTime.now().plusDays(1)) && date.isAfter(LocalDateTime.now());
    }
}
