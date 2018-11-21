package miguelalvim.trab1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PessoaViewActivity extends AppCompatActivity {
    EditText txtCPF,txtName,txtEmail;
    Button bttEdit,bttSalvar,bttAdd;
    ListView lsEventos;
    ArrayAdapter<String> aaEventAdapter;
    ArrayList<Evento> eventsP = new ArrayList<>();
    ArrayList<Evento> eventsG = new ArrayList<>();
    ArrayList<String> eventsNames = new ArrayList<>();
    ArrayList<Integer> eventsIds = new ArrayList<>();
    int pos=-1;

    DBHandler bdHandler;
    SQLiteDatabase bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa_view);

        bttSalvar = findViewById(R.id.bttSalvar);
        bttEdit = findViewById(R.id.bttEdit);
        bttAdd = findViewById(R.id.bttAdd);
        txtCPF = findViewById(R.id.txtCPF);
        txtName = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtemail);
        txtCPF.setEnabled(false);
        txtName.setEnabled(false);
        txtEmail.setEnabled(false);

        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            @SuppressLint("Recycle") Cursor c = bd.rawQuery("SELECT name,cpf,email FROM pessoa WHERE id=" + extras.getInt("id", -1), null);
            c.moveToFirst();
            txtName.setText(c.getString(c.getColumnIndex("name")));
            txtCPF.setText(c.getString(c.getColumnIndex("cpf")));
            txtEmail.setText(c.getString(c.getColumnIndex("email")));
        }else{
            finish();
        }

        lsEventos = findViewById(R.id.lsEventos);
        aaEventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, eventsNames);
        lsEventos.setAdapter(aaEventAdapter);
        aaEventAdapter.notifyDataSetChanged();
        updateEventList();

        bttEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PessoaViewActivity.this,CadastroPessoaActivity.class);
                intent.putExtra("cpf", txtCPF.getText().toString());
                intent.putExtra("name", txtName.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());
                intent.putExtra("id", getIntent().getExtras().getInt("id",-1));
                startActivityForResult(intent,0);//Request code 0 = Modificacao de pessoa
            }
        });
        bttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PessoaViewActivity.this,CadastroPessoaEventoActivity.class);
                ArrayList<String> aux = new ArrayList<>();
                for(int i=0;i<eventsG.size();++i)
                    aux.add(eventsG.get(i).titulo);
                intent.putExtra("eventosG",aux);
                intent.putExtra("id",getIntent().getExtras().getInt("id",-1));
                startActivityForResult(intent,1);//Request code 1 = Adicao de evento
            }
        });
        bttSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtCPF.getText().toString().isEmpty() &&
                        !txtName.getText().toString().isEmpty() &&
                        !txtEmail.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra("email", txtEmail.getText().toString());
                    result.putExtra("CPF", txtCPF.getText().toString());
                    result.putExtra("name", txtName.getText().toString());
                    result.putExtra("pos", pos);
                    result.putExtra("eventsP", eventsP);
                    setResult(RESULT_OK, result);
                    finish();
                }else{
                    Toast.makeText(PessoaViewActivity.this, "Há dados não inseridos",  Toast.LENGTH_SHORT).show();
                }
            }
        });
        lsEventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bd.delete("pessoaevento", "id_evento="+eventsIds.get(position)+
                        " AND id_pessoa="+getIntent().getExtras().getInt("id",-1), null);
                updateEventList();
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0 :{//Modificação de Pessoa
                if (resultCode == Activity.RESULT_OK){
                    @SuppressLint("Recycle") Cursor c = bd.rawQuery("SELECT name,cpf,email FROM pessoa WHERE id=" + getIntent().getExtras().getInt("id", -1), null);
                    c.moveToFirst();
                    txtName.setText(c.getString(c.getColumnIndex("name")));
                    txtCPF.setText(c.getString(c.getColumnIndex("cpf")));
                    txtEmail.setText(c.getString(c.getColumnIndex("email")));
                }
            }break;
            case 1 :{//Adicao de Evento
                if (resultCode == Activity.RESULT_OK){
                    updateEventList();
                }
            }break;
        }
    }
    void updateEventList(){
        eventsNames.clear();
        eventsIds.clear();
        int pessoaID =getIntent().getExtras().getInt("id",-1);
        @SuppressLint("Recycle") Cursor c = bd.rawQuery("SELECT evp.id_evento,ev.titulo FROM evento ev, pessoa p, pessoaevento evp " +
                                    "WHERE ev.id=evp.id_evento AND p.id = evp.id_pessoa AND p.id="+pessoaID, null);
        if (c!=null && c.moveToFirst()){
            do {
                int id = Integer.parseInt(c.getString(c.getColumnIndex("id_evento")));
                String titulo = c.getString(c.getColumnIndex("titulo"));
                eventsNames.add(titulo);
                eventsIds.add(id);
            } while(c.moveToNext());
        }
        aaEventAdapter.notifyDataSetChanged();
    }
}
