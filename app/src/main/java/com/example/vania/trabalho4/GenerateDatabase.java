package com.example.vania.trabalho4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenerateDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "baseDados.db";
    private static final int VERSION = 1;

    public GenerateDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_expenses = "CREATE TABLE despesas(_idDespesas integer primary key autoincrement, tipoDespesa varchar(40), valorDespesa double, dataDespesa varchar(10), horaDespesa varchar(5), anexoDespesa varchar(255))";

        String create_table_users = "CREATE TABLE users (id integer primary key autoincrement, username varchar(40), email varchar(255), password varchar(255))";


        String[] create_table = new String[]{create_table_expenses, create_table_users};

        for(String sql : create_table){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String drop_table_expenses = "DROP TABLE IF EXISTS despesas";

        String drop_table_users = "DROP TABLE IF EXISTS users";

        String[] drop_table = new String[]{drop_table_expenses, drop_table_users};

        for(String sql : drop_table){
            db.execSQL(sql);
        }
        onCreate(db);
    }




    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put("username", user.userName);
        values.put("email", user.email);
        values.put("password", user.password);

        long todo_id = db.insert("users", null, values);
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"id", "name", "email", "password"},
                "email" + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users",// Selecting Table
                new String[]{"id", "name", "email", "password"},
                "email" + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {

            return true;
        }

        return false;
    }

}

