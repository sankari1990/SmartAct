package com.smartretail.database;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbPayMast",indices = {@Index(value = {"strDesc"}, unique = true)})
public class PaymentMasterTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int intCode;

    @ColumnInfo(name = "strDesc")
    private String strDesc;

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
}
