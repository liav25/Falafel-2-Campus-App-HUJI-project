package com.example.myapplication;

import com.example.myapplication.Mana;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderFinishActivity extends AppCompatActivity {

    private  int count;
    private  int checkedCount=0;
    private Button finishButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference manotRef ;
    private OrderFinishListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String orderId = getIntent().getStringExtra("order_id");
        manotRef= db.collection("OpenOrders/"+orderId+"/Manot");
        setContentView(R.layout.activity_order_finish);
        finishButton = findViewById(R.id.finish_button);
        setUpRecyclerView();

    }

    public void dialShevach(View view) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + getString(R.string.shevach_phone_number)));
        startActivity(dialIntent);
    }

    static HashMap<String, String> hebrewExtras = setHebrewExtrasMap();

    static HashMap<String, String> setHebrewExtrasMap() {
        hebrewExtras = new HashMap<>();
        hebrewExtras.put("Amba", "עמבה");
        hebrewExtras.put("Chips", "צ'יפס");
        hebrewExtras.put("Cucumber", "מלפפון");
        hebrewExtras.put("Eggplant", "חצילים");
        hebrewExtras.put("Harif", "חריף");
        hebrewExtras.put("Hummus", "חומוס");
        hebrewExtras.put("Kruv", "כרוב");
        hebrewExtras.put("Onion", "בצל");
        hebrewExtras.put("Pickles", "חמוצים");
        hebrewExtras.put("Thina", "טחינה");
        hebrewExtras.put("Tomato", "עגבניה");
        return hebrewExtras;
    }

    // this function is filthy please ignore it
    private void setUpRecyclerView() {
        Query query = manotRef.orderBy("owner", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<OrderFinishListItem> options = new FirestoreRecyclerOptions
                .Builder<OrderFinishListItem>()
                .setQuery(query, new SnapshotParser<OrderFinishListItem>() {
                    @NonNull
                    @Override
                    public OrderFinishListItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {

                        String owner = snapshot.get("owner").toString();
                        if (owner.contains(" ")) {
                            owner = owner.split(" ")[0];
                        }
                        String type = Mana.getHebType(snapshot.get("type").toString());

                        int in = 0;
                        int out = 0;
                        Map<String, Boolean> map = (Map) snapshot.get("tosafot");
                        for (String key : map.keySet()) {
                            if (map.get(key) == true) {
                                in++;
                            } else {
                                out++;
                            }
                        }
                        String description = "";
                        if (in > out) {
                            description = "הכל";
                            if (out > 0) {
                                description += " בלי ";
                                for (String key : map.keySet()) {
                                    if (map.get(key) == false) {
                                        description += hebrewExtras.get(key) + " ";
                                    }

                                }
                            }
                        } else {
                            description = "כלום";
                            if (in > 0) {
                                description += " עם ";
                                for (String key : map.keySet()) {
                                    if (map.get(key) == true) {
                                        description += hebrewExtras.get(key) + " ";
                                    }

                                }
                            }

                        }


                        return new OrderFinishListItem(owner, type, description);
                    }
                })
                .build();

        adapter = new OrderFinishListItemAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        count = adapter.getItemCount();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void checkBoxClick(View view) {
        boolean checked = ((CheckBox)view).isChecked();
        count = adapter.getItemCount();
        
        if(checked){
            checkedCount++;
        }
        else{
            checkedCount--;
        }
        if(checkedCount==count){
            finishButton.setVisibility(View.VISIBLE);
            finishButton.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            finishButton.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else {
            finishButton.setVisibility(View.INVISIBLE);
            finishButton.setWidth(0);
            finishButton.setHeight(0);
        }
    }
}
