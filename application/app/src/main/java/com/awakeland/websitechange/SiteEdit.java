package com.awakeland.websitechange;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.webkit.URLUtil;

/**
 *
 */
public class SiteEdit extends ListActivity implements View.OnClickListener {

    private Button addSite;
    EditText et;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    String SITEFILE = "SiteEditFile.txt";
    String siteListJoined;
    List values = new ArrayList();


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

        et = (EditText) findViewById(R.id.siteAddress);

        siteListJoined = fileReadString(getApplicationContext());

        values = new ArrayList<String>(Arrays.asList(siteListJoined.split("\\|")));
        if (values.get(0) == "") {
            values.remove(0);
        }

        // Remove duplicates, if any.
        Set<String> hashValues = new HashSet<>();
        hashValues.addAll(values);
        values.clear();
        values.addAll(hashValues);

        Collections.sort(values);

        Log.i("WebsiteChange", "SiteEdit: Read SITEFILE: " + siteListJoined);
        //Log.i("WebsiteChange", "SiteEdit: Read SITEFILE siteItems: " + siteItems);
        Log.i("WebsiteChange", "SiteEdit: Read SITEFILE values: " + values);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }


    /**
     *
     * @param lv
     * @param v
     * @param pos
     * @param id
     */
    @Override
    protected void onListItemClick (ListView lv, View v, int pos, long id) {
        Log.i("WebsiteChange", "SiteEdit: List Item Clicked: " + pos);
    }


    /**
     *
     * @param view
     */
    public void onClick(View view) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        String site;


        switch (view.getId()) {
            case R.id.buttonSiteAdd:
                // Only add if string is not empty.
                site = et.getText().toString();
                if (
                        ! site.isEmpty()
                        && ! values.contains(site)
                        &&  URLUtil.isValidUrl(site)
                        ) {
                    values.add(site);
                    adapter.add(site);

                    et.setText("");
                    try {
                        PrintWriter writer = new PrintWriter(SITEFILE);
                        writer.print("");
                        writer.close();
                    }
                    catch (FileNotFoundException e) {
                        // Err msg here.
                    }

                    siteListJoined = TextUtils.join("|", values);
                    fileWriteString(siteListJoined, getApplicationContext());
                    Log.i("WebsiteChange", "SiteEdit: Write SITEFILE: " + siteListJoined);
                }
                else {
                    // Create snackbar message.
                    Snackbar.make(view, "Site: " + site + "is not a valid URL", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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

