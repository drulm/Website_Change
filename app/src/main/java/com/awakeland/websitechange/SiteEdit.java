package com.awakeland.websitechange;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class SiteEdit extends ListActivity implements View.OnClickListener {

    private Button addSite;
    EditText editttxt;
    String SITEFILE = "SiteEditFile.txt";
    String siteListJoined;
    List siteArrayList = new ArrayList();
    ArrayAdapter<String> adapter;
    private volatile int networkReturnStatus;


    /**
     *
     * @param str
     * @param context
     */
    private void fileWriteString(String str, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(
                        context.openFileOutput(SITEFILE, Context.MODE_PRIVATE)
                    );
            osw.write(str);
            osw.close();
        }
        catch (IOException e) {
            Log.e("WebsiteChange", "File writing failed: " + e.toString());
        }
    }

    /**
     *
     * @param context
     * @return
     */
    private String fileReadString(Context context) {
        String str = "";
        try {
            InputStream is = context.openFileInput(SITEFILE);
            if ( is != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader buffRead = new BufferedReader(inputStreamReader);
                String recstr = "";
                StringBuilder sb = new StringBuilder();
                while ( (recstr = buffRead.readLine()) != null ) {
                    sb.append(recstr);
                }
                is.close();
                str = sb.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.i("WebsiteChange", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.i("WebsiteChange", "Reading file failed: " + e.toString());
        }
        return str;
    }


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int n;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_edit);

        addSite = (Button) findViewById(R.id.buttonSiteAdd);
        addSite.setOnClickListener(this);

        editttxt = (EditText) findViewById(R.id.siteAddress);

        siteListJoined = fileReadString(getApplicationContext());

        siteArrayList = new ArrayList<String>(Arrays.asList(siteListJoined.split("\\|")));
        if (siteArrayList.get(0) == "") {
            siteArrayList.remove(0);
        }

        // Remove duplicates, if any.
        Set<String> hashsiteArrayList = new HashSet<>();
        hashsiteArrayList.addAll(siteArrayList);
        siteArrayList.clear();
        siteArrayList.addAll(hashsiteArrayList);

        Collections.sort(siteArrayList);

        Log.i("WebsiteChange", "SiteEdit: Read SITEFILE: " + siteListJoined);
        Log.i("WebsiteChange", "SiteEdit: Read SITEFILE siteArrayList: " + siteArrayList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, siteArrayList);
        setListAdapter(adapter);
    }


    /**
     *
     * @param listview
     * @param view
     * @param pos
     * @param id
     */
    @Override
    protected void onListItemClick (final ListView listview, View view, int pos, long id) {
        final int p = pos;
        final View v = view;

        Log.i("WebsiteChange", "SiteEdit: List Item Clicked: " + pos);
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("WebsiteChange", "SiteEdit: Menu Item: " + item.getTitle());
                if (item.getTitle().equals("Delete Entry")) {
                    Log.i("WebsiteChange", "SiteEdit: Menu Item: Inside Delete");
                    siteArrayList.remove(p);
                    updateSaveFile("", v, "delete");
                    adapter.notifyDataSetChanged();
                }
                else if (item.getTitle().equals("Check Site")) {
                    Log.i("WebsiteChange", "SiteEdit: Menu Item: Inside CheckSite");
                    WebPageTask task1 = new WebPageTask();
                    task1.execute(siteArrayList.get(p).toString());
                }
                return true;
            }
        });

        popup.show();
    }


    /**
     *
     */
    private class WebPageTask extends AsyncTask<String, Void, String> {
        /**
         *
         * @param theURL
         * @return
         */
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


        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i("2:WebsiteChange", result);
            myHandler.sendEmptyMessage(0);
        }
    }


    /**
     *
     */
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // The notice call method of doing things
                    break;
                default:
                    break;
            }
        }
    };



    /**
     *
     * @param site
     * @param view
     */
    public void updateSaveFile(String site, View view, String command) {

        String snackMsg;
        snackMsg = "Site added";

        // Check if we should add the site.
        if (! siteArrayList.contains(site) && ! site.isEmpty() && URLUtil.isValidUrl(site)) {
            siteArrayList.add(site);
            adapter.add(site);
        }
        else {
            snackMsg = "Site entered is not a valid URL";
        }

        if (command.contains("delete")) {
            snackMsg = "Site deleted";
        }
        else if (siteArrayList.contains(site)) {
            snackMsg = "Site is already in list";
        }

        // Create snackbar message.
        Snackbar snack;
        snack = Snackbar.make(view, snackMsg, Snackbar.LENGTH_LONG);
        View sbView = snack.getView();
        sbView.setBackgroundColor(Color.YELLOW);
        snack.setAction("Action", null).show();

        // Clear out the file.
        try {
            PrintWriter writer = new PrintWriter(SITEFILE);
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException e) {
            Log.i("WebsiteChange", "SiteEdit: Could not clear save file: " + SITEFILE);
        }
        // Write out the file to update for changes.
        siteListJoined = TextUtils.join("|", siteArrayList);
        fileWriteString(siteListJoined, getApplicationContext());
        Log.i("WebsiteChange", "SiteEdit: Write SITEFILE: " + siteListJoined);
    }


    /**
     *
     * @param view
     */
    public void onClick(View view) {
        String site;

        switch (view.getId()) {
            case R.id.buttonSiteAdd:
                // Only add if string is not empty.
                site = editttxt.getText().toString();
                updateSaveFile(site, view, "add");
                break;
        }
        adapter.notifyDataSetChanged();
    }


    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
