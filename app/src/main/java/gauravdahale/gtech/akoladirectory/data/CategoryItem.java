package gauravdahale.gtech.akoladirectory.data;

/**
 * Created by jayda on 9/20/2017.
 */

public class CategoryItem {
private String catName;
private int catimage;

    public CategoryItem(String catName, int catimage) {
        this.catName = catName;
        this.catimage = catimage;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatimage() {
        return catimage;
    }

    public void setCatimage(int catimage) {
        this.catimage = catimage;
    }
}
