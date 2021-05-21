package com.example.mobiledevergasia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;
/**
 Κλάση για τη δημιουργία βάσης δεδομένων και την διαχείριση των αντικειμένων της

 **/

public class Database extends SQLiteOpenHelper {

    public Database(Context context){
        super(context,"Userdata.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Entries(name TEXT primary key,background NUMBER,textcolor NUMBER,path TEXT not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Entries");

    }
    /**
     Προσθέτει ένα νέο αντικείμενο στη βάση

     **/
    public Boolean newentry(CustomItem item) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item.getDesc());
        contentValues.put("background", item.getBackgroundColor());
        contentValues.put("textcolor",item.getTextColor());
        contentValues.put("path", item.getPath());
        long result = DB.insert("Entries", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    /**
    Αποθηκεύει το νέο χρώμα του background στη βάση

     **/

    public Boolean updatebackground(String name, int color)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("background",color);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }
    /**
     Αποθηκεύει το νέο χρώμα του κειμένου στη βάση

     **/
    public Boolean updatetextcolor(String name, int color)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("textcolor",color);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }
    /**
    Διαγράφη ένα αντικείμενο από τη βάση

     **/

    public Boolean deleteentry(String name)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from Userdetails where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.delete("Entries", "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }
    /**
     Επιστρέφει μία λίστα όλων των αντικειμένων της βάσης

     **/
    public ArrayList getitems(){
        ArrayList <CustomItem> items= new ArrayList<>();
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor =DB.rawQuery("Select * from Entries",null);
        StringBuffer buffer=new StringBuffer();
        while ((cursor.moveToNext())) {
            CustomItem item = new CustomItem(cursor.getColumnName(0), cursor.getColumnName(3));
            item.setTextColor(Integer.parseInt(cursor.getString(2)));
            item.setBackgroundColor(Color.red(Integer.parseInt(cursor.getString(1))),Color.green(Integer.parseInt(cursor.getString(1))),Color.blue(Integer.parseInt(cursor.getString(1))));
            items.add(item);
        }
        return items;
    }
}

