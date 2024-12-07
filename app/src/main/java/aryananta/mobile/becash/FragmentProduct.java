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

import com.google.firebase.Firebase;
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

public class FragmentProduct extends Fragment {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private RecyclerView rvProduct;
    private ArrayList<Produk> dataset;
    private ProductAdapter koleksiProductAdapter;
    private Handler handler;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public FragmentProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        rvProduct = v.findViewById(R.id.rvProduct);
        dataset = new ArrayList<>();
        koleksiProductAdapter = new ProductAdapter(getContext(), dataset);
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProduct.setAdapter(koleksiProductAdapter);

        this.db = FirebaseDatabase.getInstance(DBURL);
        this.dbRef = this.db.getReference("produk");
        this.koleksiProductAdapter.setDBRef(dbRef);

        this.dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataset.clear(); // Bersihkan dataset sebelumnya

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()); // Format tanggal dan waktu

                // Ambil data dari snapshot
                for (DataSnapshot s : snapshot.getChildren()) {
                    Produk produk = s.getValue(Produk.class);
                    if (produk != null) {
                        dataset.add(produk); // Tambahkan produk ke dataset
                    }
                }

                // Urutkan dataset berdasarkan tanggal dan waktu tanpa atribut tambahan
                dataset.sort((p1, p2) -> {
                    try {
                        String tanggalWaktuP1 = p1.getTanggal() + " " + p1.getWaktu();
                        String tanggalWaktuP2 = p2.getTanggal() + " " + p2.getWaktu();

                        Date t1 = sdf.parse(tanggalWaktuP1); // Parsing tanggal dan waktu p1
                        Date t2 = sdf.parse(tanggalWaktuP2); // Parsing tanggal dan waktu p2

                        // Perbandingan tanggal secara ascending
                        return t1.compareTo(t2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0; // Jika parsing gagal, anggap kedua produk sama
                    }
                });

                koleksiProductAdapter.notifyDataSetChanged(); // Perbarui UI
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tangani error jika ada
            }
        });


        return v;
    }
}