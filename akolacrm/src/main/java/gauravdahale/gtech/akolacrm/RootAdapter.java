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

import gauravdahale.gtech.akolacrm.Activity.ActivityCategory;
import gauravdahale.gtech.akolacrm.Activity.MainActivity;
import gauravdahale.gtech.akolacrm.Model.CategoryModel;

public class RootAdapter extends RecyclerView.Adapter<RootAdapter.CategoryHolder> {
    private static final String TAG = "RootAdapter";
    private static final String REF = "REFERENCE";
    private static final String TITLE = "Title";

    private List<CategoryModel> categorylist;
    private Context context;
    ItemClickListener listener;

    public RootAdapter(List<CategoryModel> categorylist, Context context) {
        this.categorylist = categorylist;
        this.context = context;
    }

    @NonNull
    @Override
    public RootAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorycardview, viewGroup, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RootAdapter.CategoryHolder holder, int i) {
        final CategoryModel categoryModel = categorylist.get(i);
        holder.categoryname.setText(categoryModel.getName());
        holder.setItemOnClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(context, ActivityCategory.class);
                intent.putExtra(REF, categoryModel.getName());
                intent.putExtra(TITLE,categoryModel.getName());
                intent.putExtra("CITY",categoryModel.getName());

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