package gauravdahale.gtech.akoladirectory.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import gauravdahale.gtech.akoladirectory.data.ContractClass.RecordEntry;

public class ProviderClass extends ContentProvider {
    private static final int CATEGORY = 102;
    public static final String LOG_TAG = ProviderClass.class.getSimpleName();
    private static final int RECORD = 100;
    private static final int RECORD_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);


    static {
        sUriMatcher.addURI(ContractClass.CONTENT_AUTHORITY, ContractClass.PATH_RECORD, RECORD);
        sUriMatcher.addURI(ContractClass.CONTENT_AUTHORITY, "record/#", RECORD_ID);
    }




    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORD /*100*/:
                return RecordEntry.CONTENT_LIST_TYPE;
            case RECORD_ID /*101*/:
                return RecordEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
