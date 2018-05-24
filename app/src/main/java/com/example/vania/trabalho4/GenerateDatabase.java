package com.example.vania.trabalho4;

import android.content.Context;
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
        String s ="CREATE TABLE despesas(_idDespesas integer primary key autoincrement, tipoDespesa varchar(40), valorDespesa double, dataDespesa varchar(10), horaDespesa varchar(5), anexoDespesa varchar(255))";
        db.execSQL(s);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS despesas");
        onCreate(db);
    }
}
