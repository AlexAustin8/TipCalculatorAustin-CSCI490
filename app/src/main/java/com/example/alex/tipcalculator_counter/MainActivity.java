//Alexander Austin
//CSCI 490: Mobile App Development
//Prof. Briggs

//The purpose of this program is to create a tip calculator according
//to the specifications found here: https://github.com/CSCI-490-MobileAppDevelopment-S2018/TipCalculator-project


package com.example.alex.tipcalculator_counter;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.view.View;
import android.widget.RadioButton;
import java.text.DecimalFormat;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Declaration of variables to be used
    private EditText get_total, get_numPeople, get_customTip;
    private double total, tipPercentage, grandTotal, tip, pricePerPerson;
    private int numPeople;
    private TextView resultText;
    private Button submitButton, resetButton;
    private RadioGroup tip_select;
    private boolean custValSubmitted = false, totalSubmitted = false, numPeopleSubmitted = false;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("people", numPeople);
        savedInstanceState.putDouble("tot", total);
        savedInstanceState.putDouble("tip%", tipPercentage);
        savedInstanceState.putDouble("grandTot", grandTotal);
        savedInstanceState.putDouble("pricePerPerson", pricePerPerson);
        savedInstanceState.putString("result" , String.valueOf(resultText.getText()));
        savedInstanceState.putString("get_totalVal", String.valueOf(get_total.getText()));
        savedInstanceState.putString("get_peopleVal", String.valueOf(get_numPeople.getText()));
        savedInstanceState.putInt("checkedBox", tip_select.getCheckedRadioButtonId());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);

            numPeople = savedInstanceState.getInt("people");
            total = savedInstanceState.getDouble("tot");
            tipPercentage = savedInstanceState.getDouble("tip%");
            grandTotal = savedInstanceState.getDouble("grandTot");
            pricePerPerson = savedInstanceState.getDouble("pricePerPerson");
            resultText.setText(savedInstanceState.getString("result"));
            get_total.setText(savedInstanceState.getString("get_totalVal"));
            get_numPeople.setText(savedInstanceState.getString("get_peopleVal"));
            tip_select.check(savedInstanceState.getInt("checkedBox"));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ensure that all UI elements are properly instantiated and set up
        submitButton = this.findViewById(R.id.submit_button);
        resetButton = this.findViewById(R.id.reset_button);
        resultText = this.findViewById(R.id.result_text);
        get_total = this.findViewById(R.id.total_prompt);
        get_numPeople = this.findViewById(R.id.num_people_prompt);
        get_customTip = this.findViewById(R.id.cust_tip_prompt);
        tip_select = this.findViewById(R.id.tip_selection);

        get_total.setOnKeyListener(mKeyListener);
        get_numPeople.setOnKeyListener(mKeyListener);
        get_customTip.setOnKeyListener(mKeyListener);

        submitButton.setOnClickListener(buttonListener);
        resetButton.setOnClickListener(buttonListener);




    }


    //Check for a button press
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit_button:
                    //Ensures Button cannot be pressed if there is insufficient data. Provides an instructive toast message to user
                    if(!totalSubmitted || !numPeopleSubmitted){
                        Toast.makeText(getApplicationContext(), "Please Make sure to Fill out all Fields.", Toast.LENGTH_SHORT).show();
                    }else if(get_customTip.getVisibility() == View.VISIBLE && !custValSubmitted ){
                        Toast.makeText(getApplicationContext(), "You must fill out the custom tip amount to use", Toast.LENGTH_SHORT).show();
                    }else{
                        //Check for valid input values
                        if(tipPercentage <= 0){
                            showErrorAlert("Invalid Tip Value: You must leave a tip, I'm sure you make more money than your waiter so don't be a jerk", 1);
                        }else if(total < 1){
                            showErrorAlert("Invalid Total Value, price must be more than $1", 2);
                        }else if(numPeople < 1){
                            showErrorAlert("Invalid number of people, must be at least 1", 3);
                        }else{
                            //Run calculations and output them in proper formatting
                            tip = total * tipPercentage;
                            grandTotal = total + tip;
                            pricePerPerson = grandTotal / numPeople;

                            DecimalFormat decForm = new DecimalFormat("0.00");

                            String resultString = getResources().getString(R.string.result);
                            resultString = String.format(resultString, decForm.format(total), decForm.format(tip), decForm.format(pricePerPerson));

                            resultText.setText(resultString);

                        }



                    }
                    break;

                case R.id.reset_button:
                    //Reset all values
                    numPeople = 0;
                    total = 0;
                    tipPercentage = 0;
                    grandTotal = 0;
                    pricePerPerson = 0;
                    resultText.setText("");
                    get_total.setText("");
                    get_numPeople.setText("");
                    tip_select.clearCheck();
                    break;



            }
        }

    };

    private void showErrorAlert(String errorMessage, final int fieldId) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNeutralButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                findViewById(fieldId).requestFocus();
                            }
                        }).show();
    }
    //Used to gather input from getText objects
    private OnKeyListener mKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            switch (v.getId()) {
                case R.id.total_prompt:
                    total = Double.valueOf(get_total.getText().toString());
                    totalSubmitted = true;
                    break;

                case R.id.num_people_prompt:
                    numPeople = Integer.valueOf(get_numPeople.getText().toString());
                    numPeopleSubmitted = true;
                    break;

                case R.id.cust_tip_prompt:
                    tipPercentage = Double.valueOf(get_customTip.getText().toString());
                    custValSubmitted = true;
                    break;
            }
            return false;
        }

    };

    //Checks for radio button click and acts accordingly
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.ten_tip:
                if (checked) {
                    tipPercentage = .10;
                    get_customTip.setVisibility(View.INVISIBLE);
                    custValSubmitted = false;
                }
                    break;

            case R.id.fifteen_tip:
                if (checked) {
                    tipPercentage = .15;
                    get_customTip.setVisibility(View.INVISIBLE);
                    custValSubmitted = false;
                }
                    break;

            case R.id.twenty_tip:
                if (checked) {
                    tipPercentage = .20;
                    get_customTip.setVisibility(View.INVISIBLE);
                    custValSubmitted = false;
                }
                    break;

            case R.id.twentyfive_tip:
                if (checked){
                    tipPercentage = .25;
                    get_customTip.setVisibility(View.INVISIBLE);
                    custValSubmitted = false;
                }
                    break;

            case R.id.thirty_tip:
                if (checked){
                    tipPercentage = .30;
                    get_customTip.setVisibility(View.INVISIBLE);
                    custValSubmitted = false;
                }
                    break;

            case R.id.custom_tip:
                if (checked){
                    custValSubmitted = false;
                    get_customTip.setVisibility(View.VISIBLE);
                }
                    break;
        }
    }
}



