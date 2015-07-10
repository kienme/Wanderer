package rustybus.wanderer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import greenapple.wanderer.R;

/*
    Adding a new link?

    1. Add to strings.xml (x3)
    2. Add to setUrl in WebViewActivity

 */


public class MainActivity extends ActionBarActivity {

    //Stores the pages to view selected by user in settings (number code)
    static List<String> pageList;

    //Enable/Disable Javascript
    static boolean setJavascript;

    //UI elements
    Toolbar toolbar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Gets selected pages from sharedprefs if present, else the default selection
        pageList=new ArrayList<>();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(preferences.contains("Pages"))
            pageList.addAll(preferences.getStringSet("Pages", null));
        else
            pageList.addAll(Arrays.asList(getResources().getStringArray(R.array.page_values)));

        //Get preference for Javascript, false by default
        setJavascript=preferences.getBoolean("Javascript", false);

        //Set toolbar with white text
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        //toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //Start WebActivity on click of the button
        button=(Button)findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webViewActivity=new Intent("rustybus.wanderer.WebViewActivity");
                startActivity(webViewActivity);
            }
        });
    }

    //Refresh pageList on coming back to MainActivity from other activities

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(preferences.contains("Pages")) {
            pageList.clear();
            pageList.addAll(preferences.getStringSet("Pages", null));
        }

        setJavascript=preferences.getBoolean("Javascript", false);
    }

    //Menuuuuuu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            //Open Preference Screen
            case R.id.menu_settings:
                startActivity(new Intent("rustybus.wanderer.PrefsActivity"));
                break;

            case R.id.about:
                startActivity(new Intent("rustybus.wanderer.AboutActivity"));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}