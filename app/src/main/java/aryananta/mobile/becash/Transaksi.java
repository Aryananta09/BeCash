package aryananta.mobile.becash;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Transaksi extends AppCompatActivity implements View.OnClickListener {

    private Button btProduct, btBid;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        btProduct = findViewById(R.id.btProduct);
        btBid = findViewById(R.id.btBid);
        this.fm = getSupportFragmentManager();

        btProduct.setOnClickListener(this);
        btBid.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_auctions);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent notificationsIntent = new Intent(Transaksi.this, MainActivity.class);
                    startActivity(notificationsIntent);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_add) {
                    Intent notificationsIntent = new Intent(Transaksi.this, PostIklan.class);
                    startActivity(notificationsIntent);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_auctions) {
                    Intent notificationsIntent = new Intent(Transaksi.this, Transaksi.class);
                    startActivity(notificationsIntent);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        // Reset status tombol
        btProduct.setSelected(false);
        btBid.setSelected(false);

        if (view.getId() == R.id.btProduct) {
            view.setSelected(true);
            fm.beginTransaction().replace(R.id.container, new FragmentProduct())
                    .commit();
        }
        else if (view.getId() == R.id.btBid) {
            view.setSelected(true);
            fm.beginTransaction().replace(R.id.container, new FragmentBid())
                    .commit();
        }
    }
}