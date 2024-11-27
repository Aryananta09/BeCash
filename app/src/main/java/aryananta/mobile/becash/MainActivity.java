package aryananta.mobile.becash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvLelang;
    private List<Produk> dataset;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi RecyclerView
        rvLelang = findViewById(R.id.rvLelang);
        dataset = new ArrayList<>();
        MainAdapter adapter = new MainAdapter(this, dataset);
        rvLelang.setLayoutManager(new LinearLayoutManager(this));
        rvLelang.setAdapter(adapter);

        this.db = FirebaseDatabase.getInstance(DBURL);
        this.dbRef = this.db.getReference("produk");
        adapter.setDbRef(dbRef);
        this.dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataset.clear();
                for(DataSnapshot s : snapshot.getChildren()){
                    Produk produk = s.getValue(Produk.class);
                    dataset.add(produk);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

                return false;
            }
        });
    }

}