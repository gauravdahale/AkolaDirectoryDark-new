package gauravdahale.gtech.akoladirectory;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gauravdahale.gtech.akoladirectory.models.DataModel;

public class CustomAdapter extends ArrayAdapter<DataModel> {
    public CustomAdapter(Context context, ArrayList<DataModel> data) {
        super(context, 0, data);
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemview = convertView;
        if (convertView == null) {
            listitemview = LayoutInflater.from(getContext()).inflate(R.layout.cards_layout, parent, false);
        }
        DataModel currentdata =getItem(position);
        ((TextView) listitemview.findViewById(R.id.textViewName)).setText(currentdata.getCategory());
        ((ImageView) listitemview.findViewById(R.id.card_imageView)).setImageResource(currentdata.getmImage());
        return listitemview;
    }
}
