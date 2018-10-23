package miguelalvim.trab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CadastroPessoaEventoActivity extends AppCompatActivity {
    ListView ls;
    ArrayList<String> eventNames;
    ArrayAdapter aaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa_cadastrar_evento);

        ls = findViewById(R.id.lvList);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            eventNames = extras.getStringArrayList("eventosG");
        }else{
            finish();
        }


        aaAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,eventNames);
        ls.setAdapter(aaAdapter);
        aaAdapter.notifyDataSetChanged();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent();
                result.putExtra("pos",position);
                setResult(RESULT_OK, result);
                finish();
            }
        });

    }
}
