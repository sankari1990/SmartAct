package com.smartretail.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {GroupMasterTable.class,UnitMasterTable.class,PaymentMasterTable.class,ItemMasterTable.class,TypeMasterTable.class,SalesTable.class}, version = 5)
public abstract class SmartRetailDatabase extends RoomDatabase {

    public abstract GroupMasterDAO groupMasterDAO();
    public abstract UnitMasterDAO unitMasterDAO();
    public abstract PaymentMasterDAO paymentMasterDAO();
    public abstract ItemMasterDAO itemMasterDAO();
    public abstract TypeMasterDAO typeMasterDAO();
    public abstract SalesTableDAO salesTableDAO();

}
