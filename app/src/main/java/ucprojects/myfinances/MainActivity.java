package ucprojects.myfinances;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCDClick(View view) {
        clearAllInput();
        enableAllInput();
        disableInput(R.id.txtPaymentAmt);
    }

    public void onCheckingClick(View view) {
        clearAllInput();
        enableAllInput();
        disableInput(R.id.txtInitialBal);
        disableInput(R.id.txtInterestRate);
        disableInput(R.id.txtPaymentAmt);
    }

    public void onLoanClick(View view) {
        clearAllInput();
        enableAllInput();
    }

    public void onSaveClick(View view) {
        //save data
        boolean isSuccess = false;
        String message = "Data saved successfully";
        String bgColor = "#32cd32";
        boolean alreadyExist = false;
        DataContext db = new DataContext(MainActivity.this);
        try {
            Account acc = getAccount();
            if(isAlreadyExist(acc.getAccountNumber())){
                alreadyExist = true;
                message = "This account number already exists. Please try another account number";
                bgColor = "#ff0000";
            }
            else {
                db.open();
                isSuccess = db.insertAccount(acc);
                db.close();
            }
        }
        catch (Exception ex) {
            isSuccess = false;
        }

        //show success message

        if(!alreadyExist && !isSuccess) {
            message = "Save failed!";
            bgColor = "#ff0000";
        }
        Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        View toastView = toast.getView();
        toastView.setBackgroundColor(Color.parseColor(bgColor));
        toast.show();

        //clear inputs
        if(!alreadyExist && isSuccess) {
            clearAllInput();
        }
    }

    public void onCancelClick(View view) {
        clearAllInput();
        RadioGroup rdGroup = (RadioGroup) findViewById(R.id.grpAccountType);
        rdGroup.clearCheck();
    }

    public void onViewClick(View view) {
        DataContext db = new DataContext(MainActivity.this);
        EditText txtAccNo = (EditText) findViewById(R.id.txtAccNo);
        String accNoText = txtAccNo.getText().toString();
        if(accNoText.trim().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),"Please enter account number.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            toast.show();
            return;
        }
        int accNo = Integer.parseInt(accNoText);
        db.open();
        Cursor res = db.getDataByAccountNo(accNo);
        if(res.getCount() == 0)
        {
            Toast toast = Toast.makeText(getApplicationContext(),"No Data!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            toast.show();
        }
        else {
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()) {
                buffer.append("Account No.: "+res.getString(0)+"\n");
                buffer.append("Account Type.: "+res.getString(1)+"\n");
                buffer.append("Initial Bal: "+res.getString(2)+"\n");
                buffer.append("Current Bal: "+res.getString(3)+"\n");
                buffer.append("Interest Rate: "+res.getString(4)+"\n");
                buffer.append("Payment Amt: "+res.getString(5)+"\n");
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Account Entry");
            builder.setMessage(buffer.toString());
            builder.show();
        }
    }

    private boolean isAlreadyExist(int accNo) {
        DataContext db = new DataContext(MainActivity.this);
        db.open();
        Cursor res = db.getDataByAccountNo(accNo);
        return res.getCount() > 0;
    }

    private Account getAccount() {
        Account acc = new Account();
        RadioGroup rdGroup = (RadioGroup) findViewById(R.id.grpAccountType);
        RadioButton checkedRadio = (RadioButton) findViewById(rdGroup.getCheckedRadioButtonId());
        String accountType = checkedRadio.getText().toString();
        EditText txtPmtAmt = (EditText) findViewById(R.id.txtPaymentAmt);
        EditText txtAccNo = (EditText) findViewById(R.id.txtAccNo);
        EditText txtInitBal = (EditText) findViewById(R.id.txtInitialBal);
        EditText txtCurrentBal = (EditText) findViewById(R.id.txtCurrentBal);
        EditText txtInterestRate = (EditText) findViewById(R.id.txtInterestRate);
        acc.setAccountNumber(Integer.parseInt(txtAccNo.getText().toString()));
        acc.setAccountType(accountType);
        acc.setCurrentBalance(Double.parseDouble(txtCurrentBal.getText().toString()));
        switch (accountType) {
            case "CD":
                acc.setInitialBalance(Double.parseDouble(txtInitBal.getText().toString()));
                acc.setInterestRate(Double.parseDouble(txtInterestRate.getText().toString()));
                break;
            case "Checking":
                break;
            case "Loan":
                acc.setInitialBalance(Double.parseDouble(txtInitBal.getText().toString()));
                acc.setInterestRate(Double.parseDouble(txtInterestRate.getText().toString()));
                acc.setPaymentAmount(Double.parseDouble(txtPmtAmt.getText().toString()));
                break;
        }
        return acc;
    }

    private void clearAllInput() {
        EditText txtPmtAmt = (EditText) findViewById(R.id.txtPaymentAmt);
        EditText txtAccNo = (EditText) findViewById(R.id.txtAccNo);
        EditText txtInitBal = (EditText) findViewById(R.id.txtInitialBal);
        EditText txtCurrentBal = (EditText) findViewById(R.id.txtCurrentBal);
        EditText txtInterestRate = (EditText) findViewById(R.id.txtInterestRate);
        txtPmtAmt.setText("");
        txtInitBal.setText("");
        txtCurrentBal.setText("");
        txtInterestRate.setText("");
        txtAccNo.setText("");
    }

    private void enableAllInput() {
        EditText txtPmtAmt = (EditText) findViewById(R.id.txtPaymentAmt);
        EditText txtInitBal = (EditText) findViewById(R.id.txtInitialBal);
        EditText txtCurrentBal = (EditText) findViewById(R.id.txtCurrentBal);
        EditText txtInterestRate = (EditText) findViewById(R.id.txtInterestRate);
        txtPmtAmt.setEnabled(true);
        txtInitBal.setEnabled(true);
        txtCurrentBal.setEnabled(true);
        txtInterestRate.setEnabled(true);
    }

    private void disableInput(int id) {
        EditText txtInput = (EditText) findViewById(id);
        txtInput.setEnabled(false);
    }
}