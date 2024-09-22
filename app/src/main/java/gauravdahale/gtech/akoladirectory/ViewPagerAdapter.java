/*
package gauravdahale.gtech.akoladirectory;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    private Integer[] images = new Integer[]{Integer.valueOf(R.drawable.indianelectronics),Integer.valueOf(R.drawable.hushe),Integer.valueOf(R.drawable.ishwardas), Integer.valueOf(R.drawable.mobilemagic), Integer.valueOf(R.drawable.mobilemagic)};
    private LayoutInflater layoutInflater;
    private ScaleType mscaletype;

    public ViewPagerAdapter(Context context)
    {
        this.context = context;
    }

    public int getCount()
    {
        return this.images.length;
    }

    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = this.layoutInflater.inflate(R.layout.custom_view_pager, null);
        ImageView imageview = (ImageView) view.findViewById(R.id.bannerview);
        imageview.setImageResource(this.images[position].intValue());
        //imageview.setScaleType(ScaleType.FIT_START);
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
*/
