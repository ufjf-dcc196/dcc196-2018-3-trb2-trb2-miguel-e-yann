package miguelalvim.trab1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Person> people = new ArrayList<>();
    ArrayList<String> peopleNames = new ArrayList<>();
    ArrayList<Evento> events = new ArrayList<>();
    ArrayList<String> eventsNames = new ArrayList<>();
    ArrayAdapter<String> aaPeopleAdapter;
    ArrayAdapter<String> aaEventAdapter;

    ListView lsPeopleView;
    ListView lsEventView;
    Button bttCadastrarPessoa,bttCadastrarEvento;


    DBHandler bdHandler;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lsPeopleView = findViewById(R.id.lsListaPessoas);
        lsEventView = findViewById(R.id.lsEvents);
        bttCadastrarEvento = findViewById(R.id.bttCadastrarEvento);
        bttCadastrarPessoa = findViewById(R.id.bttCadastrarPessoa);


        aaPeopleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, peopleNames);
        aaEventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, eventsNames);

        lsPeopleView.setAdapter(aaPeopleAdapter);
        lsEventView.setAdapter(aaEventAdapter);

        aaPeopleAdapter.notifyDataSetChanged();
        aaEventAdapter.notifyDataSetChanged();


        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        updateNamesList();

        bttCadastrarPessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CadastroPessoaActivity.class);
                startActivityForResult(intent,0);//Request code 0 = cadastro de pessoa
            }
        });
        bttCadastrarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CadastroEventoActivity.class);
                startActivityForResult(intent,1);//Request code 1 = cadastro de evento
            }
        });

        lsPeopleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,PessoaViewActivity.class);
                intent.putExtra("id", people.get(position).id);
                startActivityForResult(intent,2);//Request code 2 = tela de checagem de pessoa
            }
        });
        lsEventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EventoViewActivity.class);
                ArrayList<String> participants = new ArrayList<>();

                for(int i=0;i<people.size();++i){
                    for(int j=0;j<people.get(i).eventos.size();++j) {
                        if (people.get(i).eventos.get(j).titulo.equals(events.get(position).titulo)){
                            participants.add(people.get(i).name);
                        }
                    }
                }
                intent.putExtra("people", participants);
                intent.putExtra("event", events.get(position));

                startActivity(intent);
            }
        });
    }

    private Person createPerson(String name, String cpf, String email,int id){
        Person p = new Person();
        p.name = name;
        p.cpf = cpf;
        p.email = email;
        p.id = id;
        p.eventos = new ArrayList<>();
        return p;
    }
    private Evento createEvent(String titulo, String dia, String hora, String facilitador, String descricao,int id){
        Evento e = new Evento();
        e.dia = dia;
        e.descricao = descricao;
        e.facilitador = facilitador;
        e.hora = hora;
        e.titulo = titulo;
        e.id = id;
        return e;
    }
    private void updateNamesList(){
        peopleNames.clear();
        people.clear();
        eventsNames.clear();
        events.clear();

        Cursor c = bd.rawQuery("SELECT * FROM pessoa ", null);
        if (c.moveToFirst()){
            do {
                int id = Integer.parseInt(c.getString(c.getColumnIndex("id")));
                String name = c.getString(c.getColumnIndex("name"));
                String cpf = c.getString(c.getColumnIndex("cpf"));
                String email = c.getString(c.getColumnIndex("email"));
                Log.i("DABDAB","Loaded Person(id="+id+"): "+name+" |"+cpf+" |"+email);
                people.add(createPerson(name, cpf, email,id));
                peopleNames.add(name);
            } while(c.moveToNext());
        }

        c = bd.rawQuery("SELECT * FROM evento ", null);
        if (c.moveToFirst()){
            do {
                int id = Integer.parseInt(c.getString(c.getColumnIndex("id")));
                String titulo = c.getString(c.getColumnIndex("titulo"));
                String dia = c.getString(c.getColumnIndex("dia"));
                String hora = c.getString(c.getColumnIndex("hora"));
                String facilitador = c.getString(c.getColumnIndex("facilitador"));
                String descricao = c.getString(c.getColumnIndex("descricao"));
                events.add(createEvent(titulo, dia, hora, facilitador, descricao,id));
                eventsNames.add(titulo);
            } while(c.moveToNext());
        }
        aaPeopleAdapter.notifyDataSetChanged();
        aaEventAdapter.notifyDataSetChanged();
    }
    //Event Listener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0 :{//Cadastro de Pessoa
                if (resultCode == Activity.RESULT_OK){
                    updateNamesList();
                }
            }break;
            case 1 :{//Cadastro de Eventos
                if (resultCode == Activity.RESULT_OK){
                    updateNamesList();
                }
            }break;
            case 2 :{//Edição de Pessoa
                if (resultCode == Activity.RESULT_OK){
                    updateNamesList();
                }
            }break;
        }
    }
}
