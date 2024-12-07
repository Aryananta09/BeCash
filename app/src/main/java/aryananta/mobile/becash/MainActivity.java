package aryananta.mobile.becash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvLelang, rvLive;
    private List<Produk> dataset, datasetLive;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private TextView tvSeeMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tvSeeMore = findViewById(R.id.tvSeeMore);
        tvSeeMore.setOnClickListener(this);

        // Inisialisasi RecyclerViewScheduled
        rvLive = findViewById(R.id.rvLive);
        datasetLive = new ArrayList<>();
        MainLiveAdapter adapterLive = new MainLiveAdapter(this, datasetLive);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvLive.setLayoutManager(gridLayoutManager);
        rvLive.setAdapter(adapterLive);


        // Inisialisasi RecyclerViewScheduled
        rvLelang = findViewById(R.id.rvLelang);
        dataset = new ArrayList<>();
        MainAdapter adapter = new MainAdapter(this, dataset);
        rvLelang.setLayoutManager(new LinearLayoutManager(this));
        rvLelang.setAdapter(adapter);

        this.db = FirebaseDatabase.getInstance(DBURL);
        this.dbRef = this.db.getReference("produk");
        adapter.setDbRef(dbRef);
        adapterLive.setDbRef(dbRef);
        this.dbRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataset.clear();
                    datasetLive.clear();

                    for (DataSnapshot s : snapshot.getChildren()) {
                        Produk produk = s.getValue(Produk.class);
                        if (produk == null) continue;

                        String tanggalLelang = produk.getTanggal();
                        String waktuLelang = produk.getWaktu();

                        if (tanggalLelang != null && waktuLelang != null) {
                            String tanggalWaktuGabung = tanggalLelang + " " + waktuLelang;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            Date tanggalWaktu = null;

                            try {
                                tanggalWaktu = sdf.parse(tanggalWaktuGabung);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (tanggalWaktu != null && tanggalWaktu.before(new Date())) {
                                produk.setStatus("aktif");
                                s.getRef().child("status").setValue("aktif");
                            }

                            if ("aktif".equalsIgnoreCase(produk.getStatus())) {
                                if (datasetLive.size() < 3) { // Batasi hanya 3 item
                                    datasetLive.add(produk);}
                            } else {
                                dataset.add(produk); // Masukkan ke dataset
                            }
                        }
                    }

                    // Notify adapter untuk memperbarui UI
                    adapter.notifyDataSetChanged();
                    adapterLive.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Tangani error jika ada
                }
            });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // Set Listener pada BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_add) {

                    // Pindah ke Activity Notifications
                    Intent notificationsIntent = new Intent(MainActivity.this, PostIklan.class);
                    startActivity(notificationsIntent);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_auctions) {

                    // Pindah ke Activity Notifications
                    Intent notificationsIntent = new Intent(MainActivity.this, Transaksi.class);
                    startActivity(notificationsIntent);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_home){
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;
                }
                if (item.getItemId() == R.id.live) {
                    Intent i = new Intent(MainActivity.this, LelangLive.class);
                    startActivity(i);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvSeeMore) {
            Intent i = new Intent(this, LelangLive.class);
            startActivity(i);
        }
    }


}