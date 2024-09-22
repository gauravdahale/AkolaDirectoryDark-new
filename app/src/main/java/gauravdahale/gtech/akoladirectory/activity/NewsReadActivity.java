package gauravdahale.gtech.akoladirectory.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import gauravdahale.gtech.akoladirectory.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NewsReadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_news_read);

        Intent i = getIntent();
        String image = i.getStringExtra("newspage");
        String title = i.getStringExtra("pagenumber");
        setTitle(title);
        Picasso.with(this).load(image).into((PhotoView) findViewById(R.id.detail_image));

    }
}