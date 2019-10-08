package com.diegorosa.rancho.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.diegorosa.rancho.Adapter.ListaAdapter;
import com.diegorosa.rancho.Classes.Lista;
import com.diegorosa.rancho.Classes.Usuario;
import com.diegorosa.rancho.DAO.ConfiguracaoFirebase;
import com.diegorosa.rancho.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private RecyclerView rcvLista;
    private ListaAdapter listaAdapter;
    private List<Lista> lista;
    private DatabaseReference reference;
    private Lista todasListas;
    private FloatingActionButton fabNovaLista;
    private LinearLayoutManager mLayoutManagerLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        fabNovaLista = (FloatingActionButton)findViewById(R.id.fabNovaLista);

        fabNovaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogNovaLista();
            }
        });


        autenticacao = FirebaseAuth.getInstance();


        /*String emailUsuarioLogado = autenticacao.getCurrentUser().getEmail();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("usuario").orderByChild("email").equalTo(emailUsuarioLogado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    usuario = postSnapshot.getValue(Usuario.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        rcvLista = (RecyclerView)findViewById(R.id.rcvLista);
        carregarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.actSair){
            deslogarUsuario();
        }else if(id == R.id.actNovoProduto){
            Intent intent = new Intent(PrincipalActivity.this,NovoProdutoActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(PrincipalActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void carregarLista(){
        rcvLista.setHasFixedSize(true);
        mLayoutManagerLista = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvLista.setLayoutManager(mLayoutManagerLista);
        lista = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("lista").orderByChild("keyLista").addValueEventListener(new ValueEventListener() {@Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todasListas = postSnapshot.getValue(Lista.class);
                    lista.add(todasListas);
                }
                listaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listaAdapter = new ListaAdapter(lista,this);
        rcvLista.setAdapter(listaAdapter);
    }

    private void abrirDialogNovaLista(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_nova_lista);
        final BootstrapEditText edtDescricaoLista = (BootstrapEditText)dialog.findViewById(R.id.edtDescricaoLista);
        final BootstrapButton btnNovaLista = (BootstrapButton)dialog.findViewById(R.id.btnNovaLista);
        final BootstrapButton btnCancelarLista = (BootstrapButton)dialog.findViewById(R.id.btnCancelarLista);

        btnNovaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lista novalista = new Lista();
                novalista.setDescricaoLista(edtDescricaoLista.getText().toString());
                criarNovaLista(novalista);
                Intent intent = new Intent(PrincipalActivity.this,PrincipalActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnCancelarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this,PrincipalActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void criarNovaLista(Lista novalista){
        try{
            reference = ConfiguracaoFirebase.getFirebase().child("lista");
            //novalista.setKeyListaUsuario(usuario.getKeyUsuario());
            novalista.setKeyLista(reference.push().getKey());
            reference.child(novalista.getKeyLista()).setValue(novalista);
            Toast.makeText(PrincipalActivity.this,"Nova lista cadastrada com sucesso",Toast.LENGTH_SHORT).show();
        }catch (Exception erro){
            Toast.makeText(PrincipalActivity.this,"Erro ao gravar lista",Toast.LENGTH_SHORT).show();
            erro.printStackTrace();
        }
    }

    public void abrirMinhaLista(){
        Intent intent = new Intent(PrincipalActivity.this,MinhaListaActivity.class);
        startActivity(intent);
    }
}