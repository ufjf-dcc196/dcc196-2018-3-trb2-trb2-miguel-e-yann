package miguelalvim.trab1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class EventoViewActivity extends AppCompatActivity {
    EditText txtHora,txtDia,txtFacilitador,txtTitulo,txtDescricao;
    Evento event;
    ListView lvParticipantes;
    ArrayAdapter<String> aaLista;
    ArrayList<String> people;

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

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            people = extra.getStringArrayList("people");
            event = extra.getParcelable("event");
            txtHora.setText(event.hora);
            txtDia.setText(event.dia);
            txtFacilitador.setText(event.facilitador);
            txtTitulo.setText(event.titulo);
            txtDescricao.setText(event.descricao);
        }else{
            finish();
        }

        aaLista = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, people);
        lvParticipantes.setAdapter(aaLista);
        aaLista.notifyDataSetChanged();

    }
}
