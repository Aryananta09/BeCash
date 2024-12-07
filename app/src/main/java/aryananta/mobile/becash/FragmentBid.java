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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentBid extends Fragment {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvBid;
    private BidAdapter koleksiBidAdapter;
    private ArrayList<Bid> dataset;
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
        this.dbRef = this.db.getReference("bid");
        this.koleksiBidAdapter.setDbRef(dbRef);

        this.dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataset.clear(); // Bersihkan dataset sebelumnya

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()); // Format untuk parsing tanggal dan waktu

                for (DataSnapshot s : snapshot.getChildren()) {
                    Bid bid = s.getValue(Bid.class);
                    if (bid != null) {
                        dataset.add(bid); // Tambahkan bid ke dataset
                    }
                }

                // Urutkan dataset berdasarkan tanggal dan waktu
                dataset.sort((b1, b2) -> {
                    try {
                        String tanggalWaktuB1 = b1.getTanggal() + " " + b1.getWaktu(); // Gabungkan tanggal dan waktu bid1
                        String tanggalWaktuB2 = b2.getTanggal() + " " + b2.getWaktu(); // Gabungkan tanggal dan waktu bid2

                        Date t1 = sdf.parse(tanggalWaktuB1); // Parsing tanggal dan waktu bid1
                        Date t2 = sdf.parse(tanggalWaktuB2); // Parsing tanggal dan waktu bid2

                        // Perbandingan tanggal secara ascending
                        return t1.compareTo(t2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0; // Jika parsing gagal, anggap kedua bid sama
                    }
                });

                koleksiBidAdapter.notifyDataSetChanged(); // Perbarui UI
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tangani error jika ada
            }
        });


        return v;
    }
}