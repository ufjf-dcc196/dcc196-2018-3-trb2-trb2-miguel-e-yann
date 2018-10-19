package miguelalvim.trab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onStartLoadPeople();
        onStartLoadEvents();

        lsPeopleView = findViewById(R.id.lsListaPessoas);
        lsEventView = findViewById(R.id.lsEvents);
        aaPeopleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, peopleNames);
        aaEventAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, eventsNames);

        lsPeopleView.setAdapter(aaPeopleAdapter);
        lsEventView.setAdapter(aaEventAdapter);

        aaPeopleAdapter.notifyDataSetChanged();
        aaEventAdapter.notifyDataSetChanged();


    }

    private Person createPerson(String name, String cpf, String email){
        Person p = new Person();
        p.name = name;
        p.cpf = cpf;
        p.email = email;
        return p;
    }

    private Evento createEvent(String titulo, int dia, String hora, String facilitador, String descricao){
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
        events.add(createEvent("Doing Illegal Stuff", 22, "13:00", "Ha4xorr 4A7", "You know what it means"));
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
}
