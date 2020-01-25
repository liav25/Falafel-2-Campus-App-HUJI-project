package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.view.View;

import org.w3c.dom.Text;

public class MyBagAdapter extends FirestoreRecyclerAdapter<Mana, MyBagAdapter.MyBagHolder> {


    private final Context context;



    MyBagAdapter(@NonNull FirestoreRecyclerOptions<Mana> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyBagHolder holder, final int position, @NonNull final Mana mana) {
        System.out.println(position); // todo: delete
        holder.textViewType.setText(mana.getHebType(mana.getType()));
        holder.textViewPrice.setText(Integer.toString(mana.getPrice()));
        holder.tosafot.setText(mana.getTosafotString());
        holder.statusText.setText(mana.getStatus());
        if(mana.getPaymentMethod()==Mana.MEZUMAN){
            holder.paymentImg.setImageDrawable(context.getDrawable(R.drawable.cash));
        }
        else {
            holder.paymentImg.setImageDrawable(context.getDrawable(R.drawable.credit));
        }
    }


    @NonNull
    @Override
    public MyBagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybag_row_item, parent, false);


        return new MyBagHolder(v);
    }

    class MyBagHolder extends RecyclerView.ViewHolder {
        TextView textViewType;
        TextView textViewPrice;
        TextView tosafot;
        TextView hourText;
        TextView statusText;
        ImageView paymentImg;

        public MyBagHolder(View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.manaTypeTxt);
            textViewPrice = itemView.findViewById(R.id.manaPriceTxt);
            tosafot = itemView.findViewById(R.id.tosafutTxt);
            hourText = itemView.findViewById(R.id.hourTxt);
            statusText = itemView.findViewById(R.id.statusTxt);
            paymentImg = itemView.findViewById(R.id.paymentImg);
        }
    }

}
