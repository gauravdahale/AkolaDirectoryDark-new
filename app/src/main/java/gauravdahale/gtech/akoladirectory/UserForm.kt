package gauravdahale.gtech.akoladirectory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gauravdahale.gtech.akoladirectory.models.UserModel;
import gauravdahale.gtech.akoladirectory.activity.MainActivityTabVersion;

public class UserForm extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private TextInputEditText UserName;
    private TextInputEditText UserOcccupation;
    private TextInputEditText UserNumber;
    private TextInputEditText UserCity;

    private Button bSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);


        UserName = (TextInputEditText) findViewById(R.id.userform_name);
        final String userna=UserName.getText().toString().trim();
        UserOcccupation = (TextInputEditText) findViewById(R.id.userform_occupation);
        UserNumber = (TextInputEditText) findViewById(R.id.userform_number);
        UserCity = (TextInputEditText) findViewById(R.id.userform_city);
        bSubmit = (Button) findViewById(R.id.b_submit);


        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences mSettings = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);

        final String token = mSettings.getString("USER_TOKEN","");

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.b_submit:
                        if (!isEmpty(UserName) && !isEmpty(UserOcccupation) && !isEmpty(UserNumber) && !isEmpty(UserCity)) {
                            NewShop(UserName.getText().toString().trim(), UserOcccupation.getText().toString().trim(), UserNumber.getText().toString().trim(), UserCity.getText().toString().trim(),token);
                        } else {
                            if (isEmpty(UserName)) {
                                Toast.makeText(getApplicationContext(), "Please enter your Name!", Toast.LENGTH_SHORT).show();
                            } else if (isEmpty(UserOcccupation)) {
                                Toast.makeText(getApplicationContext(), "Please Enter Your Occupation", Toast.LENGTH_SHORT).show();
                            } else if (isEmpty(UserNumber)) {
                                Toast.makeText(getApplicationContext(), "Please Enter Your Number", Toast.LENGTH_SHORT).show();
                            }
                            else if (isEmpty(UserCity)) {
                                Toast.makeText(getApplicationContext(), "Please Enter Your City", Toast.LENGTH_SHORT).show();
                            }
                        }

                        break;
                }
            }

            private void NewShop(String userName, String userOccupation, String userNumber,String userCity,String token) {
                //Creating a movie object with user defined variables
                UserModel user = new UserModel(userName,userOccupation,userNumber,userCity,token);
                //referring to movies node and setting the values from movie object to that location
                mDatabaseReference.child("users").push().setValue(user);

                FirebaseAnalytics mAnalytics = FirebaseAnalytics.getInstance(UserForm.this);

                Bundle bundle = new Bundle();
                bundle.putString("NewUser",userna);
                mAnalytics.logEvent("NewUsers", bundle);



                // Get current version code
                String username = UserName.getText().toString().trim();
                String userphone = UserNumber.getText().toString().trim();

                // Get saved version code
                SharedPreferences prefs = getSharedPreferences("USER_INFO", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_NAME", username);
                editor.putString("USER_NUMBER",userphone);
                editor.commit();

                backtomain();
            }
        });


    }

    private void backtomain() {
        Intent i = new Intent(getApplicationContext(), MainActivityTabVersion.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }


    //check if edittext is empty
    private boolean isEmpty(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Please fill the form to continue!!!",Toast.LENGTH_LONG).show();
    }
}