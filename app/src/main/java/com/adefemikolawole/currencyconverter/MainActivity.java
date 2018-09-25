package com.adefemikolawole.currencyconverter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertPathBuilderSpi;

import static com.adefemikolawole.currencyconverter.R.id.spFrom;
import static com.adefemikolawole.currencyconverter.R.id.txtMoneyValue;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    double finalConvertedValue;
    BigDecimal bd;
    double roundedConvertedValue;
    int itemIndexFrom;
    int itemIndexTo;
    String itemFrom;
    String itemTo;
    int switchValue=0;
    String text;
    Spinner spFrom;
    Spinner spTo;
    String user_input  ="";
   Button btConvert;
   Button btAbout;
   TextView tvResult;
    double userDouble;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    System.out.println("TAG = " + TAG);
        Log.d(TAG, "checking...");
        //Log.d(TAG, user_input);
//set values for spinnerFrom
      spFrom = (Spinner) findViewById(R.id.spFrom); //get THe spFrom form the activity_main layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.spinnerFrom, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFrom.setAdapter(adapter);
        spFrom.setOnItemSelectedListener(MainActivity.this);


//set values for spinnerTo
         spTo = findViewById(R.id.spTo);
        ArrayAdapter<CharSequence> adapterB = ArrayAdapter.createFromResource(MainActivity.this, R.array.spinnerTo, android.R.layout.simple_spinner_item);
        adapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTo.setAdapter(adapterB);
    spTo.setOnItemSelectedListener(MainActivity.this);


    // set activity for button Convert

      //  convertValue();
btConvert = (Button) findViewById(R.id.btConvert);
btConvert.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getUserInput();
         convertValue();
        setTvResult();

    }
});


//textview Result
        tvResult =  (TextView) findViewById(R.id.tvResult);

btAbout = (Button) findViewById(R.id.btAbout);
showAboutMessage();

    }
    public void showAboutMessage(){
        btAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Author: Adefemi Kolawole\n  ITEC 4550 :: Fall 2018",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;
        //getUserInput();

        if(spin.getId() == R.id.spFrom)
        {
            itemIndexFrom = (int) id;
            itemFrom = parent.getItemAtPosition(position).toString();

            //Toast.makeText(MainActivity.this, "Base Curr:"+ "parent:"+parent+ "Position:"+parent+ "id:"+id + "view:"+view,Toast.LENGTH_SHORT).show();
           // Log.d(TAG, "Base Curr||Plain: "+ "parent: "+parent+ "Position: "+position+ "id:"+id + "view:"+view);
            //Log.d(TAG, "Base; Curr||Formatted:"+ "parent:"+parent.toString()+ "Position:"+String.valueOf(position)+ "id:"+String.valueOf(id) + "view:"+view.toString());
            //Log.d(TAG, "spFrom || id/itemIndexFrom : "+ itemIndexFrom  + " item: " + itemFrom + ", spFrom.getId(): "+ spFrom.getId());


        }

        if(spin2.getId() == R.id.spTo)
        {
            itemIndexTo = (int) id;
            itemTo = parent.getItemAtPosition(position).toString();

            //Toast.makeText(this, "Target Curr:" ,Toast.LENGTH_SHORT).show();
           // Log.d(TAG, "spTo || id/itemIndexTo: "+ itemIndexTo  + " item: " + itemTo + ", spTo.getId(): "+ spTo.getId());
           // Log.d(TAG, "spTo.getId(): "+spTo.getId() );
            //Log.d(parent.getContext().toString(), String.valueOf(position));
        }
        Log.d(TAG, "user_input:"+ user_input +" spFrom || id/itemIndexFrom : "+ itemIndexFrom  + " item: " + itemFrom + ", spFrom.getId(): "+ spFrom.getId() + "::spTo || id/itemIndexTo : "+ itemIndexTo  + " item: " + itemTo + ", spTo.getId(): "+ spTo.getId());



        setSwitchChecker();

   Log.d(TAG, "switchValue:" + switchValue);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){


        Toast.makeText(MainActivity.this, "Choose [Base] & [Target] currency", Toast.LENGTH_SHORT).show();

    }

