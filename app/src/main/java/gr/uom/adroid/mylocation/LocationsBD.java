package gr.uom.adroid.mylocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class LocationsBD extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Locations.db";
    public static final String TABLE_NAME = "Locations_table";
    public static final String ID = "ID";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "LAT";
    public static final String COL_3 = "ING";



    public LocationsBD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT, LAT TEXT, ING TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String name, String lat, String ing){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(COL_1,name);
        contentvalues.put(COL_2,lat);
        contentvalues.put(COL_3,ing);
        long result = db.insert(TABLE_NAME,null, contentvalues);
        if (result == (-1)) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " +TABLE_NAME,null);
        return result;

    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }


}
