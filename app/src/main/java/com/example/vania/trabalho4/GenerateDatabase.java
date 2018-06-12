package com.example.vania.trabalho4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenerateDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "baseDados.db";
    private static final int VERSION = 9;

    public GenerateDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_expenses = "CREATE TABLE despesas(_idDespesa integer primary key autoincrement, _idUser integer, tipoDespesa varchar(40), valorDespesa double, dataDespesa varchar(10), horaDespesa varchar(5), anexoDespesa BLOB)";

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
}
