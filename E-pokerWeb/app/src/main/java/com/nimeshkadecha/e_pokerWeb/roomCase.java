package com.nimeshkadecha.e_pokerWeb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class roomCase extends AppCompatActivity {



    private RecyclerView recyclerViewRoomCase;
    private ArrayList Acnr, Aroom, Adate, Alic, Amobile, ACstatus;
    roomCaseAdapter RoomCase_adapter;

    private EditText searchRoomet ;
    private Button search,cancle;
    private TextView searchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_case);

        searchRoomet = findViewById(R.id.searchRoomCase);
        search = findViewById(R.id.searchRoombtn);

        cancle = findViewById(R.id.CancleRoomCase);
        cancle.setVisibility(View.GONE);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                Acnr.removeAll(Acnr);
                Aroom.removeAll(Aroom);
                Adate.removeAll(Adate);
                Alic.removeAll(Alic);
                Amobile.removeAll(Amobile);
                ACstatus.removeAll(ACstatus);
                Toast.makeText(roomCase.this, "Cancled", Toast.LENGTH_SHORT).show();
                RoomCase_adapter.notifyDataSetChanged();
            }
        });

        searchRoomet.setFilters(new InputFilter[] {
                new InputFilter.AllCaps() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        return String.valueOf(source).toLowerCase();
                    }
                }
        });

        searchStatus = findViewById(R.id.statusRoomCase);

        final int[] search_status = {0};

        if(search_status[0] == 0){
            searchStatus.setText("Search to see room Case List");
        }else{
            searchStatus.setText("");
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ENimesh", "Clicked");
                String room = searchRoomet.getText().toString();
                if(room.equals("")){
                    searchRoomet.setError("Enter Room name to see List");
                    searchStatus.setText("Enter Room name");
                }else{
                    cancle.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    search_status[0] = 1;
                    searchStatus.setText("");

                    Acnr = new ArrayList<>();
                    Aroom = new ArrayList<>();
                    Adate = new ArrayList<>();
                    Alic = new ArrayList<>();
                    Amobile = new ArrayList<>();
                    ACstatus = new ArrayList<>();

                    recyclerViewRoomCase = findViewById(R.id.recyclerViewRoomCase);
                    RoomCase_adapter = new roomCaseAdapter(roomCase.this, Acnr, Aroom, Adate, Alic, Amobile, ACstatus);
                    recyclerViewRoomCase.setAdapter(RoomCase_adapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(roomCase.this);
                    recyclerViewRoomCase.setLayoutManager(linearLayoutManager);

                    DatabaseReference reference;
                    reference = FirebaseDatabase.getInstance().getReference();

                    reference.child("case").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Map map = (Map) snapshot.getValue();
                            String dbroom = String.valueOf(map.get("Room"));
                            if(dbroom.contains(room)){
                                Acnr.add(map.get("CNR"));
                                Aroom.add(map.get("Room"));
                                Adate.add(map.get("Date"));
                                Alic.add(map.get("ALicence"));
                                Amobile.add(map.get("AMobile"));
                                ACstatus.add(map.get("CaseCondition"));
                            }

                            RoomCase_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }
}