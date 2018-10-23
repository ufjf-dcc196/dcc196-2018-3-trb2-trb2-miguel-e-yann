package miguelalvim.trab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ArrayList<Evento> eventsP = new ArrayList<Evento>();
    ArrayList<Evento> eventsG = new ArrayList<Evento>();
    ArrayList<String> eventsNames = new ArrayList<String>();
    int pos=-1;

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

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            txtName.setText(extras.getString("name"));
            txtCPF.setText(extras.getString("cpf"));
            txtEmail.setText(extras.getString("email"));
            pos = extras.getInt("pos");
            eventsP = extras.getParcelableArrayList("eventosP");
            eventsG = extras.getParcelableArrayList("eventosG");
            for(int i = 0; i< eventsP.size(); ++i){
                eventsNames.add(eventsP.get(i).titulo);
            }
        }else{
            finish();
        }
        lsEventos = findViewById(R.id.lsEventos);
        aaEventAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, eventsNames);
        lsEventos.setAdapter(aaEventAdapter);
        aaEventAdapter.notifyDataSetChanged();

        bttEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PessoaViewActivity.this,CadastroPessoaActivity.class);
                intent.putExtra("cpf", txtCPF.getText().toString());
                intent.putExtra("name", txtName.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());
                startActivityForResult(intent,0);//Request code 0 = Modificacao de pessoa
            }
        });
        bttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PessoaViewActivity.this,CadastroPessoaEventoActivity.class);
                ArrayList<String> aux = new ArrayList<String>();
                for(int i=0;i<eventsG.size();++i)
                    aux.add(eventsG.get(i).titulo);
                intent.putExtra("eventosG",aux);
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
                eventsP.remove(position);
                eventsNames.remove(position);
                aaEventAdapter.notifyDataSetChanged();

                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0 :{//Modificação de Pessoa
                if (resultCode == Activity.RESULT_OK && data != null){
                    Person p = new Person();
                    p.cpf = data.getStringExtra("CPF");
                    p.email = data.getStringExtra("email");
                    p.name = data.getStringExtra("name");

                    txtName.setText(data.getStringExtra("name"));
                    txtEmail.setText(data.getStringExtra("email"));
                    txtCPF.setText(data.getStringExtra("CPF"));
                }
            }break;
            case 1 :{//Adicao de Evento
                if (resultCode == Activity.RESULT_OK && data != null){
                    int position = data.getIntExtra("pos", 0);
                    if(!eventsP.contains(eventsG.get(position))) {
                        eventsP.add(eventsG.get(position));
                        eventsNames.add(eventsG.get(position).titulo);
                        aaEventAdapter.notifyDataSetChanged();
                    }
                }
            }break;
        }
    }
}
