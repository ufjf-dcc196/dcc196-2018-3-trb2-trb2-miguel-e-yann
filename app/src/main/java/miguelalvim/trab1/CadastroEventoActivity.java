package miguelalvim.trab1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CadastroEventoActivity extends AppCompatActivity {
    Button bttConfirm,bttCancel;
    EditText txtDia,txtDescricao,txtTitulo,txtHora,txtFacilitador;
    TextView lbActivity;

    DBHandler bdHandler;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        txtDia = findViewById(R.id.txtDia);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtHora = findViewById(R.id.txtHora);
        txtFacilitador = findViewById(R.id.txtFacilitador);
        lbActivity = findViewById(R.id.lbActivity);
        bttConfirm = findViewById(R.id.bttConfirm);
        bttCancel = findViewById(R.id.bttCancel);

        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        bttCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bttConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtDia.getText().toString().isEmpty() &&
                        !txtDescricao.getText().toString().isEmpty() &&
                        !txtHora.getText().toString().isEmpty() &&
                        !txtFacilitador.getText().toString().isEmpty() &&
                        !txtTitulo.getText().toString().isEmpty()) {

                    //inserting into database
                    Intent result = new Intent();
                    ContentValues vals = new ContentValues();
                    vals.put("dia", txtDia.getText().toString());
                    vals.put("descricao", txtDescricao.getText().toString());
                    vals.put("titulo", txtTitulo.getText().toString());
                    vals.put("hora", txtHora.getText().toString());
                    vals.put("facilitador", txtFacilitador.getText().toString());
                    long id = bd.insert("evento", null, vals);
                    if(id==-1) {
                        Toast.makeText(CadastroEventoActivity.this, "Erro ao salvar os dados",  Toast.LENGTH_SHORT).show();
                    }else{
                        result.putExtra("id", (int)id);
                    }
                    setResult(RESULT_OK, result);
                    finish();
                }else{
                    Toast.makeText(CadastroEventoActivity.this, "Há dados não inseridos",  Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
