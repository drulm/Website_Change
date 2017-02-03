package com.awakeland.websitechange;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import java.util.Arrays;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SiteEdit extends ListActivity implements View.OnClickListener {

    private Button addSite;
    EditText et;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    String SITEFILE = "SiteEditFile";
    String listSites[] = {"https://google.com", "https://news.google.com", "https://bing.com"};
    String siteListJoined;
    List values = new ArrayList();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_edit);

        addSite = (Button) findViewById(R.id.buttonSiteAdd);
        addSite.setOnClickListener(this);

        et = (EditText) findViewById(R.id.siteAddress);

        //for (int i = 0; i < listSites.length; i++)

        try {
            inputStream  = openFileInput(SITEFILE);
            inputStream.read(siteListJoined.getBytes());
            inputStream.close();
            List<String> values = new ArrayList<String>(Arrays.asList(siteListJoined.split("|||")));
            Log.i("WebsiteChange", "SiteEdit: SITEFILE: " + Arrays.toString(values.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);



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
                //List myList = new ArrayList();
                // Only add if string is not empty.
                site = et.getText().toString();
                if (! site.isEmpty() &&  URLUtil.isValidUrl(site)) {
                    values.add(site);
                    adapter.add(site);
                    et.setText("");
                    siteListJoined = TextUtils.join("|||", values);

                    try {
                        /* fileStream = openFileInput(SITEFILE);
                        ObjectInputStream ois = new ObjectInputStream(fileStream);
                        ArrayList<Object> listSitesObjects = (ArrayList<Object>) ois.readObject();
                        ois.close();*/
                        Log.i("WebsiteChange", "SiteEdit: SITEFILE: " + SITEFILE);
                        outputStream  = openFileOutput(SITEFILE, Context.MODE_PRIVATE);
                        outputStream.write(siteListJoined.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    // Create snackbar message.
                    Snackbar.make(view, "Site: " + site + "is not a valid URL", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
           /* case R.id.exit:
                finish();
                break;*/
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

