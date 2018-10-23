package miguelalvim.trab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Person> people = new ArrayList<Person>();
    ArrayList<String> peopleNames = new ArrayList<String>();
    ArrayList<Evento> events = new ArrayList<Evento>();
    ArrayList<String> eventsNames = new ArrayList<String>();
    ArrayAdapter<String> aaPeopleAdapter;
    ArrayAdapter<String> aaEventAdapter;

    ListView lsPeopleView;
    ListView lsEventView;
    Button bttCadastrarPessoa,bttCadastrarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onStartLoadPeople();
        onStartLoadEvents();
        people.get(0).eventos.add(events.get(0));

        lsPeopleView = findViewById(R.id.lsListaPessoas);
        lsEventView = findViewById(R.id.lsEvents);
        bttCadastrarEvento = findViewById(R.id.bttCadastrarEvento);
        bttCadastrarPessoa = findViewById(R.id.bttCadastrarPessoa);


        aaPeopleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, peopleNames);
        aaEventAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, eventsNames);

        lsPeopleView.setAdapter(aaPeopleAdapter);
        lsEventView.setAdapter(aaEventAdapter);

        aaPeopleAdapter.notifyDataSetChanged();
        aaEventAdapter.notifyDataSetChanged();

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

                intent.putExtra("cpf", people.get(position).cpf);
                intent.putExtra("name", people.get(position).name);
                intent.putExtra("email", people.get(position).email);
                intent.putExtra("pos", position);
                intent.putParcelableArrayListExtra("eventosP", people.get(position).eventos);
                intent.putParcelableArrayListExtra("eventosG", events);
                startActivityForResult(intent,2);//Request code 2 = tela de checagem de pessoa
            }
        });

    }

    private Person createPerson(String name, String cpf, String email){
        Person p = new Person();
        p.name = name;
        p.cpf = cpf;
        p.email = email;
        p.eventos =  new ArrayList<Evento>();
        return p;
    }
    private Evento createEvent(String titulo, String dia, String hora, String facilitador, String descricao){
        Evento e = new Evento();
        e.dia = dia;
        e.descricao = descricao;
        e.facilitador = facilitador;
        e.hora = hora;
        e.titulo = titulo;
        return e;
    }
    private void onStartLoadPeople(){
        people.add(createPerson("Miguel Alvim", "12312312312", "nope@nopes.n"));
        people.add(createPerson("Testenildo Alves", "12345654323", "nopes@nopes.n"));
        people.add(createPerson("Mamom H.", "131236661231", "nope2@nopes.n"));
        updateNamesList();
    }
    private void onStartLoadEvents(){
        events.add(createEvent("Doing Illegal Stuff", "22", "13:00", "Ha4xorr 4A7", "You know what it means"));
        updateNamesList();
    }
    private void updateNamesList(){
        peopleNames.clear();
        eventsNames.clear();
        for(int i=0;i<people.size();++i){
            peopleNames.add(people.get(i).name);
        }
        for(int i=0;i<events.size();++i){
            eventsNames.add(events.get(i).titulo);
        }
    }
    //Event Listener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0 :{//Cadastro de Pessoa
                if (resultCode == Activity.RESULT_OK && data != null){
                    Person p = createPerson(data.getStringExtra("name"),data.getStringExtra("CPF"),data.getStringExtra("email"));

                    people.add(p);
                    peopleNames.add(p.name);
                    aaPeopleAdapter.notifyDataSetChanged();
                }
            }break;
            case 1 :{//Cadastro de Eventos
                if (resultCode == Activity.RESULT_OK && data != null){
                    Evento e = createEvent(data.getStringExtra("titulo"), data.getStringExtra("dia"), data.getStringExtra("hora"), data.getStringExtra("facilitador"), data.getStringExtra("descricao"));

                    events.add(e);
                    eventsNames.add(e.titulo);
                    aaEventAdapter.notifyDataSetChanged();
                }
            }break;
            case 2 :{//Edição de Pessoa
                if (resultCode == Activity.RESULT_OK && data != null){
                    Person p = createPerson(data.getStringExtra("name"),data.getStringExtra("CPF"),data.getStringExtra("email"));
                    p.eventos = (ArrayList<Evento>)data.getExtras().get("eventsP");

                    people.set(data.getIntExtra("pos", 0),p);
                    peopleNames.set(data.getIntExtra("pos", 0),p.name);

                    aaPeopleAdapter.notifyDataSetChanged();
                }
            }break;
        }
    }
}
