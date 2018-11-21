package miguelalvim.trab1;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "semanaComp";
    private static final int DATABASE_VERSION = 1;

    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(
                "CREATE TABLE pessoa (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT, cpf TEXT, email TEXT)"
        );

        ContentValues vals = new ContentValues();
        vals.put("email", "Jose@ice.com");
        vals.put("cpf", "12345678901");
        vals.put("name", "Jose");
        db.insert("pessoa", null, vals);

        db.execSQL(
                "CREATE TABLE evento (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "titulo TEXT, dia TEXT, hora TEXT, facilitador TEXT, descricao TEXT)"
        );
        vals.clear();
        vals.put("dia", "12/23");
        vals.put("descricao", "alguma coisa");
        vals.put("titulo", "evento1");
        vals.put("hora", "13:09");
        vals.put("facilitador", "ze das cove");
        db.insert("evento", null, vals);
        db.execSQL(
                "CREATE TABLE pessoaevento (id_pessoa INTEGER, id_evento INTEGER, " +
                        "FOREIGN KEY(id_pessoa) REFERENCES pessoa(id)," +
                        "FOREIGN KEY(id_evento) REFERENCES evento(id)," +
                        "PRIMARY KEY(id_pessoa, id_evento))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                "DROP TABLE IF EXISTS pessoa"
        );
        db.execSQL(
                "DROP TABLE IF EXISTS evento"
        );
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
