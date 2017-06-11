package foodvisor.com.foodvisor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import foodvisor.com.foodvisor.model.BookmarkItem;

import static foodvisor.com.foodvisor.database.DbHelper.TABLE_NAME;


public class DbManager {
    private static String TAG = DbManager.class.getSimpleName();

    private static DbManager instance;
    private Context mContext;

    private DbManager(Context context) {
        this.mContext = context;
    }

    public static DbManager getInstance(Context context) {
        if (instance == null) {
            instance = new DbManager(context);
        }

        return instance;
    }


    public void clearDB() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        dbHelper.close();
    }

    /* get  data from bookmark table */
    public List<BookmarkItem> getDataFromDB() {
        List<BookmarkItem> modelList = new ArrayList<BookmarkItem>();
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                BookmarkItem model = new BookmarkItem();
                model.setRentId(cursor.getString(0));
                model.setRentName(cursor.getString(1));
                model.setRentAddress(cursor.getString(2));
                model.setRentCost(cursor.getString(3));
                model.setRentDescription(cursor.getString(4));
                model.setRentUploader(cursor.getString(5));
                model.setRentUploaderMobile(cursor.getString(6));
                model.setRentUploadedAt(cursor.getString(7));
                model.setImageUrl(cursor.getString(8));
                Log.d("BookmarkData ID: ", cursor.getString(0));
                Log.d("BookmarkData Name:", cursor.getString(1));
                modelList.add(model);

            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        Log.d("bookmark data", modelList.toString());

        return modelList;
    }

    /* Insert into bookmark table*/
    public void insertIntoDB(String rentID,String rentName,String rentAddress, String rentCost, String rentDescription, String rentUploader, String rentUploaderMobile, String rentUploadedAt, String rentImage){
        Log.d("insert", "before insert");

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        // 1. get reference to writable DB
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        values.put(DbHelper.RENT_ID, rentID);
        values.put(DbHelper.RENT_NAME, rentName);
        values.put(DbHelper.RENT_ADDRESS, rentAddress);
        values.put(DbHelper.RENT_PRICE, rentCost);
        values.put(DbHelper.RENT_DESCRIPTION, rentDescription);
        values.put(DbHelper.RENT_UPLOADER, rentUploader);
        values.put(DbHelper.RENT_UPLOADER_MOBILE, rentUploaderMobile);
        values.put(DbHelper.RENT_UPLOADED_AT, rentUploadedAt);
        values.put(DbHelper.RENT_IMAGE, rentImage);

        // 3. insert
        db.insert(TABLE_NAME, null, values);
        // 4. close
        dbHelper.close();
        Log.i("insert into DB", "After insert");
    }

    //delete a row from cart table
    public void deleteARow(String rentID){
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "rentID" + " = ?", new String[] { rentID });
        db.close();
    }
}

