package miguelalvim.trab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    int pos =-1;

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

        Bundle extras = getIntent().getExtras();
        edit = extras!=null;
        if(edit){
            lbActivity.setText("Edição de Cadastro");
            txtCPF.setText(extras.getString("cpf"));
            txtName.setText(extras.getString("name"));
            txtEmail.setText(extras.getString("email"));
            pos = extras.getInt("pos");
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
                    result.putExtra("email", txtEmail.getText().toString());
                    result.putExtra("CPF", txtCPF.getText().toString());
                    result.putExtra("name", txtName.getText().toString());
                    result.putExtra("pos", pos);
                    setResult(RESULT_OK, result);
                    finish();
                }else{
                    Toast.makeText(CadastroPessoaActivity.this, "Há dados não inseridos",  Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
