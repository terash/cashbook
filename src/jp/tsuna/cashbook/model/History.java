package jp.tsuna.cashbook.model;

import android.provider.BaseColumns;

public interface History extends BaseColumns {
    
    // テーブル名
    public static final String HISTORY_TABLE_NAME = "history";
    
    // 記録日
    public static final String RECORD_DATE = "record_date";
    
    // 入出区分
    public static final String IN_OUT_TYPE = "in_out_type";
    
    // 項目
    public static final String TYPE = "type";
    
    // 備考
    public static final String REMARK = "remark";
    
    // 金額
    public static final String AMOUNT = "amount";

}
