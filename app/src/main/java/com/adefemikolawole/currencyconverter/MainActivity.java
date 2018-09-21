package com.adefemikolawole.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {
SpinnerActivity spinnerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//set values for
        Spinner spFrom = (Spinner) findViewById(R.id.spFrom); //get THe spFrom form the acctivity_main layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.spinnerFrom, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrom.setAdapter(adapter);
        spFrom.setOnItemSelectedListener(spinnerActivity);

        Spinner spTo = findViewById(R.id.spTo);
        ArrayAdapter<CharSequence> adapterB = ArrayAdapter.createFromResource(MainActivity.this, R.array.spinnerTo, android.R.layout.simple_spinner_dropdown_item);
    spTo.setAdapter(adapterB);
    spTo.setOnItemSelectedListener(spinnerActivity);

    }

   class SpinnerActivity extends MainActivity implements AdapterView.OnItemSelectedListener{
       @Override
       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           String text;
           text = parent.getItemAtPosition(position).toString();
           Toast.makeText(parent.getContext(), text, Toast.LENGTH_LONG).show();
           System.out.print(text);
       }

       @Override
       public void onNothingSelected(AdapterView<?> parent) {

       }

   }
}
