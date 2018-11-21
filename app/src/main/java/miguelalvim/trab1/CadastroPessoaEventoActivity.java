package miguelalvim.trab1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CadastroPessoaEventoActivity extends AppCompatActivity {
    ListView ls;
    ArrayList<String> eventNames;
    ArrayList<Integer> eventIds;
    ArrayAdapter aaAdapter;
    int personID;

    DBHandler bdHandler;
    SQLiteDatabase bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa_cadastrar_evento);

        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        eventNames= new ArrayList<>();
        eventIds= new ArrayList<>();

        ls = findViewById(R.id.lvList);

        personID = getIntent().getExtras().getInt("id",-1);
        @SuppressLint("Recycle") Cursor c = bd.rawQuery("SELECT e.id,e.titulo FROM evento e WHERE NOT EXISTS " +
                                   "(SELECT * FROM pessoa p,pessoaevento pe WHERE e.id=pe.id_evento AND p.id=pe.id_pessoa AND p.id="+personID+")", null);
        if (c.moveToFirst()){
            do {
                int id = Integer.parseInt(c.getString(c.getColumnIndex("id")));
                String titulo = c.getString(c.getColumnIndex("titulo"));
                eventNames.add(titulo);
                eventIds.add(id);
            } while(c.moveToNext());
        }

        aaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, eventNames);
        ls.setAdapter(aaAdapter);
        aaAdapter.notifyDataSetChanged();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent();
                ContentValues vals = new ContentValues();
                vals.put("id_evento", eventIds.get(position));
                vals.put("id_pessoa", personID);
                long id_ = bd.insert("pessoaevento", null, vals);
                Log.i("DABDAB","Evento ="+eventIds.get(position) +" |Pessoa"+personID);
                Log.i("DABDAB","PessoaEvento Adicionado-> id="+id_);
                setResult(RESULT_OK, result);
                finish();
            }
        });

    }
}
