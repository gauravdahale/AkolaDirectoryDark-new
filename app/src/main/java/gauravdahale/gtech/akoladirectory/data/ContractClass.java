package gauravdahale.gtech.akoladirectory.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ContractClass {
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://gauravdahale.gtech.akoladirectory");
    public static final String CONTENT_AUTHORITY = "gauravdahale.gtech.akoladirectory";
    public static final String PATH_RECORD = "record";

    public static final class RecordEntry implements BaseColumns {
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_EMAILID = "description";
        public static final String COLUMN_IMAGE = "imageid";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_TITLE = "name";
        public static final String COLUMN_OWNER = "ownername";
        public static final String COLUMN_TIMING = "timing";
        public static final String COLUMN_TYPE = "category";
        public static final String TABLE_NAME = "record";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/gauravdahale.gtech.akoladirectory/record";
        public static final String CONTENT_LIST_TYPE = "vnd.android.cursor.dir/gauravdahale.gtech.akoladirectory/record";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ContractClass.BASE_CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_id";
    }

    private ContractClass() {
    }
}
