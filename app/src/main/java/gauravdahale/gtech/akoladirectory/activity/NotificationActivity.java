package gauravdahale.gtech.akoladirectory.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import gauravdahale.gtech.akoladirectory.models.ContactModel;
import gauravdahale.gtech.akoladirectory.R;
import gauravdahale.gtech.akoladirectory.models.CallModel;

/**
 * Created by jayda on 9/24/2017.
 */

public class NotificationActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {


    private TextView addresstextview;
    private TextView descriptionview;
    private SliderLayout sliderLayout;
    FirebaseAnalytics mAnalytics;
    private Uri mCurrentRecordUri;
    private TextView phonetextview;
    private TextView titletextview;
    private TextView owner;
    private TextView timings, counter;
    String url1, url2, url3, url4, imageurl1, imageurl2, imageurl3, imageurl4;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String user;
    String storedphone, n, a, p, image1, image2, image3, image4, o, t, d, c;
    private DatabaseReference reference, databaseReference;
    Button callbutton;
public String ref="notices";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mAnalytics = FirebaseAnalytics.getInstance(this);
        final SharedPreferences mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Intent i = getIntent();
if(i.getStringExtra("key")!=null) {
    ref = i.getStringExtra("key");
}

        mCurrentRecordUri = getIntent().getData();
        titletextview = (TextView) findViewById(R.id.shoptitlerecord);
        addresstextview = (TextView) findViewById(R.id.recordaddress);
        callbutton = findViewById(R.id.callbtnrecord);
        sliderLayout = (SliderLayout) findViewById(R.id.imagerecord);
        descriptionview = (TextView) findViewById(R.id.services);
        owner = (TextView) findViewById(R.id.ownertxtview);
        timings = (TextView) findViewById(R.id.recordtiming);
        counter = (TextView) findViewById(R.id.counter);
        user = auth.getCurrentUser().getUid();
        n = i.getStringExtra("recordtitle");
        a = i.getStringExtra("address");
        image1 = i.getStringExtra("url");
        image2 = i.getStringExtra("url2");
        image3 = i.getStringExtra("url3");
        image4 = i.getStringExtra("url4");
        p = i.getStringExtra("phone");
        d = i.getStringExtra("desc");
        t = i.getStringExtra("Timings");
        o = i.getStringExtra("Owner");

        c = i.getStringExtra("Counter");

        databaseReference = database.getReference(ref);

        storedphone = mSettings.getString("USER_NUMBER", "");
        final String storedname = mSettings.getString("USER_NAME", "");


        try {
            titletextview.setText(n);
            timings.setText(t);
            addresstextview.setText(a);
            owner.setText(o);
            counter.setText(c);
            imageurl1 = image1;
            imageurl2 = image2;
            imageurl3 = image3;
            imageurl4 = image4;
            if (image2 == null) image2 = image1;
            if (image3 == null) image3 = image1;
            if (image4 == null) image4 = image1;
        } catch (NullPointerException e) {
        }

        try {

            descriptionview.setText(Html.fromHtml(d));
        } catch (NullPointerException e) {
            descriptionview.setText(d);
        }
        setslider(imageurl1, imageurl2, imageurl3, imageurl4);
        final String phone = p;
        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String SEPARATOR = " ";
                String n1, n2;
                mAnalytics = FirebaseAnalytics.getInstance(NotificationActivity.this);
                SharedPreferences mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
                final String storedname = mSettings.getString("USER_NAME", "");
                final String storedphone = mSettings.getString("USER_NUMBER", "");
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, p);
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storedname);
                mAnalytics.logEvent("Call", bundle);
                final String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                //Toast.makeText(getApplicationContext(),"Tap again to dial",Toast.LENGTH_LONG).show();
                logcall(n, storedname, storedphone, date);


                if (phone.contains(SEPARATOR)) {
                    String[] parts = phone.split(SEPARATOR);

                    n1 = parts[0];
                    n2 = parts[1];
                    buttonclicker(n1, n2);
                } else {
                    String uri = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }

            }
        });
    }
/*
        ValueEventListener recordlistner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ContactModel record = dataSnapshot.getValue(ContactModel.class);
                assert record != null;
                setdata(record);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(recordlistner);

    }
*/

    public void logcall(String shoptitle, String username, String userphone, String calldate) {
        CallModel call = new CallModel(shoptitle, username, userphone, calldate);
        DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference();
        databasereference.child("CallLog").child(shoptitle).push().setValue(call);

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public View getRating(View view) {

        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        Button submit;
        final RatingBar ratingBar;
        LayoutInflater inflater = this.getLayoutInflater();
        final View alertlayout = inflater.inflate(R.layout.custom_rating_dialog, null);
        dialogbuilder.setView(alertlayout);
        ratingBar = alertlayout.findViewById(R.id.addratingbar);
        submit = alertlayout.findViewById(R.id.submitbtn);
        final float rating = ratingBar.getRating();

        final AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (ratingBar.getRating() == 0.0f) {
                    Toast.makeText(getApplicationContext(), "Pleae give some rating", Toast.LENGTH_LONG).show();
                    return;
                }
                databaseReference.child("Ratings").child(user).child("review").setValue(ratingBar.getRating());
                alertDialog.dismiss();
            }
        });
        return view;

    }

    public void setslider(String imageurl1, String imageurl2, String imageurl3, String imageurl4) {
        url1 = imageurl1;
        url2 = imageurl2;
        url3 = imageurl3;
        url4 = imageurl4;
        if (url2 == null) {
            url2 = url1;
        }
        if (url3 == null) {
            url3 = url1;
        }

        if (url4 == null) {
            url4 = url1;
        }

        HashMap<String, String> url_maps = new HashMap<String, String>();

        url_maps.put("0", url1);
        url_maps.put("1", url2);
        url_maps.put("2", url3);
        url_maps.put("3", url4);
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name)).empty(R.drawable.logo)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.addOnPageChangeListener(this);

    }

    public void buttonclicker(final String phone1, final String phone2) {
        TextView phonetxt1, phonetxt2;
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View alertlayout = inflater.inflate(R.layout.custom_call_dialog, null);
        dialogbuilder.setView(alertlayout);
        phonetxt1 = alertlayout.findViewById(R.id.numberone);
        phonetxt1.setText(phone1);
        phonetxt2 = alertlayout.findViewById(R.id.numbertwo);
        phonetxt2.setText(phone2);

        phonetxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + phone1;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }


        });
        phonetxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + phone2;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }


        });

        AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();
    }

    public void setdata(final ContactModel record) {
    }
}


