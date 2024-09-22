package gauravdahale.gtech.akolaadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();

    DatabaseReference mDatabaseReference = this.database.getReference();
    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference mdatabase;


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView Address;
        TextView Phone, Occu, City;

        public ItemViewHolder(View v) {
            super(v);
            name =(TextView)v.findViewById(R.id.user);
            Phone =(TextView)v.findViewById(R.id.userphone);
            Occu =(TextView)v.findViewById(R.id.userbusiness);
            City =(TextView)v.findViewById(R.id.usercity);

        }

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        RecyclerView = (RecyclerView) findViewById(R.id.recyclerview);




        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.setLayoutManager(this.linearLayoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("users");



        mdatabase = this.mDatabaseReference.child("users").getRef();

        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)
                        .build();
        adapter=(new FirebaseRecyclerAdapter<UserModel, ItemViewHolder>(options) {


            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.usercardview, parent, false);

                return new ItemViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder viewHolder, int position, @NonNull final UserModel items) {
                viewHolder.name.setText(items.getUserName());
                viewHolder.Phone.setText(items.getUserNumber());
                viewHolder.Occu.setText(items.getUserOccupation());
                viewHolder.City.setText(items.getUserCity());





            }
        });

        RecyclerView.setAdapter(adapter);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }}












