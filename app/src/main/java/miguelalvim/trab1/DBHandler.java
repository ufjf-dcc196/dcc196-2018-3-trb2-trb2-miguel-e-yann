package miguelalvim.trab1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "semanaComp";
    public static final int DATABASE_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(
                "CREATE TABLE pessoa (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT, cpf TEXT, email TEXT)"
        );
        db.execSQL(
                "CREATE TABLE evento (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "titulo TEXT, dia TEXT, hora TEXT, facilitador TEXT, descricao TEXT)"
        );
        db.execSQL(
                "CREATE TABLE pessoaevento (id_pessoa INTEGER, id_evento INTEGER, " +
                        "FOREIGN KEY(id_pessoa) REFERENCES pessoa(id)," +
                        "FOREIGN KEY(id_evento) REFERENCES evento(id)," +
                        "PRIMARY KEY(id_pessoa, id_evento))"
        );
        //Populate for DEBUG
        //db.rawQuery("INSERT INTO pessoa values('Miguel Alvim','12312312312','nope@nopes.n')",null);
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
