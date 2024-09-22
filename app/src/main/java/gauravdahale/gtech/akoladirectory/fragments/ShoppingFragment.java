package gauravdahale.gtech.akoladirectory.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;

import gauravdahale.gtech.akoladirectory.models.ContactModel;
import gauravdahale.gtech.akoladirectory.activity.EmergencyActivity;
import gauravdahale.gtech.akoladirectory.ItemClickListener;
import gauravdahale.gtech.akoladirectory.R;
import gauravdahale.gtech.akoladirectory.activity.ShopListActivity;


public class ShoppingFragment extends Fragment {
    RecyclerView mRecyclerView;
    ScaleAnimation shrinkAnim;
    private ProgressBar tvNoMovies;
FirebaseRemoteConfig mRemoteConfig;
    FirebaseAnalytics mAnalytics;
    FirebaseRecyclerAdapter adapter;
public String background;
    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Category")
                .child("Shopping");

        FirebaseRecyclerOptions<ContactModel> options =
                new FirebaseRecyclerOptions.Builder<ContactModel>()
                        .setQuery(query, ContactModel.class)
                        .build();
        adapter = (new FirebaseRecyclerAdapter<ContactModel, HealthFragment.ItemViewHolder>(options) {


            @Override
            public HealthFragment.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.categoryview, parent, false);
                return new HealthFragment.ItemViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull HealthFragment.ItemViewHolder viewHolder, int position, @NonNull final ContactModel items) {
                final String address = items.getN();
                final String title = items.getD();
                viewHolder.catname.setText(items.getC());
                Picasso.with(getActivity()).load(items.getI()).into(viewHolder.catimage);
                viewHolder.setItemOnClickListener(new ItemClickListener() {

                    public void onItemClick(int position) {
                        if(items.getT()==null){
                            Intent i = new Intent(getActivity(), ShopListActivity.class);
                            i.putExtra("Title", address);
                            i.putExtra("Settitle", title);
                            startActivity(i);


                        }else{
                            Intent i = new Intent(getActivity(), EmergencyActivity.class);
                            i.putExtra("Title", address);
                            i.putExtra("Settitle", title);
                            startActivity(i);

                        }


                    }
                });
            }
        }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shopping, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.shoppingrecycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new HealthFragment.GridSpacingItemDecoration(2, dpToPx(5), true));




        mRecyclerView.setAdapter(this.adapter);


        return v;


    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener itemClickListener;
        public ImageView catimage;
        public TextView catname;

        public ItemViewHolder(View v) {
            super(v);
            this.catimage = (ImageView) v.findViewById(R.id.categoryimage);
            this.catname = (TextView) v.findViewById(R.id.cattxtname);
            v.setOnClickListener(this);

        }

        public void setItemOnClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(getLayoutPosition());
        }

    }

    //DP TO PX METHOD
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        adapter.startListening();
        super.onResume();
    }
}