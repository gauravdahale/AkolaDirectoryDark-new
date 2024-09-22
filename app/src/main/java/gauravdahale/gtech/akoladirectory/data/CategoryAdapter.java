package gauravdahale.gtech.akoladirectory.data;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import gauravdahale.gtech.akoladirectory.activity.ContactUsActivity;
import gauravdahale.gtech.akoladirectory.activity.EmergencyActivity;
import gauravdahale.gtech.akoladirectory.activity.NewsActivity;
import gauravdahale.gtech.akoladirectory.R;
import gauravdahale.gtech.akoladirectory.activity.RecordListActivity;
//import gauravdahale.gtech.akoladirectory.activity.RegisterActivity;

/**
 * Created by jayda on 9/20/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context mContext;
    private List<CategoryItem> categorylist;
    ListItemClickListner mOnclicklistener;

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView catimage;
        public TextView catname;

        public CategoryHolder(View view) {
            super(view);
            catname = (TextView) view.findViewById(R.id.catname);
            catimage = (ImageView) view.findViewById(R.id.catimage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i;
            int pos = getAdapterPosition();
            switch (pos) {
                case 0:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Hospital");
                    i.putExtra("Settitle", "Hospitals");
                    mContext.startActivity(i);
                    return;

                    case 1:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Medical");
                        i.putExtra("Settitle", "Medicals");
                    mContext.startActivity(i);
                    return;

                case 2:
                    i = new Intent(mContext, NewsActivity.class);
                    i.putExtra("Title", "divyahindi");
                    i.putExtra("Settitle", "Divya Hindi Newspaper");
                    mContext.startActivity(i);
                    return;

                    case 3:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "Emergency");
                        i.putExtra("Settitle", "Emergency Services");
                    mContext.startActivity(i);
                    return;
                case 4:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "CoachingClasses");
                    i.putExtra("Settitle", "Coaching Classes");
                    mContext.startActivity(i);
                    return;

                case 5:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Music");
                    i.putExtra("Settitle", "Music and Dance Classes");
                    mContext.startActivity(i);
                    return;


                case 6:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "mobilestore");
                    i.putExtra("Settitle", "Mobile Stores");
                    mContext.startActivity(i);
                    return;
                case 7:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "ComputerStores");
                    i.putExtra("Settitle", "Computer Stores");
                    mContext.startActivity(i);
                    return;
                case 8:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "HomeAppliances");
                    i.putExtra("Settitle", "Home Appliances & Electronics");
                    mContext.startActivity(i);
                    return;
                case 9:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "electronics");
                    i.putExtra("Settitle", "Electrical Stores");
                    mContext.startActivity(i);
                    return;

                case 10:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "schools");
                    i.putExtra("Settitle", "School");
                    mContext.startActivity(i);
                    return;

                case 11:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "College");
                    i.putExtra("Settitle", "Colleges");
                    mContext.startActivity(i);
                    return;

                case 12:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Banks");
                    i.putExtra("Settitle", "Banks");
                    mContext.startActivity(i);

                    return;

                case 13:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Bakery");
                    i.putExtra("Settitle", "Bakery and Cakes");
                    mContext.startActivity(i);
                    return;

                case 14:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Food");
                    i.putExtra("Settitle", "Restaurants & Bars");
                    mContext.startActivity(i);
                    return;
                case 15:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Clothes");
                    i.putExtra("Settitle", "Apparels and Clothes");
                    mContext.startActivity(i);
                    return;
                case 16:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "FurnitureandInterior");
                    i.putExtra("Settitle", "Furniture and Interior");
                    mContext.startActivity(i);
                    return;

                case 17:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Hardware");
                    i.putExtra("Settitle", "Hardware Stores");
                    mContext.startActivity(i);
                    return;
                case 18:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Paints");
                    i.putExtra("Settitle", "Paints and Colors");
                    mContext.startActivity(i);
                    return;

                case 19:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Jewellery");
                    i.putExtra("Settitle", "Jewellery Stores");
                    mContext.startActivity(i);
                    return;
                case 20:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Automobile");
                    i.putExtra("Settitle", "Automobiles ");
                    mContext.startActivity(i);
                    return;
                case 21:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Fabrication");
                    i.putExtra("Settitle", "Fabrication");
                    mContext.startActivity(i);
                    return;


                case 22:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "Electrician");
                    i.putExtra("Settitle", "Electrician");
                    mContext.startActivity(i);
                    return;

                case 23:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "Carpenter");
                    i.putExtra("Settitle", "Carpenter");
                    mContext.startActivity(i);
                    return;
                case 24:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "Plumber");
                    i.putExtra("Settitle", "Plumbers");
                    mContext.startActivity(i);
                    return;
                case 25:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "Painter");
                    i.putExtra("Settitle", "Painters");
                    mContext.startActivity(i);
                    return;
                case 26:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "Devolopers");
                    i.putExtra("Settitle", "Construction & Developers");
                    mContext.startActivity(i);
                    return;
                case 27:
                    i = new Intent(mContext, EmergencyActivity.class);
                    i.putExtra("Title", "SarpMitra");
                    i.putExtra("Settitle", "Sarpmitra");
                    mContext.startActivity(i);
                    return;
                case 28:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Tattoo");
                    i.putExtra("Settitle", "Tattoo's and Fashion Acceseries");
                    mContext.startActivity(i);
                    return;
                case 29:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Perfumes");
                    i.putExtra("Settitle", "Perfume Stores ");
                    mContext.startActivity(i);
                    return;

                    case 30:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "DesignerBangles");
                    i.putExtra("Settitle", "Designer Bangles");
                    mContext.startActivity(i);
                    return;


                case 31:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Insurance");
                    i.putExtra("Settitle", "Insurance Services");
                    mContext.startActivity(i);
                    return;

                case 32:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Bichayat");
                    i.putExtra("Settitle", "Bichayat");
                    mContext.startActivity(i);
                    return;

                case 33:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "GiftCenters");
                    i.putExtra("Settitle", "Gift Centers");
                    mContext.startActivity(i);
                    return;
                case 34:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "PhotoStudioMedia");
                    i.putExtra("Settitle", "Photo Studios");
                    mContext.startActivity(i);
                    return;
                case 35:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Printing");
                    i.putExtra("Settitle", "Printing and Offset");
                    mContext.startActivity(i);
                    return;

                case 36:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "TravelsandTourism");
                    i.putExtra("Settitle", "Travels and Tourism");
                    mContext.startActivity(i);
                    return;
                case 37:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Sports");
                    i.putExtra("Settitle", "Sports and Fitness");
                    mContext.startActivity(i);
                    return;

                case 38:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Utensils");
                    i.putExtra("Settitle", "Kitchenware");
                    mContext.startActivity(i);
                    return;



                case 39:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Vegetables");
                    i.putExtra("Settitle", "Vegetables");
                    mContext.startActivity(i);
                    return;

                case 40:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Agriculture");
                    i.putExtra("Settitle", "Agriculture");
                    mContext.startActivity(i);
                    return;

                case 41:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Cosmetics");
                    i.putExtra("Settitle", "Cosmetics and Imitations");
                    mContext.startActivity(i);
                    return;
                case 42:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "ESeva");
                    i.putExtra("Settitle", "Online Services");
                    mContext.startActivity(i);
                    return;
                case 43:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "SuperMarkets");
                    i.putExtra("Settitle", "Supermarkets and Grocery");
                    mContext.startActivity(i);
                    return;

                case 44:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Parlour");
                    i.putExtra("Settitle", "Parlors and Salons");
                    mContext.startActivity(i);
                    return;
                case 45:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Opticals");
                    i.putExtra("Settitle", "Opticals and Glasses");
                    mContext.startActivity(i);
                    return;

                case 46:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Hotels");
                    i.putExtra("Settitle", "Hotels and Lodge");
                    mContext.startActivity(i);
                    return;

                case 47:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Aluminium");
                    i.putExtra("Settitle", "Aluminium Works");
                    mContext.startActivity(i);
                    return;

                case 48:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Construction");
                    i.putExtra("Settitle", "Construction and Buildings");
                    mContext.startActivity(i);
                    return;

                case 49:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "RoServices");
                    i.putExtra("Settitle", "RO Services");
                    mContext.startActivity(i);
                    return;

                case 50:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Tax");
                    i.putExtra("Settitle", "Tax Consultants");
                    mContext.startActivity(i);
                    return;
                case 51:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "CCTV");
                    i.putExtra("Settitle", "CCTV and EPBAX Services");
                    mContext.startActivity(i);
                    return;
                case 52:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "DTH");
                    i.putExtra("Settitle", "DTH Services");
                    mContext.startActivity(i);
                    return;

                case 53:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Hostels");
                    i.putExtra("Settitle", "Hostles");
                    mContext.startActivity(i);
                    return;

                case 54:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Distributors");
                    i.putExtra("Settitle", "Wholesale and Distributors");
                    mContext.startActivity(i);
                    return;

                case 55:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Arts");
                    i.putExtra("Settitle", "Arts and Music");
                    mContext.startActivity(i);
                    return;

                case 56:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Solar");
                    i.putExtra("Settitle", "Solar");
                    mContext.startActivity(i);
                    return;

                case 57:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Transport");
                    i.putExtra("Settitle", "Transport and Courier ");
                    mContext.startActivity(i);
                    return;
                    case 58:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Internet");
                        i.putExtra("Settitle", "Internet Providers");
                    mContext.startActivity(i);
                    return;
                case 59:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Boutique");
                    i.putExtra("Settitle", "Boutiques ");
                    mContext.startActivity(i);
                    return;

                case 60:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Security");
                    i.putExtra("Settitle", "Security Services");
                    mContext.startActivity(i);
                    return;

                case 61:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "GruhUdyog");
                    i.putExtra("Settitle","Gruh Udyog");
                    mContext.startActivity(i);
                    return;


                case 62:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Dairy");
                    i.putExtra("Settitle","Dairy");
                    mContext.startActivity(i);
                    return;


                case 63:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Mess");
                    i.putExtra("Settitle","Mess");
                    mContext.startActivity(i);
                    return;


                case 64:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Finance");
                    i.putExtra("Settitle","Finance");
                    mContext.startActivity(i);
                    return;

                case 65:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "Septic");
                    i.putExtra("Settitle","Septic Tanks");
                    mContext.startActivity(i);
                    return;

                case 66:
                    i = new Intent(mContext, RecordListActivity.class);
                    i.putExtra("Title", "ShoesandSandals");
                    i.putExtra("Settitle", "Footwear");
                    mContext.startActivity(i);
                    return;

                case 67:
                    i = new Intent(mContext, ContactUsActivity.class);
                    i.putExtra("Title", "Contact Us");
                        i.putExtra("Settitle", "Contact Us");
                    mContext.startActivity(i);
                    return;

                case 68:
                    Intent share = new Intent("android.intent.action.SEND");
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, "आपल्या अकोल्याची अकोला डिरेक्टरी अँप आजच डाउनलोड करा: https://play.google.com/store/apps/details?id=gauravdahale.gtech.akoladirectory ");
                    share.putExtra("android.intent.extra.SUBJECT", "डाउनलोड करा अकोला शहराची एक नंबर अँप");
                    mContext.startActivity(Intent.createChooser(share, "Akola Directory App"));
                    return;
                case 69:
//                    i = new Intent(mContext, RegisterActivity.class);
//                    i.putExtra("Title", "Contact Us");
//                    i.putExtra("Settitle", "Business Registration");
//                    mContext.startActivity(i);
                    return;

                default:
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    return;
            }

        }
    }

    public CategoryAdapter(Context mContext, List<CategoryItem> categorylist, ListItemClickListner listener) {
        this.mContext = mContext;
        this.categorylist = categorylist;
        this.mOnclicklistener = listener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.catcardview, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, int position) {
        CategoryItem categoryItem = categorylist.get(position);
        holder.catname.setText(categoryItem.getCatName());
//Loading category image with glide library
        Glide.with(mContext).load(categoryItem.getCatimage()).into(holder.catimage);

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    //Interface to recieve on click messages
    public interface ListItemClickListner {
        void OnListiItemClick(int clickeditemindex);

    }
}


