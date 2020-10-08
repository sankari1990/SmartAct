package com.smartretail.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SalesTableDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(SalesTable data);

}
