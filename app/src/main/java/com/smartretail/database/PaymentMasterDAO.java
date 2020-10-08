package com.smartretail.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PaymentMasterDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(PaymentMasterTable data);

    @Query("SELECT * from tbPayMast WHERE strDesc= :value Limit 1")
    List<PaymentMasterTable> getItemByValue(String value);

    @Query("Select strDesc FROM tbPayMast")
    List<String> getAllPayNames();

    @Query("Select intCode from tbPayMast where strDesc = :value Limit 1")
    int id(String value);

    @Query("update tbPayMast SET strDesc = :modifiedValue WHERE strDesc = :originalValue")
    int update(String originalValue, String modifiedValue);

    @Query("DELETE FROM tbPayMast WHERE strDesc = :value")
    abstract int deleteByValue(String value);


}
