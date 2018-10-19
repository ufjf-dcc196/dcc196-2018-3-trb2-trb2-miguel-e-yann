package miguelalvim.trab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    LinkedList<Person> people = new LinkedList<Person>();
    LinkedList<String> peopleNames = new LinkedList<String>();
    LinkedList<Evento> events = new LinkedList<Evento>();
    LinkedList<String> eventsNames = new LinkedList<String>();
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

    }

    private Person createPerson(String name, String cpf, String email){
        Person p = new Person();
        p.name = name;
        p.cpf = cpf;
        p.email = email;
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
    //Event Listenner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0 :{//Cadastro de Pessoa
                if (resultCode == Activity.RESULT_OK && data != null){
                    Person p = new Person();
                    p.cpf =data.getStringExtra("CPF");
                    p.email =data.getStringExtra("email");
                    p.name =data.getStringExtra("name");
                    people.add(p);
                    peopleNames.add(p.name);
                    aaPeopleAdapter.notifyDataSetChanged();
                }
            }break;
            case 1 :{//Cadastro de Eventos
                if (resultCode == Activity.RESULT_OK && data != null){
                    Evento e = new Evento();
                    e.titulo = data.getStringExtra("titulo");
                    e.hora = data.getStringExtra("hora");
                    e.facilitador = data.getStringExtra("facilitador");
                    e.descricao = data.getStringExtra("descricao");
                    e.dia = data.getStringExtra("dia");

                    events.add(e);
                    eventsNames.add(e.titulo);
                    aaEventAdapter.notifyDataSetChanged();
                }
            }break;
            case 2 :{
                if (resultCode == Activity.RESULT_OK && data != null){
                    if(data.getBooleanExtra("Add", false)) {
//                        ++totExterno;
//                        txtExterno.setText("Servidores: " + totExterno);
                    }
                }
            }break;
        }
    }
}
