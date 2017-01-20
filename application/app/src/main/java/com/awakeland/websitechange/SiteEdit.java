package com.awakeland.websitechange;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SiteEdit extends ListActivity implements View.OnClickListener {

    private Button addSite;
    EditText et;
    String listSites[] = {"https://google.com", "https://news.google.com", "https://bing.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_edit);

        addSite = (Button) findViewById(R.id.buttonSiteAdd);
        addSite.setOnClickListener(this);

        et = (EditText) findViewById(R.id.siteAddress);
        List values = new ArrayList();
        for (int i = 0; i < listSites.length; i++) {
            values.add(listSites[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }


    public void onClick(View view) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        String site;

        switch (view.getId()) {
            case R.id.buttonSiteAdd:
                List myList = new ArrayList();
                // Only add if string is not empty.
                site = et.getText().toString();
                if (! site.isEmpty() &&  URLUtil.isValidUrl(site)) {
                    myList.add(site);
                    adapter.add(site);
                    et.setText("");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}

