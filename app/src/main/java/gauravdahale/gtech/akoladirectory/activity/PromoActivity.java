package gauravdahale.gtech.akoladirectory.activity;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gauravdahale.gtech.akoladirectory.App;
import gauravdahale.gtech.akoladirectory.R;

public class PromoActivity extends AppCompatActivity {
    private TextView descripton;

    private Uri mCurrentRecordUri;
    private ImageView imagepromo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoimage);
        mCurrentRecordUri = getIntent().getData();
        descripton = (TextView) findViewById(R.id.promotext);
        imagepromo = (ImageView) findViewById(R.id.promoimage);
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        String desc = i.getStringExtra("desc");
        Picasso.with(getApplicationContext()).load(url).into(imagepromo);
    descripton.setText(desc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivityTabVersion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//        App.getContext().startActivity(intent);
    }
}
