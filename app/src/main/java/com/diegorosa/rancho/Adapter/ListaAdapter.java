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

import com.diegorosa.rancho.Activitys.MinhaListaActivity;
import com.diegorosa.rancho.Activitys.PrincipalActivity;
import com.diegorosa.rancho.Classes.Lista;
import com.diegorosa.rancho.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<Lista> listaList;
    private Context context;
    private DatabaseReference referenciaFirebase;
    private List<Lista> lista;
    private Lista todasListas;

    public ListaAdapter(List<Lista> l,Context c){
        context = c;
        listaList = l;
    }
    @NonNull
    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_lista,viewGroup,false);
        return new ListaAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListaAdapter.ViewHolder holder, final int position) {

        final Lista item = listaList.get(position);
        lista = new ArrayList<>();
        referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        item.setKeyLista(referenciaFirebase.getKey());
        referenciaFirebase.child("lista").orderByChild("keyLista").equalTo(item.getKeyLista()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    holder.constraintLayoutItensLista.setVisibility(View.VISIBLE);
                    todasListas = postSnapshot.getValue(Lista.class);
                    lista.add(todasListas);
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            holder.constraintLayoutItensLista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        holder.txtDescricaoItem.setText(item.getDescricaoLista());
    }

    @Override
    public int getItemCount() {
        return listaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtDescricaoItem;
        protected ConstraintLayout constraintLayoutItensLista;
        protected Intent intent;

        public ViewHolder(View itemView){
            super(itemView);
            txtDescricaoItem = (TextView)itemView.findViewById(R.id.txtDescricaoItem);
            constraintLayoutItensLista = (ConstraintLayout)itemView.findViewById(R.id.constraintLayoutItensLista);
        }
    }


}
