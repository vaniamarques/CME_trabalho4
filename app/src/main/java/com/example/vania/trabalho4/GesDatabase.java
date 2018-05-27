package com.example.vania.trabalho4;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class GesDatabase {
    private GenerateDatabase dbHelper;
    private SQLiteDatabase database;


    public GesDatabase(Context context) {
        dbHelper = new GenerateDatabase(context);
    }

    public GesDatabase open() {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertDespesa(String tipoDespesa, Double valorDespesa, String dataDespesa, String horaDespesa, String anexoDespesa){
        ContentValues valores;
        long resultado;

        database = dbHelper.getWritableDatabase();
        valores = new ContentValues();
        valores.put("tipoDespesa", tipoDespesa);
        valores.put("valorDespesa", valorDespesa);
        valores.put("dataDespesa", dataDespesa);
        valores.put("horaDespesa", horaDespesa);
        valores.put("anexoDespesa", anexoDespesa);

        resultado = database.insert("despesas", null, valores);
        database.close();

        if (resultado ==-1)
            return false;
        else
            return true;

    }

    public boolean updateDespesa(Integer _idDespesa, String tipoDespesa, Double valorDespesa, String dataDespesa, String horaDespesa, String anexoDespesa){
        String whereClause = "_idDespesa = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = new Integer(_idDespesa).toString();

        ContentValues valores = new ContentValues();
        valores.put("tipoDespesa", tipoDespesa);
        valores.put("valorDespesa", valorDespesa);
        valores.put("dataDespesa", dataDespesa);
        valores.put("horaDespesa", horaDespesa);
        valores.put("anexoDespesa", anexoDespesa);

        long resultado = database.update("despesas", valores, whereClause, whereArgs);


        if (resultado ==-1)
            return false;
        else
            return true;
    }



    public Cursor obterResumoDespesas() {
        String[] colunas = new String[4];
        colunas[0] = "_idDespesa";
        colunas[1] = "tipoDespesa";
        colunas[2] = "valorDespesa";
        colunas[3] = "dataDespesa";
        return database.query("despesas", colunas, null, null, null, null, null, null);
    }

    public Cursor obterTodosRegistos() {
        String[] colunas = new String[6];
        colunas[0] = "_idDespesa";
        colunas[1] = "tipoDespesa";
        colunas[2] = "valorDespesa";
        colunas[3] = "dataDespesa";
        colunas[4] = "horaDespesa";
        colunas[5] = "anexoDespesa";
        return database.query("despesas", colunas, null, null, null, null, null, null);
    }


    public Cursor obterDespesaEspecifica(Integer index){
        Cursor cursor = database.rawQuery(
                "select _idDespesa, tipoDespesa, valorDespesa, dataDespesa, horaDespesa, anexoDespesa from despesas where _idDespesa=?", new String[] { index.toString() });


        return cursor;

    }


    public void addUser(User user) {

        Log.d("Utilizador", user.email.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("username", user.userName);
        values.put("email", user.email);
        values.put("password", user.password);

        long todo_id = db.insert("users", null, values);
        db.close();
    }



    public Cursor obterTodosUsers() {
        String[] colunas = new String[6];
        colunas[0] = "id";
        colunas[1] = "username";
        colunas[2] = "email";
        colunas[3] = "password";
        return database.query("users", colunas, null, null, null, null, null, null);
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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