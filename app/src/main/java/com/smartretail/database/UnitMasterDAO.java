package com.smartretail.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UnitMasterDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(UnitMasterTable data);

    @Query("SELECT * from tbUnitMast WHERE strDesc= :value Limit 1")
    List<UnitMasterTable> getItemByValue(String value);

    @Query("Select strDesc FROM tbUnitMast")
    List<String> getAllUnitNames();

    @Query("Select intCode from tbUnitMast where strDesc = :value Limit 1")
    int getId(String value);

    @Query("Select strDesc FROM tbUnitMast where intCode = :intCode Limit 1")
    String getUnitName(int intCode);

    @Query("update tbUnitMast SET strDesc = :modifiedValue WHERE strDesc = :originalValue")
    int update(String originalValue, String modifiedValue);

    @Query("DELETE FROM tbUnitMast WHERE strDesc = :value")
    abstract int deleteByValue(String value);


}
