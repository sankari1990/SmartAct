package com.smartretail.database;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbItemMast",indices = {@Index(value = {"strDesc","intCode"}, unique = true)})
public class ItemMasterTable implements Serializable {
    @ColumnInfo(name = "intCode")
    @PrimaryKey
    private int intCode;

    @ColumnInfo(name = "strDesc")
    private String strDesc;

    private int intGrpCode;

    private int intUnitCode;

    private boolean stock;

    @ColumnInfo(name = "amount")
    private double amount;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public int getIntCode() {
        return intCode;
    }

    public void setIntCode(int intCode) {
        this.intCode = intCode;
    }

    public int getIntGrpCode() {
        return intGrpCode;
    }

    public int getIntUnitCode() {
        return intUnitCode;
    }

    public void setIntGrpCode(int intGrpCode) {
        this.intGrpCode = intGrpCode;
    }

    public void setIntUnitCode(int intUnitCode) {
        this.intUnitCode = intUnitCode;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }
}
