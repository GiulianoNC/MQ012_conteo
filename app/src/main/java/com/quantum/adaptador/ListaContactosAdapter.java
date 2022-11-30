package com.quantum.adaptador;

import static com.quantum.db.DbContactos.colorGlobal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quantum.entidades.Contactos;
import com.quantum.mq012.R;
import com.quantum.mq012.VerActivity;

import java.util.ArrayList;

public class ListaContactosAdapter  extends RecyclerView.Adapter  <ListaContactosAdapter.ContactoViewHolder> {

    //crear constructor para llamado
    ArrayList  <Contactos> listaContactos;
    //prueba
    boolean correcto = false;
    int  id = 0;


    public ListaContactosAdapter(ArrayList <Contactos> listaContactos){
        this.listaContactos = listaContactos;
    }

    //diseño de cada elemento de la lista
    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item, null, false);
        return new ContactoViewHolder(view);
    }

    //vamos a poder asignar los elementos para cada opcion
    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {

        final Contactos contactos = listaContactos.get(position);
        holder.viewItem.setText(listaContactos.get(position).getItem());
        holder.viewNroSerie.setText(listaContactos.get(position).getNumero_Serie());

        holder.ViewResultado.setText(listaContactos.get(position).getResultado());

        String resultado = listaContactos.get(position).getResultado();
        if(resultado.equals("error")){
            holder.ViewResultado.setBackgroundColor(contactos.isSelected() ? Color.RED : Color.RED);
            holder.ViewResultado.setTextColor(contactos.isSelected() ? Color.WHITE : Color.WHITE);

        }else if (resultado.equals("procesado")){
            holder.ViewResultado.setBackgroundColor(contactos.isSelected() ? Color.GREEN : Color.BLUE);
            holder.ViewResultado.setTextColor(contactos.isSelected() ? Color.WHITE : Color.WHITE);

        }else{
            holder.ViewResultado.setBackgroundColor(contactos.isSelected() ? Color.WHITE : Color.WHITE);
        }

        holder.ids.setText(listaContactos.get(position).getId() + "");

      //  holder.view.setBackgroundColor(contactos.isSelected() ? Color.CYAN : Color.WHITE);

        //para seleccionar por unidad
     /*   holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactos.setSelected(!contactos.isSelected());
                holder.view.setBackgroundColor(contactos.isSelected() ? Color.CYAN : Color.WHITE);

            }
        });*/

    }

    //mostrar el tamaño de nuestra lista
    @Override
    public int getItemCount() {
       return listaContactos == null ? 0 :  listaContactos.size();
    }

    //identificar los elementos
    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        public TextView viewItem, viewNroSerie,ViewResultado,ids;
        private View view;


        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            viewItem = itemView.findViewById(R.id.viewItem);
            viewNroSerie = itemView.findViewById(R.id.viewNroSerie);
            ViewResultado = itemView.findViewById(R.id.viewResultado);
            ids = itemView.findViewById(R.id.IDtext);
            this.view = itemView;


            //para seleccionar una determinada seleccion de serie y codigo
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 Context context = view.getContext();
                 Intent intent = new Intent(context, VerActivity.class);
                  intent.putExtra("ID", listaContactos.get(getAdapterPosition()).getId());

                  context.startActivity(intent);


                }
            });


        }
    }
}

