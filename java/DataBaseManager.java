package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;




public class DataBaseManager extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "CashCash.db";
    private static final int DATABASE_VERSION = 1;


    public DataBaseManager (Context context )
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Requête pour créer les différentes tables de la BDD
    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql1 = "create table controler ("
                + "    id integer primary key AUTOINCREMENT, "
                + "    numero_serie varchar not null, "
                + "    numero_intervention varchar not null,"
                + "    temps_passer varchar not null,"
                + "    commentaire varchar not null"
                + ")";
        db.execSQL( strSql1 );
        Log.i( "DATABASE", "onCreate invoked1" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Requête pour insérer les matériels vérifié
    public void insertMateriels (Controler c)
    {
        String numero_serie = c.getN_serie().replace("'", "''");
        String numero_intervention = c.getId_intervention().replace("'", "''");
        String temps_passer = c.getTemps().replace("'", "''");
        String commentaire = c.getCommentaire().replace("'", "''");
        String strSql = "INSERT INTO controler (numero_serie, numero_intervention, temps_passer, commentaire) VALUES ( " + numero_serie + ", ' " + numero_intervention + " ', ' " + temps_passer + "', '" + commentaire + "')";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE", "insertMateriel");
    }

    // Retourne une ArrayList avec tous les contrôle de la bdd SQLite
    public List<Controler> readLesControles(){
        List<Controler> lesControles = new ArrayList<>();
        String strSql = "SELECT * FROM controler";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Controler unControle = new Controler(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));
                lesControles.add(unControle);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return lesControles;
    }

    // Supprime les contrôle de la bdd SQLite
    public void deleteControle(Controler unControle) {
        String numSerie = unControle.getN_serie().replace("'", "''");
        String numIntervention = unControle.getId_intervention().replace("'", "''");
        String strSql = "DELETE FROM controler WHERE numero_serie = '" + numSerie + "' AND numero_intervention = '" + numIntervention + "'";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE", "delete");
    }

}
