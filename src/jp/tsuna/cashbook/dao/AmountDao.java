package jp.tsuna.cashbook.dao;

import android.database.Cursor;
import jp.tsuna.cashbook.helper.CashBookData;

public class AmountDao {

    private static AmountDao amountDao;
    
    private AmountDao() {
    }
    
    public static AmountDao getInstance() {
        if (amountDao == null) {
            amountDao = new AmountDao();
        }
        return amountDao;
            }
    
    
    public Cursor getAmount() {


        return null;
    }
    
    
    
}
