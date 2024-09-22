package gauravdahale.gtech.akoladirectory;

/**
 * Created by Gaurav on 2/4/2018.
 */

public class NewsItem {
    private String newsimage;
    private String newspage;

    public NewsItem() {
    }

    public NewsItem(String newsimage, String newpage) {
        this.newsimage = newsimage;
        this.newspage = newpage;
    }

    public String getNewsimage() {
        return newsimage;
    }

    public void setNewsimage(String newsimage) {
        this.newsimage = newsimage;
    }

    public String getNewspage() {
        return newspage;
    }

    public void setNewpage(String newpage) {
        this.newspage = newpage;
    }
}
