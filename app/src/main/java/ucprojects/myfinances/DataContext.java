package ucprojects.myfinances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataContext {
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    public DataContext(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public  boolean insertAccount(Account c) {
        boolean success = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("account_number", c.getAccountNumber());
            initialValues.put("account_type", c.getAccountType());
            initialValues.put("initial_balance", c.getInitialBalance());
            initialValues.put("current_balance", c.getCurrentBalance());
            initialValues.put("interest_rate", c.getInterestRate());
            initialValues.put("payment_amount", c.getPaymentAmount());
            success = database.insert("account", null, initialValues) > 0;
        }
        catch (Exception ex) {

        }
        return success;
    }

    public  boolean updateAccount(Account c) {
        boolean success = false;
        int acc_no = c.getAccountNumber();
        try {
            ContentValues updateValues = new ContentValues();
            updateValues.put("account_number", c.getAccountNumber());
            updateValues.put("account_type", c.getAccountType());
            updateValues.put("initial_balance", c.getInitialBalance());
            updateValues.put("current_balance", c.getCurrentBalance());
            updateValues.put("interest_rate", c.getInterestRate());
            updateValues.put("payment_amount", c.getPaymentAmount());
            success = database.update("account", updateValues, "account_number=" + acc_no, null) > 0;
        }
        catch (Exception ex) {

        }
        return success;
    }

    public Cursor getDataByAccountNo(int accNo) {
        String getQuery = "Select * from account where account_number="+accNo;
        Cursor acc = database.rawQuery(getQuery, null);
        return acc;
    }
}
