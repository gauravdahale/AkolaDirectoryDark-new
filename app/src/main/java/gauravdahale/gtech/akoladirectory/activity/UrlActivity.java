package gauravdahale.gtech.akoladirectory.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;

import gauravdahale.gtech.akoladirectory.R;

public class UrlActivity extends AppCompatActivity {


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_url);
            Intent i = getIntent();
            String url = i.getStringExtra("weburl");

            WebView webView = (WebView) findViewById(R.id.webView);
            webView.loadUrl(url);
        }
    }
