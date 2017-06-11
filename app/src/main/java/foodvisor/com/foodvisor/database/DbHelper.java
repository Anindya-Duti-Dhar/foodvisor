package foodvisor.com.foodvisor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private String TAG = DbHelper.class.getSimpleName();

    private static final String DB_NAME = "rent";
    // bookmark Information
    public static final String RENT_ID = "rentID" ;
    public static final String RENT_NAME = "rentName";
    public static final String RENT_ADDRESS = "rentAddress";
    public static final String RENT_PRICE = "rentPrice";
    public static final String RENT_DESCRIPTION = "rentDescription";
    public static final String RENT_UPLOADER = "rentUploader";
    public static final String RENT_UPLOADER_MOBILE = "rentUploaderMobile";
    public static final String RENT_UPLOADED_AT = "rentUploadedAt";
    public static final String RENT_IMAGE = "rentImage";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "bookmark";
    public static final String TABLE_STATEMENT = "create table "+TABLE_NAME +"(rentID TEXT,rentName TEXT, rentAddress TEXT, rentPrice TEXT, rentDescription TEXT, rentUploader TEXT, rentUploaderMobile TEXT, rentUploadedAt TEXT, rentImage TEXT)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "--- onCreate database ---");
        // statement for cart table
        db.execSQL(TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
