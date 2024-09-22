package gauravdahale.gtech.akolacrm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gauravdahale.gtech.akolacrm.ItemClickListener;
import gauravdahale.gtech.akolacrm.Model.CategoryModel;
import gauravdahale.gtech.akolacrm.R;
import gauravdahale.gtech.akolacrm.RootAdapter;

public class MainActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter mAdapter;
    RecyclerView mRecycler;
    RootAdapter adapter;
    private List<CategoryModel> categoryList;
    private static final String TAG = " Main Activity ";
ItemClickListener listener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryList = new ArrayList<CategoryModel>();
        Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar =  findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        TextView toolbartxt = findViewById(R.id.toolbartextview);
        toolbartxt.setText("CRM ROOT");
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        mRecycler = findViewById(R.id.recycler_view);


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getcategory(dataSnapshot);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
      //  initnormalrecyclerview();d


    }

    private void getcategory(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String cat = ds.getKey();
            Log.d(TAG, cat);
            CategoryModel model=new CategoryModel(ds.getKey());
            categoryList.add(model);
            adapter = new RootAdapter(categoryList,MainActivity.this);
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