public void setSwitchChecker(){
    //check for From Euro toTheOtherFIveCurrencies
    if ((itemIndexFrom == 0) && (itemIndexTo == 0)){
        switchValue = 0;
    }
    //check for From Euro toTheOtherFIveCurrencies
        if ((itemIndexFrom == 1) && (itemIndexTo == 1)){
            switchValue = 1;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 2)){
            switchValue = 2;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 3)){
            switchValue = 3;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 4)){
            switchValue = 4;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 5)){
            switchValue = 5;
        }
            //check for From usd toTheOtherFIveCurrencies
            if ((itemIndexFrom == 2) && (itemIndexTo == 1)){
                switchValue = 6;
            }
            if ((itemIndexFrom == 2) && (itemIndexTo == 2)){
                switchValue = 7;
            }
            if ((itemIndexFrom == 2) && (itemIndexTo == 3)){
                switchValue = 8;
            }
            if ((itemIndexFrom == 2) && (itemIndexTo == 4)){
                switchValue = 9;
            }
            if ((itemIndexFrom == 2) && (itemIndexTo == 5)){
                switchValue = 10;
            }

                //check for From Gbp toTheOtherFIveCurrencies
                if ((itemIndexFrom == 3) && (itemIndexTo == 1)){
                    switchValue = 11;
                }
                if ((itemIndexFrom == 3) && (itemIndexTo == 2)){
                    switchValue = 12;
                }
                if ((itemIndexFrom == 3) && (itemIndexTo == 3)){
                    switchValue = 13;
                }
                if ((itemIndexFrom == 3) && (itemIndexTo == 4)){
                    switchValue = 14;
                }
                if ((itemIndexFrom == 3) && (itemIndexTo == 5)){
                    switchValue = 15;
                }

                        //check for From Cad toTheOtherFIveCurrencies
                        if ((itemIndexFrom == 4) && (itemIndexTo == 1)){
                            switchValue = 16;
                        }
                        if ((itemIndexFrom == 4) && (itemIndexTo == 2)){
                            switchValue = 17;
                        }
                        if ((itemIndexFrom == 4) && (itemIndexTo == 3)){
                            switchValue = 18;
                        }
                        if ((itemIndexFrom == 4) && (itemIndexTo == 4)){
                            switchValue = 19;
                        }
                        if ((itemIndexFrom == 4) && (itemIndexTo == 5)){
                            switchValue = 20;
                        }

                                //check for From Zar toTheOtherFIveCurrencies
                                if ((itemIndexFrom == 5) && (itemIndexTo == 1)){
                                    switchValue = 21;
                                }
                                if ((itemIndexFrom == 5) && (itemIndexTo == 2)){
                                    switchValue = 22;
                                }
                                if ((itemIndexFrom == 5) && (itemIndexTo == 3)){
                                    switchValue = 23;
                                }
                                if ((itemIndexFrom == 5) && (itemIndexTo == 4)){
                                    switchValue = 24;
                                }
                                if ((itemIndexFrom == 5) && (itemIndexTo == 5)){
                                    switchValue = 25;
                                }
}


   public  void getUserInput(){
       EditText txtMoneyValue = (EditText) findViewById(R.id.txtMoneyValue);
       user_input = txtMoneyValue.getText().toString();
       if (user_input.equals( " ") ){
           //tvResult.setText(user_input);
           Toast.makeText(MainActivity.this, "No value to convert\n input must be greater than 0", Toast.LENGTH_LONG);
           // Toast.makeText(MainActivity.this, "Please enter a value greater than 0 for conversion", Toast.LENGTH_LONG);
       }
else {
           //convertValue();
           try {
               Double.parseDouble(user_input);
               user_input = user_input.toString();

           } catch (Exception x) {
               Log.d(TAG, "Check your input!!!\nMake sure your input is right!!! ");
              Toast.makeText(MainActivity.this, "Check your input!!!\nMake sure your input is right!!! ", Toast.LENGTH_LONG).show();
           }
       }

   }

    public void setTvResult(){
       // getUserInput();

        convertValue();
        bd  = new BigDecimal(finalConvertedValue);
        bd = bd.round(new MathContext(4));
        double roundedConvertedValue = bd.doubleValue();
        //tvResult.setText(String.valueOf(usdToGbp));

       //tvResult.setText(user_input);
       //String checker = tvResult.getText().toString();

     //  if (tvResult.getText().toString().equals( " ")  || (!tvResult.getText().toString().equals( String.valueOf(0)) )){
       // if (tvResult.getText().toString().equals( " ") ){
        //if (user_input.equals( " ") ){
           //tvResult.setText(user_input);
          // Toast.makeText(MainActivity.this, "Please enter a value greater than 0 for conversion", Toast.LENGTH_LONG);
      // }

      // else{
           //convertValue();
           tvResult.setText(String.valueOf(roundedConvertedValue));
           Log.d(TAG, String.valueOf(roundedConvertedValue));
           Toast.makeText(MainActivity.this, "Conversion complete.", Toast.LENGTH_LONG);


       }


   public void convertValue(){




        double eurValue = 1.00;
        double usdValue = 1.17384;
        double gbpValue = 0.89578;
       double cadValue = 1.521707;
       double zarValue = 16.861978;
       btConvert = (Button) findViewById(R.id.btConvert);

        switch (switchValue){

//check for From Euro toTheOtherFIveCurrencies
            case 0:
                Log.d(TAG,  "case1||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                //finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * eurValue);
                Toast.makeText(MainActivity.this, "Select Currencies!!!", Toast.LENGTH_SHORT).show();
                getUserInput();

                break;
            case 1:
                Log.d(TAG,  "case1||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * eurValue);

                break;
            case 2:
                Log.d(TAG, "case2||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * usdValue);

                break;
            case 3:
                Log.d(TAG, "case3||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * gbpValue);
                break;
            case 4:
                Log.d(TAG, "case4||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * cadValue);
                break;
            case 5:
                Log.d(TAG, "case5||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * zarValue);
                break;
            case 6:
                Log.d(TAG,  "case1||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 7:
                Log.d(TAG, "case2||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 8:
                Log.d(TAG, "case3||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 9:
                Log.d(TAG, "case4||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 10:
                Log.d(TAG, "case5||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 11:
                Log.d(TAG,  "case1||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 12:
                Log.d(TAG, "case2||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 13:
                Log.d(TAG, "case3||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 14:
                Log.d(TAG, "case4||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 15:
                Log.d(TAG, "case5||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 16:
                Log.d(TAG,  "case1||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 17:
                Log.d(TAG, "case2||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 18:
                Log.d(TAG, "case3||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 19:
                Log.d(TAG, "case4||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 20:
                Log.d(TAG, "case5||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 21:
                Log.d(TAG,  "case1||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 22:
                Log.d(TAG, "case2||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 23:
                Log.d(TAG, "case3||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 24:
                Log.d(TAG, "case4||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;
            case 25:
                Log.d(TAG, "case5||switchValue: " +switchValue + ", ItemFrom: "+ itemFrom + ", itemTo:" + itemTo);
                break;


        }




   }



  private class JsonTask extends AsyncTask<String, String, String> {


      @Override
      protected String doInBackground(String... strings) {
          try{
              URL url = new URL("http://data.fixer.io/api/latest?access_key=b70449fc1a6ad33f2940bfdbcf125c41");
              HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
              InputStream inputStream = httpUrlConnection.getInputStream();
              BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
              String line = "";
              while (line != null){
              }
          }
          catch (MalformedJsonException e){
             e.printStackTrace();
             Log.d(TAG, e.toString());

          }
          catch (IOException ioe){
              Log.d(TAG, ioe.toString());

          }

          return null;
      }
  }
}


