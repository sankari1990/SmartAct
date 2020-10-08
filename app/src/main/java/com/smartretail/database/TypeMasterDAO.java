package com.smartretail.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TypeMasterDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(TypeMasterTable data);

    @Query("SELECT * from tbsaletypemast WHERE strDesc= :value Limit 1")
    List<PaymentMasterTable> getItemByValue(String value);

    @Query("Select strDesc FROM tbsaletypemast")
    List<String> getAllPayNames();

    @Query("Select intCode from tbsaletypemast where strDesc = :value Limit 1")
    int id(String value);

    @Query("update tbsaletypemast SET strDesc = :modifiedValue WHERE strDesc = :originalValue")
    int update(String originalValue, String modifiedValue);

    @Query("DELETE FROM tbsaletypemast WHERE strDesc = :value")
    abstract int deleteByValue(String value);


}
