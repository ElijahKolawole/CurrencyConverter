package com.adefemikolawole.currencyconverter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertPathBuilderSpi;
import java.util.ArrayList;
import java.util.HashMap;

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
    int switchValue = 0;
    String text;
    Spinner spFrom;
    Spinner spTo;
    String user_input = "";
    Button btConvert;
    Button btAbout;
    TextView tvResult;
    double userDouble;
    private static final String TAG = MainActivity.class.getSimpleName();
    /*JSOn related items*/
    ArrayList<HashMap<String, String>> rateList;
    HashMap<String, String> rateMap;
    String EUR;
    String USD;
    String GBP;
    String CAD;
    String ZAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rateList = new ArrayList<>();
        new GetCurrencies().execute();



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
        tvResult = (TextView) findViewById(R.id.tvResult);

        btAbout = (Button) findViewById(R.id.btAbout);
        showAboutMessage();

    }

    public void showAboutMessage() {
        btAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Author: Adefemi Kolawole\n  ITEC 4550 :: Fall 2018", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner) parent;
        Spinner spin2 = (Spinner) parent;
        //getUserInput();

        if (spin.getId() == R.id.spFrom) {
            itemIndexFrom = (int) id;
            itemFrom = parent.getItemAtPosition(position).toString();

            //Toast.makeText(MainActivity.this, "Base Curr:"+ "parent:"+parent+ "Position:"+parent+ "id:"+id + "view:"+view,Toast.LENGTH_SHORT).show();
            // Log.d(TAG, "Base Curr||Plain: "+ "parent: "+parent+ "Position: "+position+ "id:"+id + "view:"+view);
            //Log.d(TAG, "Base; Curr||Formatted:"+ "parent:"+parent.toString()+ "Position:"+String.valueOf(position)+ "id:"+String.valueOf(id) + "view:"+view.toString());
            //Log.d(TAG, "spFrom || id/itemIndexFrom : "+ itemIndexFrom  + " item: " + itemFrom + ", spFrom.getId(): "+ spFrom.getId());


        }

        if (spin2.getId() == R.id.spTo) {
            itemIndexTo = (int) id;
            itemTo = parent.getItemAtPosition(position).toString();

            //Toast.makeText(this, "Target Curr:" ,Toast.LENGTH_SHORT).show();
            // Log.d(TAG, "spTo || id/itemIndexTo: "+ itemIndexTo  + " item: " + itemTo + ", spTo.getId(): "+ spTo.getId());
            // Log.d(TAG, "spTo.getId(): "+spTo.getId() );
            //Log.d(parent.getContext().toString(), String.valueOf(position));
        }
        Log.d(TAG, "user_input:" + user_input + " spFrom || id/itemIndexFrom : " + itemIndexFrom + " item: " + itemFrom + ", spFrom.getId(): " + spFrom.getId() + "::spTo || id/itemIndexTo : " + itemIndexTo + " item: " + itemTo + ", spTo.getId(): " + spTo.getId());


        setSwitchChecker();

        Log.d(TAG, "switchValue:" + switchValue);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


        Toast.makeText(MainActivity.this, "Choose [Base] & [Target] currency", Toast.LENGTH_SHORT).show();

    }

    public void setSwitchChecker() {
        //check for From Euro toTheOtherFIveCurrencies
        if ((itemIndexFrom == 0) && (itemIndexTo == 0)) {
            switchValue = 0;
        }
        //check for From Euro toTheOtherFIveCurrencies
        if ((itemIndexFrom == 1) && (itemIndexTo == 1)) {
            switchValue = 1;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 2)) {
            switchValue = 2;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 3)) {
            switchValue = 3;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 4)) {
            switchValue = 4;
        }
        if ((itemIndexFrom == 1) && (itemIndexTo == 5)) {
            switchValue = 5;
        }
        //check for From usd toTheOtherFIveCurrencies
        if ((itemIndexFrom == 2) && (itemIndexTo == 1)) {
            switchValue = 6;
        }
        if ((itemIndexFrom == 2) && (itemIndexTo == 2)) {
            switchValue = 7;
        }
        if ((itemIndexFrom == 2) && (itemIndexTo == 3)) {
            switchValue = 8;
        }
        if ((itemIndexFrom == 2) && (itemIndexTo == 4)) {
            switchValue = 9;
        }
        if ((itemIndexFrom == 2) && (itemIndexTo == 5)) {
            switchValue = 10;
        }

        //check for From Gbp toTheOtherFIveCurrencies
        if ((itemIndexFrom == 3) && (itemIndexTo == 1)) {
            switchValue = 11;
        }
        if ((itemIndexFrom == 3) && (itemIndexTo == 2)) {
            switchValue = 12;
        }
        if ((itemIndexFrom == 3) && (itemIndexTo == 3)) {
            switchValue = 13;
        }
        if ((itemIndexFrom == 3) && (itemIndexTo == 4)) {
            switchValue = 14;
        }
        if ((itemIndexFrom == 3) && (itemIndexTo == 5)) {
            switchValue = 15;
        }

        //check for From Cad toTheOtherFIveCurrencies
        if ((itemIndexFrom == 4) && (itemIndexTo == 1)) {
            switchValue = 16;
        }
        if ((itemIndexFrom == 4) && (itemIndexTo == 2)) {
            switchValue = 17;
        }
        if ((itemIndexFrom == 4) && (itemIndexTo == 3)) {
            switchValue = 18;
        }
        if ((itemIndexFrom == 4) && (itemIndexTo == 4)) {
            switchValue = 19;
        }
        if ((itemIndexFrom == 4) && (itemIndexTo == 5)) {
            switchValue = 20;
        }

        //check for From Zar toTheOtherFIveCurrencies
        if ((itemIndexFrom == 5) && (itemIndexTo == 1)) {
            switchValue = 21;
        }
        if ((itemIndexFrom == 5) && (itemIndexTo == 2)) {
            switchValue = 22;
        }
        if ((itemIndexFrom == 5) && (itemIndexTo == 3)) {
            switchValue = 23;
        }
        if ((itemIndexFrom == 5) && (itemIndexTo == 4)) {
            switchValue = 24;
        }
        if ((itemIndexFrom == 5) && (itemIndexTo == 5)) {
            switchValue = 25;
        }
    }


    public void getUserInput() {
        EditText txtMoneyValue = (EditText) findViewById(R.id.txtMoneyValue);
        user_input = txtMoneyValue.getText().toString();
        if (user_input.equals(" ")) {
            //tvResult.setText(user_input);
            Toast.makeText(MainActivity.this, "No value to convert: Enter value", Toast.LENGTH_LONG);
            // Toast.makeText(MainActivity.this, "Please enter a value greater than 0 for conversion", Toast.LENGTH_LONG);
        } else if (user_input.equals(String.valueOf(0))) {
            //tvResult.setText(user_input);
            Toast.makeText(MainActivity.this, "Input must be greater than 0", Toast.LENGTH_LONG);
            // Toast.makeText(MainActivity.this, "Please enter a value greater than 0 for conversion", Toast.LENGTH_LONG);
        } else {
            //convertValue();
            try {
                double doubleUserInput = Double.parseDouble(user_input);

                user_input = String.valueOf(doubleUserInput);
                Toast.makeText(MainActivity.this, "Input must be greater than 0", Toast.LENGTH_LONG);

            } catch (NumberFormatException nfe) {
                Log.d(TAG, nfe.toString());

            } catch (Exception x) {
                Log.d(TAG, x.toString());
                Toast.makeText(MainActivity.this, "Check your input!!!\nMake sure your input is right!!! ", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void setTvResult() {
        // getUserInput();

        convertValue();
        bd = new BigDecimal(finalConvertedValue);
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


    public void convertValue() {


        double eurValue = 1.00;
        double usdValue = 1.17384;
        double gbpValue = 0.89578;
        double cadValue = 1.521707;
        double zarValue = 16.861978;
        btConvert = (Button) findViewById(R.id.btConvert);
        getUserInput();
        try {
            Double.parseDouble(user_input);

            switch (switchValue) {

//check for From Euro toTheOtherFIveCurrencies
                case 0:
                    Log.d(TAG, "case1||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    //finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * eurValue);
                    Toast.makeText(MainActivity.this, "Select Currencies!!!", Toast.LENGTH_SHORT).show();


                    break;
                case 1:
                    Log.d(TAG, "case1||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    // finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * eurValue);
                    finalConvertedValue = Double.parseDouble(user_input) * eurValue;
                    break;
                case 2:
                    Log.d(TAG, "case2||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    //finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * usdValue);
                    finalConvertedValue = Double.parseDouble(user_input) * usdValue;


                    break;
                case 3:
                    Log.d(TAG, "case3||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    // finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * gbpValue);
                    finalConvertedValue = Double.parseDouble(user_input) * gbpValue;

                    break;
                case 4:
                    Log.d(TAG, "case4||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    // finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * cadValue);
                    finalConvertedValue = Double.parseDouble(user_input) * cadValue;

                    break;
                case 5:
                    Log.d(TAG, "case5||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    // finalConvertedValue = ( 1 / Double.parseDouble(user_input)  * zarValue);
                    finalConvertedValue = Double.parseDouble(user_input) * zarValue;

                    break;
                case 6:
                    Log.d(TAG, "case1||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 7:
                    Log.d(TAG, "case2||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 8:
                    Log.d(TAG, "case3||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 9:
                    Log.d(TAG, "case4||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 10:
                    Log.d(TAG, "case5||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 11:
                    Log.d(TAG, "case1||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 12:
                    Log.d(TAG, "case2||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 13:
                    Log.d(TAG, "case3||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 14:
                    Log.d(TAG, "case4||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 15:
                    Log.d(TAG, "case5||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 16:
                    Log.d(TAG, "case1||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 17:
                    Log.d(TAG, "case2||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 18:
                    Log.d(TAG, "case3||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 19:
                    Log.d(TAG, "case4||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 20:
                    Log.d(TAG, "case5||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 21:
                    Log.d(TAG, "case1||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 22:
                    Log.d(TAG, "case2||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 23:
                    Log.d(TAG, "case3||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 24:
                    Log.d(TAG, "case4||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;
                case 25:
                    Log.d(TAG, "case5||switchValue: " + switchValue + ", ItemFrom: " + itemFrom + ", itemTo:" + itemTo);
                    break;


            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }


    }


    /*
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
      }*/
     class GetCurrencies extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, ".........downloading conversion rate........", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            //Make a request to the server and get response back
            String url = "http://data.fixer.io/api/latest?access_key=b70449fc1a6ad33f2940bfdbcf125c41";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            Log.d(TAG, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    //getting jSon array node
                    JSONArray rate = jsonObject.getJSONArray("rates");

                    //looping through all contacts

                    for (int i = 0; i < rate.length(); i++) {
                        JSONObject s = rate.getJSONObject(i);
                        //currencies name
                        EUR = s.getString("EUR");
                        USD = s.getString("USD");
                        GBP = s.getString("GBP");
                        CAD = s.getString("CAD");
                        ZAR = s.getString("ZAR");
                        System.out.println(EUR + "test");

                       // Log.d(TAG,"rateMap.get(EUR): " + rateMap.get("EUR"));
                        Log.d(TAG,"EUR: " +EUR);



                        // tmp hash map for rate
                        rateMap = new HashMap<>();

                        //add each note to the arraylist
                        rateMap.put("EUR", EUR);
                        rateMap.put("USD", USD);
                        rateMap.put("GBP", GBP);
                        rateMap.put("CAD", CAD);
                        rateMap.put("ZAR", ZAR);

                        //adding map to rateLIst
                        //rateList.add(rateMap);

                        //Log.d(TAG, rateList.toString());
                       //System.out.println(TAG + "rateMap.get(EUR)" + rateMap.get(EUR));


                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "jSon PArsing Error: " + e.getMessage());
                    Log.d(TAG, "jSon PArsing Error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "jSon PArsing Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json server");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public class HttpHandler {

        private  final String TAG = HttpHandler.class.getSimpleName();

        public HttpHandler() {
        }

        public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
    }


}


