package jp.tsuna.cashbook.model;

import android.provider.BaseColumns;

public interface MonthlyData extends BaseColumns {
    
    //テーブル名
    public static final String MONTHLY_DATA_TABLE_NAME = "monthly_data";
    
    // 年
    public static final String YEAR = "year";
    
    // 月
    public static final String MONTH = "month";
    
    // 前月より繰り越し
    public static final String BROUGHT_FORWARD = "brought_forward";
    
    // 次月へ繰り越し
    public static final String CARRIED_FORWARD = "carried_forward";

}
