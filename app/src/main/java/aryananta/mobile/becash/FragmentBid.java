package aryananta.mobile.becash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentBid extends Fragment {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvBid;
    private BidAdapter koleksiBidAdapter;
    private ArrayList<Produk> dataset;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public FragmentBid() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bid, container, false);

        dataset = new ArrayList<>();
        rvBid = v.findViewById(R.id.rvProduct);
        koleksiBidAdapter = new BidAdapter(getContext(), dataset);
        rvBid.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBid.setAdapter(koleksiBidAdapter);

        this.db = FirebaseDatabase.getInstance(DBURL);
        this.dbRef = this.db.getReference("produk");
        this.koleksiBidAdapter.setDbRef(dbRef);

        this.dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataset.clear();
                for(DataSnapshot s : snapshot.getChildren()){
                    Produk produk = s.getValue(Produk.class);
                    dataset.add(produk);
                }
                koleksiBidAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}