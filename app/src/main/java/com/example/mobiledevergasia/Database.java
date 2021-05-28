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
 name: το όνομα του στοιχείου
 background: το χρώμα του background
 backgroundcheck: λειτουργεί ως FLAG για το ενδεχόμενο της αλλαγής του background
 textcolor: το χρώμα του κειμένου
 path: το path της ηχογράφησης

 **/

public class Database extends SQLiteOpenHelper {

    public Database(Context context){
        super(context,"Entries.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Entries(name TEXT primary key,background NUMBER,textcolor NUMBER,backgroundcheck NUMBER,path TEXT not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Entries");

    }

    /**
     Προσθέτει ένα νέο αντικείμενο στη βάση
     @param item:το στοιχείο με το σύνολο πληροφοριών για τη νέα προσθήκη στη βάση

     **/
    public Boolean newEntry(CustomItem item) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item.getName());
        contentValues.put("background", item.getBackgroundColor());
        contentValues.put("textcolor",item.getTextColor());
        contentValues.put("path", item.getPath());
        contentValues.put("backgroundcheck",0);
        long result = DB.insert("Entries", null, contentValues);
        return result != -1;
    }
    /**
     Αλλάζει το όνομα ενός στοιχείου στη βάση
     @param oldName:το ήδη υπάρχον όνομα του στοιχείο
     @param newName:το νέο όνομα που θα έχει το στοιχείο με name=oldName

     **/
    public Boolean updateName(String oldName,String newName){
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",newName);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{oldName});
        if (cursor.getCount()>0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{oldName});
            cursor.close();
            return result != -1;
        }else {
            return false;
        }

    }
    /**
     Αποθηκεύει το νέο χρώμα του background στη βάση
     @param name:το όνομα του στοιχείου στη βάση
     @param color:το νέο χρώμα του background του στοιχείου

     **/

    public Boolean updateBackground(String name, int color) {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("background",color);
        contentValues.put("backgroundcheck",1);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        }else {
            return false;
        }
    }

    /**
     Αποθηκεύει το νέο χρώμα του κειμένου στη βάση
     @param name:το όνομα του στοιχείου στη βάση
     @param color:το νέο χρώμα του κειμένου του στοιχείου

     **/
    public Boolean updateTextColor(String name, int color) {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("textcolor",color);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        }else {
            return false;
        }
    }
    /**
     * @param name:το όνομα του στοιχείου στη βάση
     * @param path:το νέο path του στοιχείου
     **/

    public Boolean updatePath(String name,String path) {
        System.out.println("mpika mpika mpika mpika mpika " + path);
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("path",path);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        System.out.println(" cursor    " + cursor.getCount());
        if (cursor.getCount()>=0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        }else {
            return false;
        }
    }

    /**
     Διαγράφη ένα αντικείμενο από τη βάση
     @param name:το όνομα του στοιχείου στη βάση

     **/
    public Boolean deleteEntry(String name) {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.delete("Entries", "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        }else {
            return false;
        }
    }

    /**
     * Κάνει reset τα χρώματα του background και text
     * @param name:το όνομα του στοιχείου στη βάση
     **/
    public Boolean reset(String name){
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("textcolor",Color.WHITE);
        contentValues.put("backgroundcheck",0);
        Cursor cursor=DB.rawQuery("Select * from Entries where name = ?",new String[]{name});
        if (cursor.getCount()>0) {
            long result = DB.update("Entries", contentValues, "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        }else {
            return false;
        }
    }

    /**
     Επιστρέφει μία λίστα όλων των αντικειμένων της βάσης

     **/
    public ArrayList<CustomItem> getItems(){
        ArrayList <CustomItem> items= new ArrayList<>();
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor =DB.rawQuery("Select * from Entries",null);
        while ((cursor.moveToNext())) {
            CustomItem item = new CustomItem(cursor.getString(4), cursor.getString(0));
            item.setTextColor(cursor.getInt(2));
            if(cursor.getInt(3)==1) {
                item.setBackgroundColor(Color.red(cursor.getInt(1)), Color.green(cursor.getInt(1)), Color.blue(cursor.getInt(1)));
            }
            items.add(item);
        }
        cursor.close();
        return items;
    }
}



