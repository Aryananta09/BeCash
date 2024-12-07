package aryananta.mobile.becash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class LelangLive extends AppCompatActivity {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvLelang;
    private LelangAdapter adapter;
    private List<Produk> lelangItemList;
    private Button btRiwayat;
    private DatabaseReference databaseReference, dbReferenceRiwayat,dbReferenceBid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_live);

        rvLelang = findViewById(R.id.rvLelang);
        btRiwayat = findViewById(R.id.btRiwayat);

        databaseReference = FirebaseDatabase.getInstance
                        (DBURL)
                .getReference("produk");
        dbReferenceRiwayat = FirebaseDatabase.getInstance(DBURL)
                        .getReference("riwayat");
        dbReferenceBid = FirebaseDatabase.getInstance(DBURL).getReference("bid");


        rvLelang.setLayoutManager(new LinearLayoutManager(this));

        lelangItemList = new ArrayList<>();

        adapter = new LelangAdapter(this,lelangItemList, item -> onItemClick(item));
        rvLelang.setAdapter(adapter);
        adapter.setDbRef(databaseReference,dbReferenceBid);

        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lelangItemList.clear(); // Bersihkan daftar sebelum menambahkan data baru

                for (DataSnapshot data : snapshot.getChildren()) {
                    Produk produk = data.getValue(Produk.class);

                    if (produk != null && "aktif".equalsIgnoreCase(produk.getStatus())) {
                        lelangItemList.add(produk);
                    }
                }

                adapter.notifyDataSetChanged(); // Beritahu adapter bahwa data telah berubah
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btRiwayat.setOnClickListener(v -> {
            Intent intent = new Intent(this, RiwayatActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.live);

        // Set Listener pada BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_add) {

                    // Pindah ke Activity Notifications
                    Intent notificationsIntent = new Intent(LelangLive.this, PostIklan.class);
                    startActivity(notificationsIntent);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_auctions) {

                    // Pindah ke Activity Notifications
                    Intent notificationsIntent = new Intent(LelangLive.this, Transaksi.class);
                    startActivity(notificationsIntent);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_home){
                    Intent i = new Intent(LelangLive.this, MainActivity.class);
                    startActivity(i);
                    return true;
                }
                if (item.getItemId() == R.id.live) {
                    Intent i = new Intent(LelangLive.this, LelangLive.class);
                    startActivity(i);
                    return true;
                }

                return false;
            }
        });
    }

//    private List<LelangItem> getLelangItems() {
//        List<LelangItem> items = new ArrayList<>();
//        items.add(new LelangItem(1, "Toyota Avanza 2012", "Buka harga 90.000.000", R.drawable.avanza));
//        items.add(new LelangItem(2, "Honda Beat 2019", "Buka harga 8.000.000", R.drawable.beat));
//        items.add(new LelangItem(3, "Innova Reborn", "Buka harga 130.000.000", R.drawable.innova));
//        items.add(new LelangItem(4, "Kijang 1990", "Buka harga 75.000.000", R.drawable.kijang));
//        items.add(new LelangItem(5, "Honda Jazz 2018", "Buka harga 120.000.000", R.drawable.jazz));
//        items.add(new LelangItem(6, "Grand Max Pickup", "Buka harga 80.000.000", R.drawable.pickup));
//        items.add(new LelangItem(7, "Yamaha R15", "Buka harga 10.000.000", R.drawable.r15));
//        return items;
//    }

    private void onItemClick(Produk item) {
        saveToRiwayat(item);
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(item.getId()));
        bundle.putString("judul", item.getNama_barang());
        bundle.putString("penjual", item.getNama_penjual());
        bundle.putString("alamat", item.getAlamat());
        bundle.putString("hargaOpen", item.getHargaOpen());
        bundle.putString("hargaBuyNow", item.getHargaBuyNow());
        bundle.putString("tanggal", item.getTanggal());
        bundle.putString("waktu", item.getWaktu());
        bundle.putString("deskripsi", item.getDeskripsi());
        bundle.putString("gambar", item.getGambar());

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveToRiwayat(Produk item) {
        new Thread(() -> {
            dbReferenceRiwayat.child(String.valueOf(item.getId())).setValue(item)
                    .addOnCompleteListener(task -> runOnUiThread(() -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Item berhasil disimpan ke riwayat", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Gagal menyimpan item ke riwayat", Toast.LENGTH_SHORT).show();
                        }
                    }));
        }).start();
    }
}