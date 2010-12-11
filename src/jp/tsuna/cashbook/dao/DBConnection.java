package jp.tsuna.cashbook.dao;

import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "Cashbook.db";
    private static final int DATABASE_VERSION = 1;
    private static DBConnection dbConnection;
    public static DBConnection getInstance(Context ctx){
        if(dbConnection == null) dbConnection = new DBConnection(ctx);
        return dbConnection ;
    }
    private DBConnection(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    

}
