package ch.epfl.sweng.swengproject.storage.db;

/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import ch.epfl.sweng.swengproject.MyApplication;

/**
 * Contains the database holder and serves as the main access point for the underlying
 * connection to your app's persisted, relational data.
 */
@Database(entities = {User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase MEMORY_INSTANCE;
    private static AppDatabase HD_INSTANCE;
    public abstract UserDao userDao();

    private static boolean isUnderTest = false;

    /**
     * Setting isUnderTest to true will result that each subsequent call of getInstance() will
     * return a database that is just in memory, so mocked...
     * @param isUnderTest
     */
    public static void setUnderTest(boolean isUnderTest){
        AppDatabase.isUnderTest = isUnderTest;
    }

    /**
     *
     * @return the database instance that interact with the device (hard disk) for this application
     */
    public static AppDatabase getInstance() {
        if(AppDatabase.isUnderTest){
            return getInMemoryInstance();
        }else{
            return getInHDInstance();
        }
    }

    private static AppDatabase getInHDInstance() {
        Context context = MyApplication.getAppContext();
        if (HD_INSTANCE == null) {
            HD_INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"MyDataBase")
                            .build();
        }
        System.out.println("AppDatabase is returning a real database that will persist on disk");
        return HD_INSTANCE;
    }

    private static AppDatabase getInMemoryInstance() {
        Context context = MyApplication.getAppContext();
        if (MEMORY_INSTANCE == null) {
            MEMORY_INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class).build();
        }
        System.out.println("AppDatabase is returning a instance that will stay just in memory for testing purpose");
        return MEMORY_INSTANCE;
    }

    /**
     *
     * free the memory
     */
    public static void destroyInstance() {
        MEMORY_INSTANCE = null;
        HD_INSTANCE = null;
    }

}
