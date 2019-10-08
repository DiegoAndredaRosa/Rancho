package com.diegorosa.rancho.Activitys;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.diegorosa.rancho.Classes.Usuario;
import com.diegorosa.rancho.DAO.ConfiguracaoFirebase;
import com.diegorosa.rancho.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class NovoUsuarioActivity extends AppCompatActivity {
    private BootstrapEditText edtEmail;
    private BootstrapEditText edtSenha;
    private BootstrapEditText edtSenhaRep;
    private BootstrapButton btnCriaNovoUsuario;
    private BootstrapButton btnVoltar;
    private FirebaseAuth autenticacao;
    private DatabaseReference reference;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        edtEmail= (BootstrapEditText) findViewById(R.id.edtEmail);
        edtSenha= (BootstrapEditText) findViewById(R.id.edtSenha);
        edtSenhaRep= (BootstrapEditText) findViewById(R.id.edtSenhaRep);
        btnCriaNovoUsuario= (BootstrapButton) findViewById(R.id.btnCriaNovoUsuario);
        btnVoltar= (BootstrapButton) findViewById(R.id.btnVoltar);

        btnCriaNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSenha.getText().toString().equals(edtSenhaRep.getText().toString())){
                    usuario= new Usuario();
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());

                    cadastrarUsuario();
                }else{
                    Toast.makeText(NovoUsuarioActivity.this, "As senhas não se correspondem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NovoUsuarioActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(NovoUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    insereUsuario(usuario);
                    finish();
                    autenticacao.signOut();
                    Intent intent = new Intent(NovoUsuarioActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException erro){
                        erroExcecao = "A senha deve possuir no mínimo 8 caracteres";
                    }catch (FirebaseAuthInvalidCredentialsException erro){
                        erroExcecao = "O e-mail digitado é inválido";
                    }catch (FirebaseAuthUserCollisionException erro){
                        erroExcecao = "O e-mail digitado já está cadastrado";
                    }catch (Exception erro){
                        erroExcecao = "Erro ao efetuar o cadastro";
                        erro.printStackTrace();
                    }
                    Toast.makeText(NovoUsuarioActivity.this, "Erro: "+erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean insereUsuario(Usuario usuario){
        try{
            reference = ConfiguracaoFirebase.getFirebase().child("usuario");
            usuario.setKeyUsuario(reference.push().getKey());
            reference.child(usuario.getKeyUsuario()).setValue(usuario);
            Toast.makeText(NovoUsuarioActivity.this,"Usuário cadastrado com sucesso",Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception erro){
            Toast.makeText(NovoUsuarioActivity.this,"Erro ao gravar usuário",Toast.LENGTH_SHORT).show();
            erro.printStackTrace();
            return false;
        }
    }
}