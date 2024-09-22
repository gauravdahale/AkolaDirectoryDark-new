package gauravdahale.gtech.akolaadmin.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import gauravdahale.gtech.akolaadmin.R;
import gauravdahale.gtech.akolaadmin.RequestModel;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    private List<RequestModel> mRequestList;
    private String editremark;

    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Username;
        TextView Address;
        TextView Phone, Userphone, ntext, phtext, createdat, status;
        EditText remark;
        ImageButton sendbtn, delbtn;


        MyViewHolder(View v) {

            super(v);
            this.Name = (TextView) v.findViewById(R.id.shopname);
            this.Address = (TextView) v.findViewById(R.id.shopaddress);
            this.Userphone = (TextView) v.findViewById(R.id.userphone);
            this.Username = (TextView) v.findViewById(R.id.username);
            this.Phone = (TextView) v.findViewById(R.id.shopphone);
            this.phtext = (TextView) v.findViewById(R.id.textView3);
            this.ntext = (TextView) v.findViewById(R.id.textView);
            this.status = (TextView) v.findViewById(R.id.status);
            this.remark = (EditText) v.findViewById(R.id.editeremark);
            this.createdat = v.findViewById(R.id.createdat);
            this.sendbtn = v.findViewById(R.id.sendbtn);
            this.delbtn = v.findViewById(R.id.deletebutton);

        }
    }

    public RequestAdapter(List<RequestModel> mRequestList, Context context) {
        super();
        this.mRequestList = mRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestcard, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final RequestAdapter.MyViewHolder holder, final int position) {
        final RequestModel items = mRequestList.get(position);
      final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference(items.getRef());
        holder.Name.setText(items.getN());
        holder.Address.setText(items.getI());
        holder.Phone.setText(items.getP());
        holder.Username.setText(items.getI());
        holder.Userphone.setText(items.getD());
        holder.createdat.setText(items.getT());
        holder.status.setText(items.getS());


        holder.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> rem = new HashMap<>();
                editremark = holder.remark.getText().toString().trim();
                rem.put("status", editremark);
                mRef.getDatabase().getReference();

                mRef.updateChildren(rem);
Toast.makeText(context,"UPdated at "+items.getN(),Toast.LENGTH_LONG).show();
            }
        });
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.removeValue();

                Toast.makeText(context, "Deleted at"+items.getN(), Toast.LENGTH_LONG).show();
notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRequestList.size();
    }


}
