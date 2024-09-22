package gauravdahale.gtech.akoladirectory.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import gauravdahale.gtech.akoladirectory.ItemClickListener;
import gauravdahale.gtech.akoladirectory.NewsItem;
import gauravdahale.gtech.akoladirectory.R;
import gauravdahale.gtech.akoladirectory.data.CategoryAdapter;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView newsrecyclerView;
    FirebaseRecyclerAdapter adapter;
    ScaleAnimation shrinkAnim;
    String reference = null;
    CategoryAdapter.ListItemClickListner listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);

        setTitle("Divya Hindi-इसका हर पन्ना हिरा पन्ना ");
        String imageurl, texturl;
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference mDatabaseReference = database.getReference();

        newsrecyclerView = (RecyclerView) findViewById(R.id.newsrecyclerview);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        newsrecyclerView.setLayoutManager(layoutManager);

        newsrecyclerView.addItemDecoration(new MainActivityTabVersion.GridSpacingItemDecoration(2, dpToPx(5), true));

        int resId = R.anim.grid_layout_animation_from_bottom;

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);

        Context context;

        newsrecyclerView.setLayoutAnimation(animation);

        reference = getIntent().getStringExtra("path");

        newsrecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance()

                .getReference().child("Akola/divyahindi");

        FirebaseRecyclerOptions<NewsItem> options =

                new FirebaseRecyclerOptions.Builder<NewsItem>()

                        .setQuery(query, NewsItem.class)

                        .build();

        adapter = (new FirebaseRecyclerAdapter<NewsItem, NewsActivity.ItemViewHolder>(options) {

            @Override

            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.newsview, parent, false);

                return new ItemViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder viewHolder, int position, @NonNull final NewsItem items) {
                viewHolder.pagetxt.setText(items.getNewspage());
                Log.e("News Activity", items.getNewspage());
                Picasso.with(getApplicationContext()).load(items.getNewsimage()).networkPolicy(NetworkPolicy.OFFLINE).into(viewHolder.Image, new Callback() {
                            @Override

                            public void onSuccess() {

                            }

                            @Override

                            public void onError() {

                                Picasso.with(NewsActivity.this).load(items.getNewsimage()).into(viewHolder.Image);

                            }

                        }

                );

                viewHolder.setItemOnClickListener(new ItemClickListener() {
                    public void onItemClick(int pos) {
                        Intent i = new Intent(NewsActivity.this.getApplicationContext(), NewsReadActivity.class);
                        i.putExtra("newspage", items.getNewsimage());
                        i.putExtra("pagenumber", items.getNewspage());
                        NewsActivity.this.startActivity(i);

                    }
                });
            }
        });


        newsrecyclerView.setAdapter(adapter);
    }

    private int dpToPx(int dp) {

        Resources r = getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemClickListener itemClickListener;

        ImageView Image;

        TextView pagetxt;

        public ItemViewHolder(View v) {
            super(v);

            this.Image = (ImageView) v.findViewById(R.id.pageimage);

            this.pagetxt = (TextView) v.findViewById(R.id.pagenumber);

            v.setOnClickListener(this);

        }


        public void setItemOnClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(getLayoutPosition());
        }

    }

    @Override
    protected void onStart() {

        super.onStart();

        adapter.startListening();

    }

    @Override

    protected void onStop() {

        super.onStop();

        adapter.startListening();

    }
}


