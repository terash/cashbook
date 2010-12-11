package jp.tsuna.cashbook.helper;

import static android.provider.BaseColumns._ID;
import static jp.tsuna.cashbook.model.Amount.AMOUNT_TABLE_NAME;
import static jp.tsuna.cashbook.model.Amount.BALANCE;
import static jp.tsuna.cashbook.model.History.AMOUNT;
import static jp.tsuna.cashbook.model.History.RECORD_DATE;
import static jp.tsuna.cashbook.model.History.HISTORY_TABLE_NAME;
import static jp.tsuna.cashbook.model.History.IN_OUT_TYPE;
import static jp.tsuna.cashbook.model.History.REMARK;
import static jp.tsuna.cashbook.model.History.TYPE;
import static jp.tsuna.cashbook.model.User.USER_NAME;
import static jp.tsuna.cashbook.model.User.USER_TABLE_NAME;
import static jp.tsuna.cashbook.model.MonthlyData.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class CashBookData extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "cashbook.db";
    private static final int DATABASE_VERSION = 5;

    public CashBookData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + AMOUNT_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BALANCE + " INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE " + HISTORY_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                   + RECORD_DATE + " DATE NOT NULL, "
                                   + IN_OUT_TYPE + " INTEGER NOT NULL, "
                                   + TYPE + " TEXT NOT NULL, "
                                   + REMARK + " TEXT NULL, "
                                   + AMOUNT + " INTEGER NOT NULL"
                                   + ")");

        db.execSQL("CREATE TABLE " + MONTHLY_DATA_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + YEAR + " TEXT NOT NULL, "
                + MONTH + " TEXT NOT NULL, "
                + BROUGHT_FORWARD + " INTEGER NOT NULL, "
                + CARRIED_FORWARD + " INTEGER NULL "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE " + AMOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE " + HISTORY_TABLE_NAME);
        
        onCreate(db);
    }

}
