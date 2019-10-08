package com.diegorosa.rancho.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.diegorosa.rancho.Adapter.ProdutoAdapter;
import com.diegorosa.rancho.Classes.Lista;
import com.diegorosa.rancho.Classes.Produto;
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

public class NovoProdutoActivity extends AppCompatActivity {
    private FloatingActionButton fabNovoProduto;
    private DatabaseReference reference;
    private Produto todosProdutos;
    private FirebaseAuth autenticacao;
    private RecyclerView rcvProduto;
    private ProdutoAdapter produtoAdapter;
    private List<Produto> produto;
    private Produto todasProdutos;
    private FloatingActionButton fabNovaProduto;
    private LinearLayoutManager mLayoutManagerProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        fabNovoProduto = (FloatingActionButton)findViewById(R.id.fabNovoProduto);

        fabNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogNovoProduto();
            }
        });

        autenticacao = FirebaseAuth.getInstance();

        rcvProduto = (RecyclerView)findViewById(R.id.rcvProduto);
        carregarProduto();
    }


    private void abrirDialogNovoProduto(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_novo_produto);
        final BootstrapEditText edtNomeProduto = (BootstrapEditText)dialog.findViewById(R.id.edtNomeProduto);
        final BootstrapEditText edtPrecoUnitProduto = (BootstrapEditText)dialog.findViewById(R.id.edtPrecoUnitProduto);
        final BootstrapEditText edtQuantMedidaProduto = (BootstrapEditText)dialog.findViewById(R.id.edtQuantMedidaProduto);
        final BootstrapButton btnNovoProduto = (BootstrapButton)dialog.findViewById(R.id.btnNovoProduto);
        final BootstrapButton btnCancelarNovoProduto = (BootstrapButton)dialog.findViewById(R.id.btnCancelarNovoProduto);

        btnNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produto novoproduto = new Produto();
                novoproduto.setNomeProduto(edtNomeProduto.getText().toString());
                novoproduto.setPrecoUnitProduto(edtPrecoUnitProduto.getText().toString());
                novoproduto.setQuantMedidaProduto(edtQuantMedidaProduto.getText().toString());
                criarNovoProduto(novoproduto);
                Intent intent = new Intent(NovoProdutoActivity.this,NovoProdutoActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnCancelarNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NovoProdutoActivity.this,NovoProdutoActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void criarNovoProduto(Produto novoproduto){
        try{
            reference = ConfiguracaoFirebase.getFirebase().child("produto");
            novoproduto.setKeyProduto(reference.push().getKey());
            reference.child(novoproduto.getKeyProduto()).setValue(novoproduto);
            Toast.makeText(NovoProdutoActivity.this,"Nova lista cadastrada com sucesso",Toast.LENGTH_SHORT).show();
        }catch (Exception erro){
            Toast.makeText(NovoProdutoActivity.this,"Erro ao gravar lista",Toast.LENGTH_SHORT).show();
            erro.printStackTrace();
        }
    }

    private void carregarProduto(){
        rcvProduto.setHasFixedSize(true);
        mLayoutManagerProduto = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvProduto.setLayoutManager(mLayoutManagerProduto);
        produto = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("produto").orderByChild("keyProduto").addValueEventListener(new ValueEventListener() {@Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            produto.clear();
            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                todosProdutos = postSnapshot.getValue(Produto.class);
                produto.add(todosProdutos);
            }
            produtoAdapter.notifyDataSetChanged();
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        produtoAdapter = new ProdutoAdapter(produto,this);
        rcvProduto.setAdapter(produtoAdapter);
    }

}
