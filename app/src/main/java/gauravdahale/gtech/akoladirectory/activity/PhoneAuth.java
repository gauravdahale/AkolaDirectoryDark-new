package gauravdahale.gtech.akoladirectory.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gauravdahale.gtech.akoladirectory.R;

/**
 * Created by Gaurav on 8/8/2018.
 */


public class PhoneAuth extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "PHONEAUTH";
    private FirebaseAuth auth;
    private TextInputEditText editTextMobile, editusername, edituseroccupation, editusercity;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phoneauth);


        editTextMobile = findViewById(R.id.auth_number);
        editusername = findViewById(R.id.auth_name);
        edituseroccupation = findViewById(R.id.auth_occupation);
        editusercity = findViewById(R.id.auth_city);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        // finish();




        SharedPreferences mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);

        final String token = mSettings.getString("USER_TOKEN", "");

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();
                String username = editusername.getText().toString();
                String useroccupation = edituseroccupation.getText().toString();
                String usercity = editusercity.getText().toString();
                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }
                else
                if (username.isEmpty()) {
                    editusername.setError("Enter Your Name");
                    editusername.requestFocus();
                    return;
                }
                else
                if (useroccupation.isEmpty()) {
                    edituseroccupation.setError("Enter Your Occupation");
                    edituseroccupation.requestFocus();
                    return;
                }else{
                    if (usercity.isEmpty()) {
                        editusercity.setError("Enter Your City");
                        editusercity.requestFocus();
                        return;
                    }
                }

                Intent intent = new Intent(PhoneAuth.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile",mobile);
                intent.putExtra("username", username);
                intent.putExtra("useroccupation", useroccupation);
                intent.putExtra("usercity", usercity);

                startActivity(intent);
                // finish();
            }
        });


    }

}

