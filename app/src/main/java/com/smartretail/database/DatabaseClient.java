package com.smartretail.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
 
    private Context mCtx;
    private static DatabaseClient mInstance;
    
    //our sdk database object
    private SmartRetailDatabase smartRetailDatabase;
 
    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        
        //creating the app database with Room database builder
        //OfflineTasks is the name of the database

        smartRetailDatabase = Room.databaseBuilder(mCtx, SmartRetailDatabase.class, "smartretail").fallbackToDestructiveMigration().build();
    }
 
    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }
 
    public SmartRetailDatabase getAppDatabase() {
        return smartRetailDatabase;
    }
}