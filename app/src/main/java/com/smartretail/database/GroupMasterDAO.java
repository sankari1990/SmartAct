package com.smartretail.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface GroupMasterDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(GroupMasterTable data);

    @Query("SELECT * from tbGroupMast WHERE strDesc= :value Limit 1")
    List<GroupMasterTable> getItemByValue(String value);

    @Query("Select strDesc FROM tbGroupMast")
    List<String> getAllGroupNames();

    @Query("Select strDesc FROM tbGroupMast where intCode = :intCode Limit 1")
    String getGroupName(int intCode);

    @Query("Select intCode from tbgroupmast where strDesc = :value Limit 1")
    int getId(String value);

    @Query("update tbGroupMast SET strDesc = :modifiedValue WHERE strDesc = :originalValue")
    int update(String originalValue,String modifiedValue);

    @Query("DELETE FROM tbGroupMast WHERE strDesc = :value")
    abstract int deleteByValue(String value);


}
