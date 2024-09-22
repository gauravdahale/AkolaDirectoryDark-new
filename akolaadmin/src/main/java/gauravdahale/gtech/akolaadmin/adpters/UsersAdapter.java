package gauravdahale.gtech.akolaadmin.adpters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gauravdahale.gtech.akolaadmin.R;
import gauravdahale.gtech.akolaadmin.UserModel;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    private List<UserModel> mUserList;

    public UsersAdapter(List<UserModel> mUserList) {
        super();
        this.mUserList = mUserList;
    }


    @NonNull

    @Override
    public UsersAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercardview, parent,
                false);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserHolder holder, int position) {
UserModel user=mUserList.get(position);
holder.business.setText(user.getUserOccupation());
holder.name.setText(user.getUserName());
holder.phone.setText(user.getUserNumber());
holder.city.setText(user.getUserCity());
holder.date.setText(user.getDate());
   holder.sr.setText(position+1+""); }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView name, business, phone, city, date, sr;

        public UserHolder(View v) {
            super(v);
            this.name = v.findViewById(R.id.user);
            this.business = v.findViewById(R.id.userbusiness);
            this.phone = v.findViewById(R.id.userphone);
            this.date = v.findViewById(R.id.installdateview);
            this.sr = v.findViewById(R.id.usersr);
            this.city = v.findViewById(R.id.usercity);
        }
    }
}
