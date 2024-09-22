package gauravdahale.gtech.akolacrm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gauravdahale.gtech.akolacrm.ItemClickListener;
import gauravdahale.gtech.akolacrm.Model.CategoryModel;
import gauravdahale.gtech.akolacrm.Model.ContactModel;
import gauravdahale.gtech.akolacrm.R;
import gauravdahale.gtech.akolacrm.ShopAdapter;


public class ShopActivity extends AppCompatActivity {
    RecyclerView mRecycler;
    ShopAdapter adapter;
    private List<CategoryModel> categoryList;
    private static final String TAG = " SHOP ACTIVITY ";
    private static final String REF = "REFERENCE";
    private static final String TITLE = "Title";
    public String city;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
//fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ShopActivity.this, RecordActivity.class);
                i.putExtra("REF", getIntent().getStringExtra(REF));
                startActivity(i);

            }
        });


        categoryList = new ArrayList<CategoryModel>();
        Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
         toolbar =  findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        TextView toolbartxt = findViewById(R.id.toolbartextview);
        String ref = getIntent().getStringExtra(REF);
        String title = getIntent().getStringExtra(TITLE);
        city = getIntent().getStringExtra("CITY");
        toolbartxt.setText("Shop Activity");
        toolbartxt.setText(title);
        mRecycler = findViewById(R.id.shop_recycler);
        //DatabaseReference mReference = FirebaseDatabase.getInstance().getReference(ref);
        Query query = FirebaseDatabase.getInstance().getReference(ref);
        FirebaseRecyclerOptions<ContactModel> options =
                new FirebaseRecyclerOptions.Builder<ContactModel>().setLifecycleOwner(this)
                        .setQuery(query, ContactModel.class).setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter mAdapter = new FirebaseRecyclerAdapter<ContactModel, ItemViewHolder>(options) {


            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.shop_card_view, viewGroup, false);
                return new ItemViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull final ContactModel model) {
                holder.Name.setText(model.getN());
                holder.viewscounter.setText(getRef(position).getKey());

                holder.setItemOnClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(int i) {
                        String itemref = city + "/" + getRef(position).getParent().getKey();
                        Log.d("Pooja", itemref);
                        Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                        intent.putExtra("PARCEL", model);
                        intent.putExtra("CITY", city);
                        intent.putExtra("ID", getRef(position).getKey());
                        intent.putExtra("REF", itemref);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapter);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener itemClickListener;
        TextView Name;
        TextView viewscounter;

        public ItemViewHolder(View v) {
            super(v);
            this.Name = (TextView) v.findViewById(R.id.shopname);
            this.viewscounter = (TextView) v.findViewById(R.id.shopkey);
            v.setOnClickListener(this);

        }

        public void setItemOnClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(getLayoutPosition());
        }

    }
}