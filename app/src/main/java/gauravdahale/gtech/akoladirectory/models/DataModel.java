package gauravdahale.gtech.akoladirectory.models;

public class DataModel {
    private String mCategory;
    private int mId_;
    private int mImage;

    public DataModel(String category, int image) {
        this.mCategory = category;
        this.mImage = image;
    }

    public String getCategory() {
        return this.mCategory;
    }

    public int getmImage() {
        return this.mImage;
    }

    public int getmId_() {
        return this.mId_;
    }
}
