package com.diegorosa.rancho.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.diegorosa.rancho.Classes.Usuario;
import com.diegorosa.rancho.DAO.ConfiguracaoFirebase;
import com.diegorosa.rancho.Helper.Preferencias;
import com.diegorosa.rancho.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private BootstrapEditText edtEmail;
    private BootstrapEditText edtSenha;
    private BootstrapButton btnLogin;
    private TextView txtNovoUsuario;
    private TextView txtRecuperarSenha;
    private Usuario usuario;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtSenha = (BootstrapEditText) findViewById(R.id.edtSenha);
        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        txtNovoUsuario = (TextView) findViewById(R.id.txtNovoUsuario);
        txtRecuperarSenha = (TextView) findViewById(R.id.txtRecuperarSenha);

        final EditText editTextEmail = new EditText(MainActivity.this);
        editTextEmail.setHint("exemplo@exemplo.com");

        permission();

        if (usuarioLogado()) {
            Intent usuarioJaLogado = new Intent(MainActivity.this, PrincipalActivity.class);
            startActivity(usuarioJaLogado);
        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                        usuario = new Usuario();
                        usuario.setEmail(edtEmail.getText().toString());
                        usuario.setSenha(edtSenha.getText().toString());
                        validarLogin();
                    } else {
                        Toast.makeText(MainActivity.this, "e-mail e senha devem ser preenchidos", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            txtNovoUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NovoUsuarioActivity.class);
                    startActivity(intent);
                }
            });

            txtRecuperarSenha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Recuperar senha");
                    builder.setMessage("Informe o seu e-mail");
                    builder.setView(editTextEmail);
                    if(!editTextEmail.getText().equals("")){
                        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                autenticacao = FirebaseAuth.getInstance();
                                String emailRecuperacao = editTextEmail.getText().toString();
                                autenticacao.sendPasswordResetEmail(emailRecuperacao).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, "Em instantes você receberá um e-mail para recuperação da senha", Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(MainActivity.this, "Falha no envio de e-mail de recuperação", Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                    }else{
                        Toast.makeText(MainActivity.this, "Preencha o campo com seu e-mail", Toast.LENGTH_SHORT).show();
                    }
                    alerta = builder.create();
                    alerta.show();
                }
            });
        }
    }
    private void validarLogin () {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Login efetuado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                finish();
                startActivity(intent);
                Preferencias preferencias = new Preferencias(MainActivity.this);
                preferencias.salvarUsuarioPreferencias(usuario.getEmail(),usuario.getSenha());
            } else {
                Toast.makeText(MainActivity.this, "e-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        }
        });
    }

    public Boolean usuarioLogado () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public void permission(){
        int PERMISSION_ALL =1;

        String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this,PERMISSION,PERMISSION_ALL);
    }

}