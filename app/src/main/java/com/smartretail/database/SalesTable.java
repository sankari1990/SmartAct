package com.smartretail.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbSales",indices = {@Index(value = {"intNo"}, unique = true)})

public class SalesTable {
    @PrimaryKey
    private int key;

    private int intNo;
    private String dtmDt;
    private int intItCode;
    private int sngQty;
    private double sngKgs;
    private double sngRate;
    private double sngAmt;
    private double sngTotal;
    private double sngRoff;
    private boolean blnCancel;
    private int intAcctNo;
    private int intPayCode;
    private int intSaleType;
    private int intRow;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getIntNo() {
        return intNo;
    }

    public void setIntNo(int intNo) {
        this.intNo = intNo;
    }

    public String getDtmDt() {
        return dtmDt;
    }

    public void setDtmDt(String dtmDt) {
        this.dtmDt = dtmDt;
    }

    public int getIntItCode() {
        return intItCode;
    }

    public void setIntItCode(int intItCode) {
        this.intItCode = intItCode;
    }

    public int getSngQty() {
        return sngQty;
    }

    public void setSngQty(int sngQty) {
        this.sngQty = sngQty;
    }

    public double getSngKgs() {
        return sngKgs;
    }

    public void setSngKgs(double sngKgs) {
        this.sngKgs = sngKgs;
    }

    public double getSngRate() {
        return sngRate;
    }

    public void setSngRate(double sngRate) {
        this.sngRate = sngRate;
    }

    public double getSngAmt() {
        return sngAmt;
    }

    public void setSngAmt(double sngAmt) {
        this.sngAmt = sngAmt;
    }

    public double getSngTotal() {
        return sngTotal;
    }

    public void setSngTotal(double sngTotal) {
        this.sngTotal = sngTotal;
    }

    public double getSngRoff() {
        return sngRoff;
    }

    public void setSngRoff(double sngRoff) {
        this.sngRoff = sngRoff;
    }

    public boolean isBlnCancel() {
        return blnCancel;
    }

    public void setBlnCancel(boolean blnCancel) {
        this.blnCancel = blnCancel;
    }

    public int getIntAcctNo() {
        return intAcctNo;
    }

    public void setIntAcctNo(int intAcctNo) {
        this.intAcctNo = intAcctNo;
    }

    public int getIntPayCode() {
        return intPayCode;
    }

    public void setIntPayCode(int intPayCode) {
        this.intPayCode = intPayCode;
    }

    public int getIntSaleType() {
        return intSaleType;
    }

    public void setIntSaleType(int intSaleType) {
        this.intSaleType = intSaleType;
    }

    public int getIntRow() {
        return intRow;
    }

    public void setIntRow(int intRow) {
        this.intRow = intRow;
    }
}
