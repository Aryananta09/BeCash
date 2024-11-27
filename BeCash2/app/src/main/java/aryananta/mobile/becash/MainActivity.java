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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvLelang;
    private List<Produk> dataset;
    private Handler handler;
    private ProdukDatabase db;

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

        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                adapter.notifyDataSetChanged();
            }
        };

        Thread t = new Thread(() -> {
            this.db = DBInstance.getInstance(this);
            List<Produk> data = this.db.produkDao().getAll();
            this.dataset.clear();
            this.dataset.addAll(data);
            this.handler.sendMessage(this.handler.obtainMessage());
        });
        t.start();

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