package rustybus.wanderer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.janrone.lib.listener.ToolbarWebViewScrollListener;
import com.janrone.lib.ui.ToolbarWebView;
import com.software.shell.fab.FloatingActionButton;

import java.util.Random;

//import greenapple.wanderer.R;


public class WebViewActivity extends ActionBarActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    //WebView webView;
    ToolbarWebView webView;
    //ImageButton floatButton;
    FloatingActionButton fab;
    String url="http://www.google.com";

    ShareActionProvider shareActionProvider;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);

        random=new Random();
        initialise();
        setUrl(false);

        webView.loadUrl(url);
        /*floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUrl(false);
                webView.loadUrl(url);
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUrl(false);
                webView.loadUrl(url);
            }
        });
    }

    @Override
    public void onBackPressed() {

        //Go back in history
        if(webView.canGoBack()) {
            webView.goBack();
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }

        else {
            webView.clearHistory();
            super.onBackPressed();
        }
    }

    private void initialise() {
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        //toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Google");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        //floatButton=(ImageButton)findViewById(R.id.fButton);
        fab=(FloatingActionButton)findViewById(R.id.fab);

        //webView=(WebView)findViewById(R.id.webView);
        webView= (ToolbarWebView) findViewById(R.id.toolBarWebView);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);

        if(MainActivity.setJavascript)
            webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());

        webView.setOnCustomScroolChangeListener(webView, new ToolbarWebViewScrollListener() {
            @Override
            public void onHide() {
                fab.hide();
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                progressBar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }

            @Override
            public void onShow() {
                fab.show();
                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                progressBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }
        });
    }

    private void setUrl(boolean reload) {

        if(!reload) {
            int size = MainActivity.pageList.size();

            if (size != 0) {
                int randomNumber = random.nextInt(size);

                switch (Integer.parseInt(MainActivity.pageList.get(randomNumber))) {

                    //Wikipedia
                    case 0:
                        url = "http://en.wikipedia.org/wiki/Special:Random";
                        toolbar.setTitle("Wikipedia");
                        break;

                    //9gag
                    case 1:
                        url = "http://9gag.com/random";
                        toolbar.setTitle("9gag");
                        break;

                    //Explosm
                    case 2:
                        url = "http://explosm.com/comics/random";
                        toolbar.setTitle("Cyanide and Happiness");
                        break;

                    //Reddit
                    case 3:
                        url = "http://reddit.com/r/random/";
                        toolbar.setTitle("Reddit");
                        break;

                    //Wordpress
                    case 4:
                        url = "https://en.blog.wordpress.com/next/";
                        toolbar.setTitle("Wordpress");
                        break;

                    //IMDb
                    case 5:
                        url = "http://www.imdb.com/random/title";
                        toolbar.setTitle("IMDb");
                        break;

                    //Wikihow
                    case 6:
                        url = "http://wikihow.com/Special:Randomizer";
                        toolbar.setTitle("Wikihow");
                        break;
                }
            }
        }

        else
            url=webView.getUrl();
    }

    private class MyWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setProgress(100);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);

            if(shareActionProvider!=null)
                shareActionProvider.setShareIntent(getShareIntent());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setUrl(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        MenuItem item=menu.findItem(R.id.share);
        shareActionProvider= (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareActionProvider.setShareIntent(getShareIntent());
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            /*case R.id.share:

                //shareActionProvider.setShareIntent(getShareIntent());
                Log.i("fast cars in the city", "SHARE BUTTON PRESSED!!!");

                Intent shareIntent=new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Discovery!");
                String shareMessage = "I found this on Web Wanderer: " + webView.getUrl();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                //startActivity(Intent.createChooser(shareIntent, "Share link via"));

                shareActionProvider.setShareIntent(shareIntent);

                break;*/

            case android.R.id.home:

                webView.clearHistory();
                NavUtils.navigateUpFromSameTask(this);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent getShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Discovery!");

        String message;
        if(webView.getUrl()!=null)
            message = "I found this on Wanderer: " + webView.getUrl();
        else
            message = "Find more using Web Wanderer app";
        intent.putExtra(Intent.EXTRA_TEXT, message);
        return intent;
    }
}