package jp.tsuna.cashbook;

import static android.provider.BaseColumns._ID;
import static jp.tsuna.cashbook.model.Amount.AMOUNT_TABLE_NAME;
import static jp.tsuna.cashbook.model.Amount.BALANCE;
import static jp.tsuna.cashbook.model.History.AMOUNT;
import static jp.tsuna.cashbook.model.History.HISTORY_TABLE_NAME;
import static jp.tsuna.cashbook.model.History.IN_OUT_TYPE;
import static jp.tsuna.cashbook.model.History.RECORD_DATE;
import static jp.tsuna.cashbook.model.History.REMARK;
import static jp.tsuna.cashbook.model.History.TYPE;
import static jp.tsuna.cashbook.model.User.USER_NAME;
import static jp.tsuna.cashbook.model.User.USER_TABLE_NAME;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.tsuna.cashbook.helper.CashBookData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CashBook extends Activity implements android.view.View.OnClickListener {

    private static final String TAG = "CashBook";
    private CashBookData cashbook;
    
    private static String[] USER_DATA = {_ID, USER_NAME, };
    private static String[] AMOUNT_DATA = {_ID, BALANCE, };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        cashbook = new CashBookData(this);

        try { 
            Cursor cursor = getUser();
            // ユーザ登録されていない場合、登録ダイアログを表示する
            if (cursor.getCount() == 0) {
               showUserEntry();
            }
            setContentView(R.layout.main);
            
            // ボタンの初期化
            View entryExpenditureButton = findViewById(R.id.entry_expenditure_button);
            entryExpenditureButton.setOnClickListener(this);
            View entryIncomeButton = findViewById(R.id.entry_income_button);
            entryIncomeButton.setOnClickListener(this);
            View settingButton = findViewById(R.id.setting_button);
            settingButton.setOnClickListener(this);
            View exitButton = findViewById(R.id.exit_button);
            exitButton.setOnClickListener(this);
            View thisMonthTransitionButton = findViewById(R.id.this_month_breakdown_button);
            thisMonthTransitionButton.setOnClickListener(this);
            View thisYearTransitionButton = findViewById(R.id.this_year_transition_button);
            thisYearTransitionButton.setOnClickListener(this);
            View searchMonthDataButton = findViewById(R.id.search_month_data_button);
            searchMonthDataButton.setOnClickListener(this);
            
            // ユーザ名の設定
            TextView userNameView = (TextView)findViewById(R.id.userName);
            cursor.moveToFirst();
            userNameView.setText(cursor.getString(1));
            
            initializeData();
            
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
            
        } finally {
            cashbook.close();
        }
    }

    /**
     * 初期化
     */
    private void initializeData() {
        // 残金
        Cursor cursor = getAmount();
        TextView balanceView = (TextView)findViewById(R.id.thisMonthBalance);
        if (cursor.getCount() == 0) {
            balanceView.setText("0");
        } else {
            cursor.moveToFirst();
            int balance = cursor.getInt(1);
            balanceView.setText(String.valueOf(balance));
            if (balance < 0) {
                balanceView.setTextColor(getResources().getColor(R.color.red));
            }
        }
        
        // 今月の残り日数
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        int lastDay = cal.get(Calendar.DAY_OF_MONTH);
        
        TextView thisMonthLeftView = (TextView)findViewById(R.id.thisMonthLeft);
        thisMonthLeftView.setText(String.valueOf(lastDay - today));
        
    }

    /**
     * 残金を取得
     * @return
     */
    private Cursor getAmount() {
        SQLiteDatabase db = cashbook.getReadableDatabase();
        Cursor cursor = db.query(AMOUNT_TABLE_NAME, AMOUNT_DATA, null, null, null, null, null);
        startManagingCursor(cursor);
        return cursor;
    }

    /**
     * ユーザ情報を取得
     * @return
     */
    private Cursor getUser() {
        SQLiteDatabase db = cashbook.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, USER_DATA, null, null, null, null, null);
        startManagingCursor(cursor);
        return cursor;
    }
    
    /**
     * ユーザ登録画面を表示
     */
    private void showUserEntry() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.user_entry_label));
        alertDialogBuilder.setMessage(getResources().getString(R.string.welcome));
        final EditText editText = new EditText(this);
        alertDialogBuilder.setView(editText);
        alertDialogBuilder.setPositiveButton(
            getResources().getString(R.string.user_entry),
            new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = cashbook.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    String userName = editText.getText().toString();
                    values.put(USER_NAME, userName);
                    db.insertOrThrow(USER_TABLE_NAME, null, values);
                    
                    TextView userNameView = (TextView)findViewById(R.id.userName);
                    userNameView.setText(userName);
                }
            });
        alertDialogBuilder.show();
    }

    /**
     * ボタンが押された時の処理
     */
    public void onClick(View v) {
        
        switch(v.getId()) {
        
        case R.id.entry_expenditure_button:
            showEntryExpenditure();
            break;
        case R.id.entry_income_button:
            showEntryIncome();
            break;
        
        case R.id.setting_button:
            break;
            
        case R.id.exit_button:
            finish();
            break;
        
        case R.id.this_month_breakdown_button:
            break;
            
        case R.id.this_year_transition_button:
            break;
            
        case R.id.search_month_data_button:
            break;
        
        default:
            break;
        
        }
    }


    /**
     * 支出登録ダイアログ表示
     */
    private void showEntryExpenditure() {
        
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View expenditureLayout = layoutInflater.inflate(R.layout.expenditure,
                                 (ViewGroup)findViewById(R.id.entryExpenditure));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(expenditureLayout);
        alertDialogBuilder.setPositiveButton("登録", new OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
                // 残金の更新
                Cursor cursor = getAmount();
                TextView balanceView = (TextView)findViewById(R.id.thisMonthBalance);
                EditText expenditureText = (EditText)expenditureLayout.findViewById(R.id.expenditure);
                int expenditure = Integer.parseInt(expenditureText.getText().toString());
                int balance = 0;
                SQLiteDatabase db = cashbook.getWritableDatabase();
                ContentValues values = new ContentValues();

                if (cursor.getCount() == 0) {
                    balance = balance - expenditure;
                    values.put(BALANCE, balance);
                    db.insertOrThrow(AMOUNT_TABLE_NAME, null, values);

                } else {
                    cursor.moveToFirst();
                    balance = cursor.getInt(1);
                    balance = balance - expenditure;
                    values.put(BALANCE, balance);
                    db.update(AMOUNT_TABLE_NAME, values, null, null);
                }
                balanceView.setText(String.valueOf(balance));
                
                if (balance < 0) {
                    balanceView.setTextColor(getResources().getColor(R.color.red));
                }
                
                // 履歴の登録
                values = new ContentValues();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 
                Date recordDate = new Date();
                int inOutType = 1;
                Spinner expenditureType = (Spinner)expenditureLayout.findViewById(R.id.expenditureType);
                String type = (String)expenditureType.getSelectedItem();
                EditText expenditureNote = (EditText)expenditureLayout.findViewById(R.id.expenditureNote);
                String remark = expenditureNote.getText().toString();
                
                values.put(RECORD_DATE, dateFormat.format(recordDate));
                values.put(IN_OUT_TYPE, inOutType);
                values.put(TYPE, type);
                values.put(REMARK, remark);
                values.put(AMOUNT, expenditure);
                db.insertOrThrow(HISTORY_TABLE_NAME, null, values);
                
                // 月次記録の更新
                updateMonthlyData();
            }

        });
        alertDialogBuilder.setNegativeButton("キャンセル", new OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialogBuilder.show();
    }
    
    private void updateMonthlyData() {
        
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        //TODO 月次記録を検索
        SQLiteDatabase db = cashbook.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        db.insertOrThrow(USER_TABLE_NAME, null, values);
        
    }

    
    /**
     * 収入登録ダイアログ表示
     */
    private void showEntryIncome() {
        
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View incomeLayout = layoutInflater.inflate(R.layout.income,
                                 (ViewGroup)findViewById(R.id.entryIncome));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(incomeLayout);
        alertDialogBuilder.setPositiveButton("登録", new OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
                // 残金の更新
                Cursor cursor = getAmount();
                TextView balanceView = (TextView)findViewById(R.id.thisMonthBalance);
                EditText incomeText = (EditText)incomeLayout.findViewById(R.id.income);
                int income = Integer.parseInt(incomeText.getText().toString());
                int balance = 0;
                SQLiteDatabase db = cashbook.getWritableDatabase();
                ContentValues values = new ContentValues();

                if (cursor.getCount() == 0) {
                    balance = balance + income;
                    values.put(BALANCE, balance);
                    db.insertOrThrow(AMOUNT_TABLE_NAME, null, values);

                } else {
                    cursor.moveToFirst();
                    balance = cursor.getInt(1) + income;
                    values.put(BALANCE, balance);
                    db.update(AMOUNT_TABLE_NAME, values, null, null);
                }
                balanceView.setText(String.valueOf(balance));
                
                if (balance > 0) {
                    balanceView.setTextColor(getResources().getColor(R.color.foreground));
                }
                
                // 履歴の登録
                values = new ContentValues();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 
                Date recordDate = new Date();
                int inOutType = 0;
                Spinner incomeType = (Spinner)incomeLayout.findViewById(R.id.incomeType);
                String type = (String)incomeType.getSelectedItem();
                EditText incomeNote = (EditText)incomeLayout.findViewById(R.id.incomeNote);
                String remark = incomeNote.getText().toString();
                
                values.put(RECORD_DATE, dateFormat.format(recordDate));
                values.put(IN_OUT_TYPE, inOutType);
                values.put(TYPE, type);
                values.put(REMARK, remark);
                values.put(AMOUNT, income);
                db.insertOrThrow(HISTORY_TABLE_NAME, null, values);
            }
        });
        alertDialogBuilder.setNegativeButton("キャンセル", new OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialogBuilder.show();
        
        
    }

    
}