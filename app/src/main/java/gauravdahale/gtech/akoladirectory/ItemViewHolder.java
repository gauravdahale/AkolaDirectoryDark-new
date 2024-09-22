package gauravdahale.gtech.akoladirectory;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public  class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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