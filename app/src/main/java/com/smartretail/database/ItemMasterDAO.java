package com.smartretail.database;

import com.smartretail.master.ItemMasterActivity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ItemMasterDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(ItemMasterTable data);

    @Query("SELECT * from tbItemMast WHERE strDesc= :value Limit 1")
    List<ItemMasterTable> getItemNameByValue(String value);

    @Query("SELECT * from tbItemMast WHERE intCode= :value Limit 1")
    List<ItemMasterTable> getItemCodeByValue(int value);

    @Query("Select * FROM tbItemMast")
    List<ItemMasterTable> getAllItems();

    @Query("Select * FROM tbItemMast Order BY strDesc ASC")
    List<ItemMasterTable> getAllItemsBYName();

    @Query("Select intCode FROM tbItemMast")
    List<Integer> getAllIntCodes();

    @Query("Select strDesc from tbItemMast where intCode = :value Limit 1")
    String itemName(int value);

    @Query("Select amount from tbItemMast where intCode = :value Limit 1")
    double getAmount(int value);


    @Query("Select intCode from tbItemMast where strDesc = :value Limit 1")
    int id(String value);

    @Query("Update tbItemMast set amount = :amount where intCode = :value")
    void updateAmount(double amount,int value);

    @Update(onConflict = OnConflictStrategy.FAIL)
    void update(ItemMasterTable itemMasterTable);

    @Delete
    void delete(ItemMasterTable itemMasterTable);

    @Query("SELECT * from tbItemMast WHERE intGrpCode = :value Limit 1")
    List<ItemMasterTable> getGroupCodeId(int value);

    @Query("SELECT * from tbItemMast WHERE intUnitCode = :value Limit 1")
    List<ItemMasterTable> getUnitCodeId(int value);

    @Query("SELECT intUnitCode from tbItemMast WHERE intCode = :value Limit 1")
    int getUnitCode(int value);


}
