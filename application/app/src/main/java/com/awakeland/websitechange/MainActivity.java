package com.awakeland.websitechange;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonTest;
    private Button buttonAddSite;
    private volatile int networkReturnStatus;
    private EditText statusCode;

    @Override
    public void onClick(View v) {
        int status;

        /*Log.v("WebsiteChange", "-----Start Ping-----");
        status = checkSite("https://google.com");
        Log.v("WebsiteChange", "Y: Status Returned:" + status);
        status = checkSite("192.168.0.3");
        Log.v("WebsiteChange", "No: Status Returned:" + status);
        status = checkSite("https://yahoo.com");
        Log.v("WebsiteChange", "Yes: Status Returned:" + status);
        status = checkSite("http://nosite.nosite");
        Log.v("WebsiteChange", "No: Status Returned:" + status);
        Log.v("WebsiteChange", "-----End Ping-----");*/
/*
        switch (v.getId()) {

            case R.id.oneButton:
                // do your code
                break;

            case R.id.twoButton:
                // do your code
                break;

            case R.id.threeButton:
                // do your code
                break;

            default:
                break;
        }
        */

        switch (v.getId()) {
            case R.id.check_host:
                WebPageTask task1 = new WebPageTask();
                task1.execute("https://google.com");
                WebPageTask task2 = new WebPageTask();
                task2.execute("127.0.0.1");
                WebPageTask task3 = new WebPageTask();
                task3.execute("https://news.google.com");
                WebPageTask task4 = new WebPageTask();
                task4.execute("http://nosite.nosite");
                WebPageTask task5 = new WebPageTask();
                task5.execute("http://awakeland.com");
                Snackbar.make(v, "Button pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.add_site:
                Intent intent = new Intent(MainActivity.this, SiteEdit.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonTest = (Button) findViewById(R.id.check_host);
        buttonTest.setOnClickListener(this);
        buttonAddSite = (Button) findViewById(R.id.add_site);
        buttonAddSite.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }



    /**
     * checkSite(String Address)
     * @param Address
     * @return
     */
    public int checkSite(String Address) {
        final String theURL = Address;
        final int[] returnValue = new int[1];

        Thread t = new Thread() {
            @Override
            public void run() {
                int responseCode;

                super.run();
                try {
                    URL url = new URL(theURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();

                    responseCode = connection.getResponseCode();
                    networkReturnStatus = responseCode;
                    Log.i("WebsiteChange", "getResponseCode(): " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Log.i("WebsiteChange", "Connected!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("WebsiteChange", "No Connection");
                }
            }
        };
        t.start();
        return networkReturnStatus;
    }


    private class WebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... theURL) {
            int responseCode;
            String responseReturnString = "";

            Log.i("WebsiteChange", "2:In WebPageTask " + theURL[0]);
            try {
                URL url = new URL(theURL[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                responseCode = connection.getResponseCode();
                networkReturnStatus = responseCode;
                Log.i("WebsiteChange", "2:getResponseCode(): " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    responseReturnString = "Connection" + responseCode;
                }
                Log.i("2: In WebsiteChange", "2:Connected! " + responseCode);
            } catch (Exception e) {
                e.printStackTrace();
                responseReturnString = "No Connection";
                Log.i("2: In WebsiteChange", "2:No Connection");
            }
            return responseReturnString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("2:WebsiteChange", result);
            myHandler.sendEmptyMessage(0);
        }
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // calling to this function from other pleaces
                    // The notice call method of doing things
                    break;
                default:
                    break;
            }
        }
    };


}

