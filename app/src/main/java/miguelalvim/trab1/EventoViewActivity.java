package miguelalvim.trab1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class EventoViewActivity extends AppCompatActivity {
    EditText txtHora,txtDia,txtFacilitador,txtTitulo,txtDescricao;
    ListView lvParticipantes;
    ArrayAdapter<String> aaLista;
    ArrayList<String> people = new ArrayList<>();
    DBHandler bdHandler;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_view);

        txtHora = findViewById(R.id.txtHora);
            txtHora.setEnabled(false);
        txtDia = findViewById(R.id.txtDia);
            txtDia.setEnabled(false);
        txtFacilitador = findViewById(R.id.txtFacilitador);
            txtFacilitador.setEnabled(false);
        txtTitulo = findViewById(R.id.txtTitulo);
            txtTitulo.setEnabled(false);
        txtDescricao = findViewById(R.id.txtDescricao);
            txtDescricao.setEnabled(false);
        lvParticipantes = findViewById(R.id.lsParticipantes);
        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            @SuppressLint("Recycle") Cursor c = bd.rawQuery("SELECT titulo,dia,hora,facilitador,descricao FROM evento WHERE id=" + extra.getInt("event", -1), null);
            c.moveToFirst();
            txtTitulo.setText(c.getString(c.getColumnIndex("titulo")));
            txtDia.setText(c.getString(c.getColumnIndex("dia")));
            txtHora.setText(c.getString(c.getColumnIndex("hora")));
            txtFacilitador.setText(c.getString(c.getColumnIndex("facilitador")));
            txtDescricao.setText(c.getString(c.getColumnIndex("descricao")));
        }else{
            finish();
        }

        aaLista = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, people);
        lvParticipantes.setAdapter(aaLista);
        aaLista.notifyDataSetChanged();
        updatePersonList();
    }

    void updatePersonList() {
        people.clear();
        int eventoID = getIntent().getExtras().getInt("event", -1);
        @SuppressLint("Recycle") Cursor c = bd.rawQuery("SELECT p.name FROM evento ev, pessoa p, pessoaevento evp " +
                "WHERE ev.id=evp.id_evento AND p.id = evp.id_pessoa AND ev.id=" + eventoID, null);
        if (c != null && c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex("name"));
                people.add(name);
            } while (c.moveToNext());
        }
        aaLista.notifyDataSetChanged();
    }
}
