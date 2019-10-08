package com.diegorosa.rancho.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.diegorosa.rancho.Activitys.PrincipalActivity;
import com.diegorosa.rancho.Classes.Produto;
import com.diegorosa.rancho.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder> {
    private List<Produto> produtoList;
    private Context context;
    private DatabaseReference referenciaFirebase;
    private List<Produto> produto;
    private Produto todosProdutos;

    public ProdutoAdapter(List<Produto> l,Context c){
        context = c;
        produtoList = l;
    }
    @NonNull
    @Override
    public ProdutoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_produto,viewGroup,false);
        return new ProdutoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoAdapter.ViewHolder holder, final int position) {

        final Produto item = produtoList.get(position);
        produto = new ArrayList<>();
        referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        item.setKeyProduto(referenciaFirebase.getKey());
        referenciaFirebase.child("produto").orderByChild("keyProduto").equalTo(item.getKeyProduto()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produto.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    holder.constraintLayoutItensProduto.setVisibility(View.VISIBLE);
                    todosProdutos = postSnapshot.getValue(Produto.class);
                    produto.add(todosProdutos);
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.constraintLayoutItensProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.txtNomeItem.setText(item.getNomeProduto());
        holder.txtQuantMedidaItem.setText(item.getQuantMedidaProduto());
        holder.txtPrecoUnitItem.setText(item.getPrecoUnitProduto());

    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtNomeItem;
        protected TextView txtQuantMedidaItem;
        protected TextView txtPrecoUnitItem;
        protected ConstraintLayout constraintLayoutItensProduto;
        protected Intent intent;

        public ViewHolder(View itemView){
            super(itemView);
            txtNomeItem = (TextView)itemView.findViewById(R.id.txtNomeItem);
            txtQuantMedidaItem = (TextView)itemView.findViewById(R.id.txtQuantMedidaItem);
            txtPrecoUnitItem = (TextView)itemView.findViewById(R.id.txtPrecoUnitItem);
            constraintLayoutItensProduto = (ConstraintLayout)itemView.findViewById(R.id.constraintLayoutItensProduto);
        }
    }


}
