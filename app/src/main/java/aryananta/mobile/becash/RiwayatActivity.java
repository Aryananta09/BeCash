package aryananta.mobile.becash;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RiwayatActivity extends AppCompatActivity {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvRiwayat;
    private RiwayatAdapter adapter;
    private Button btnDelete;
    private ImageView btnBack;
    private DatabaseReference databaseReference;
    private List<Produk> riwayatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        rvRiwayat = findViewById(R.id.recycler_view_riwayat);
        btnDelete = findViewById(R.id.btDelete);
        btnBack = findViewById(R.id.btBackToMain2);

        databaseReference = FirebaseDatabase.getInstance
                        (DBURL)
                .getReference("riwayat");

        riwayatList = new ArrayList<>();
        adapter = new RiwayatAdapter(this,riwayatList);
        rvRiwayat.setAdapter(adapter);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(this));

        loadRiwayatData();

        btnDelete.setOnClickListener(v -> deleteAllRiwayat());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRiwayatData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                riwayatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Produk item = dataSnapshot.getValue(Produk.class);
                    if (item != null) {
                        riwayatList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RiwayatActivity.this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllRiwayat() {
        databaseReference.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RiwayatActivity.this, "Riwayat dihapus", Toast.LENGTH_SHORT).show();
                        riwayatList.clear(); // Kosongkan daftar di adapter
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(RiwayatActivity.this, "Gagal menghapus riwayat", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}