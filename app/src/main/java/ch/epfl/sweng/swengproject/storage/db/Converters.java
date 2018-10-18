package ch.epfl.sweng.swengproject.storage.db;


import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        if(value < 0){
            throw new IllegalArgumentException();
        }
        return new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if(date.getTime() < 0){
            throw new IllegalArgumentException();
        }
        return date.getTime();
    }
}

