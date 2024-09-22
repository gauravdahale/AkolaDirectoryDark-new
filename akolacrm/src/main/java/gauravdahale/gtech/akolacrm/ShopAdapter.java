package gauravdahale.gtech.akolacrm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gauravdahale.gtech.akolacrm.Activity.RecordActivity;
import gauravdahale.gtech.akolacrm.Model.CategoryModel;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.CategoryHolder> {
    private static final String TAG = "ShopAdapter";
    private static final String REF = "REFERENCE";
    private static final String TITLE = "Title";

    private List<CategoryModel> categorylist;
    private Context context;
    ItemClickListener listener;

    public ShopAdapter(List<CategoryModel> categorylist, Context context) {
        this.categorylist = categorylist;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_card_view, viewGroup, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.CategoryHolder holder, int i) {
        final CategoryModel categoryModel = categorylist.get(i);
        holder.shopname.setText(categoryModel.getName());
        holder.shopkey.setText(categoryModel.getKey());

        holder.setItemOnClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(context, RecordActivity.class);

                intent.putExtra(REF, categoryModel.getName());
                intent.putExtra(TITLE,categoryModel.getKey()+">"+categoryModel.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView shopname,shopkey;
        ItemClickListener clickListener;

        CategoryHolder(@NonNull View v) {
            super(v);
            shopname = (TextView) itemView.findViewById(R.id.shopname);
            shopkey = (TextView) itemView.findViewById(R.id.shopkey);
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