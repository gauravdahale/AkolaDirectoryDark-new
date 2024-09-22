package gauravdahale.gtech.akolacrm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gauravdahale.gtech.akolacrm.CategoryAdapter;
import gauravdahale.gtech.akolacrm.Model.CategoryModel;
import gauravdahale.gtech.akolacrm.R;

public class ActivityCategory extends AppCompatActivity {
    RecyclerView mRecycler;
    CategoryAdapter adapter;
    private List<CategoryModel> categoryList;
    private static final String TAG = " Category Activity ";
    private static final String REF = "REFERENCE";
    private static final String TITLE = "Title";
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryList = new ArrayList<CategoryModel>();
        Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar =  findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        TextView toolbartxt = findViewById(R.id.toolbartextview);
        final String ref= getIntent().getStringExtra(REF);
        String title= getIntent().getStringExtra(TITLE);
         city=getIntent().getStringExtra("CITY");
        toolbartxt.setText("Root>"+title+"(CategoryActivity)");

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference(ref);
        mRecycler = findViewById(R.id.recycler_view);


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getcategory(dataSnapshot,ref);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        //  initnormalrecyclerview();


    }

    private void getcategory(DataSnapshot dataSnapshot,String ref) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String cat = ds.getKey();
            Log.d(TAG, cat);
            CategoryModel model=new CategoryModel(ds.getKey());
            categoryList.add(model);
            adapter = new CategoryAdapter(categoryList,getApplicationContext(),ref,city);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter.notifyDataSetChanged();
            mRecycler.setLayoutManager(layoutManager);
            mRecycler.setItemAnimator(new DefaultItemAnimator());
            mRecycler.setAdapter(adapter);
        }

    }


    private void initnormalrecyclerview() {


    }

    /*private void init() {
    Query query =FirebaseDatabase.getInstance().getReference("Nandura");
        FirebaseRecyclerOptions<CategoryModel> options =
                new FirebaseRecyclerOptions.Builder<CategoryModel>()
                        .setQuery(query, CategoryModel.class).setLifecycleOwner(this)
                        .build();
    mAdapter=new FirebaseRecyclerAdapter<CategoryModel,CategoryViewHolder>(options) {
        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorycardview,viewGroup,false);
        return  new CategoryViewHolder(v, categorylist);

        }

        @Override
        protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull CategoryModel model) {
     holder.getHeading().setText(model.getN());

        }
    };
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
mRecycler.setAdapter(mAdapter);
    }*/
}
