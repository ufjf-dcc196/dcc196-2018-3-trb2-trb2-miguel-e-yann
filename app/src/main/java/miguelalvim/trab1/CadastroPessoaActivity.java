package miguelalvim.trab1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CadastroPessoaActivity extends AppCompatActivity {
    EditText txtCPF,txtName,txtEmail;
    TextView lbActivity;
    Button bttConfirm,bttCancel;
    boolean edit = false;
    int pos =-1,id_=-1;


    DBHandler bdHandler;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtCPF = findViewById(R.id.txtTitulo);
        txtName = findViewById(R.id.txtDia);
        txtEmail = findViewById(R.id.txtHora);
        bttConfirm = findViewById(R.id.bttConfirm);
        bttCancel = findViewById(R.id.bttCancel);
        lbActivity = findViewById(R.id.lbActivity);

        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        Bundle extras = getIntent().getExtras();
        edit = extras!=null;
        if(edit){
            lbActivity.setText("Edição de Cadastro");
                    Cursor c = bd.rawQuery("SELECT name,cpf,email FROM pessoa WHERE id="+extras.getInt("id",-1), null);
                    c.moveToFirst();
                    txtName.setText(c.getString(c.getColumnIndex("name")));
                    txtCPF.setText(c.getString(c.getColumnIndex("cpf")));
                    txtEmail.setText(c.getString(c.getColumnIndex("email")));

//            txtCPF.setText(extras.getString("cpf"));
//            txtName.setText(extras.getString("name"));
//            txtEmail.setText(extras.getString("email"));
//            pos = extras.getInt("pos");
            id_ = extras.getInt("id");
        }

        bttCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bttConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtCPF.getText().toString().isEmpty() &&
                        !txtName.getText().toString().isEmpty() &&
                        !txtEmail.getText().toString().isEmpty()) {
                    Intent result = new Intent();

                    //inserting into database
                    ContentValues vals = new ContentValues();
                    vals.put("email", txtEmail.getText().toString());
                    vals.put("cpf", txtCPF.getText().toString());
                    vals.put("name", txtName.getText().toString());
                    if(edit) {
                        Log.i("DABDAB","id: "+id_);
                        Log.i("DABDAB","modified: "+bd.update("pessoa", vals,"id="+id_,null));
                        setResult(RESULT_OK, null);
                    }else{
                        long id = bd.insert("pessoa", null, vals);
                        if(id==-1) {
                            Toast.makeText(CadastroPessoaActivity.this, "Erro ao salvar os dados",  Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, result);
                        }
                        result.putExtra("id", id);
                        setResult(RESULT_OK, result);
                    }
                    finish();
                }else{
                    Toast.makeText(CadastroPessoaActivity.this, "Há dados não inseridos",  Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
