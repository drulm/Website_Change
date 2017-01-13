package com.awakeland.websitechange;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;


    @Override
    public void onClick(View v) {
        boolean c = false;
        // boolean c = checkHost("8.8.8.8", 53, 2000);
        try {
            URL url = new URL("http://www.google.com/");
            // c = isAvailable(url);
            //more code goes here
        } catch (MalformedURLException ex){
        //do exception handling here
        }

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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

    public static boolean isAvailable(URL url){
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            urlConnection.disconnect();
            return true;
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return false;
    }

    public static boolean checkHost(String Addr, int port, int timeout){
        boolean conn = false;
        Socket sock;
        try {
            sock = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(Addr, port);
            sock.connect(socketAddress, timeout);
            if (sock.isConnected()) {
                conn = true;
                sock.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

}


