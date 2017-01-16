package com.awakeland.websitechange;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;


    @Override
    public void onClick(View v) {
        boolean c = false;

        c = checkSite("http://google.com");

        Log.v("WebsiteChange", String.valueOf(c));

        Snackbar.make(v, "Replace with your own action " + c, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO update this", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button = (Button) findViewById(R.id.check_host);

        button.setOnClickListener(this);
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
    public static boolean checkSite(String Address) {
        final String theURL = Address;

        new Thread() {
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
                    Log.i("WebsiteChange", "getResponseCode() IS : " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Log.i("WebsiteChange", "Connected");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("WebsiteChange", "No Connection");
                }
            }
        }.start();
        return true;
    }

}

