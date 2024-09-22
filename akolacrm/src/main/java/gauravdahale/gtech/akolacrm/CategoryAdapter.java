package gauravdahale.gtech.akolacrm;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gauravdahale.gtech.akolacrm.Activity.ShopActivity;
import gauravdahale.gtech.akolacrm.Model.CategoryModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private static final String TAG = "CategoryAdapter";
    private static final String REF = "REFERENCE";
    private static final String TITLE = "Title";

    private List<CategoryModel> categorylist;
    private Context context;
    ItemClickListener listener;
    private String ref;
private  String city;
    public CategoryAdapter(List<CategoryModel> categorylist, Context context, String ref,String city) {
        this.categorylist = categorylist;
        this.context = context;
        this.ref = ref;
    this.city=city;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorycardview, viewGroup, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder holder, int i) {
        final CategoryModel categoryModel = categorylist.get(i);
        holder.categoryname.setText(categoryModel.getName());


        holder.setItemOnClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra(REF, ref+"/"+categoryModel.getName());
                intent.putExtra(TITLE, categoryModel.getName());
                intent.putExtra("CITY",city);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryname;
        ItemClickListener clickListener;

        CategoryHolder(@NonNull View v) {
            super(v);
            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            itemView.setOnClickListener(this);
        }

        void setItemOnClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.clickListener.onItemClick(getLayoutPosition());

        }
    }
}