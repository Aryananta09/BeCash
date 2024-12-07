package aryananta.mobile.becash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailItem extends AppCompatActivity implements View.OnClickListener {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private TextView tvJudul, tvPenjual, tvAlamat, tvHarga, tvTanggal, tvWaktu, tvDeskripsi;
    private ImageView ivGambar;
    private String id, judul, penjual, alamat, harga, tanggal, waktu, deskripsi, gambar;
    private ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        tvJudul = findViewById(R.id.tvNama);
        tvPenjual = findViewById(R.id.tvPenjual);
        tvAlamat = findViewById(R.id.tvAlamatBarang);
        tvHarga = findViewById(R.id.tvOpenPrice);
        tvTanggal = findViewById(R.id.tvDate);
        tvWaktu = findViewById(R.id.tvTime);
        tvDeskripsi = findViewById(R.id.tvDesc);
        ivGambar = findViewById(R.id.ivItems);
        btBack = findViewById(R.id.backButton);
        btBack.setOnClickListener(this);


        // Ambil Bundle dari Intent
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getString("id");
            judul = bundle.getString("judul");
            penjual = bundle.getString("penjual");
            alamat = bundle.getString("alamat");
            harga = bundle.getString("harga");
            tanggal = bundle.getString("tanggal");
            waktu = bundle.getString("waktu");
            deskripsi = bundle.getString("deskripsi");

            // Set data ke TextView
            tvJudul.setText(judul);
            tvPenjual.setText(penjual);
            tvAlamat.setText(alamat);
            tvHarga.setText(harga);
            tvTanggal.setText(tanggal);
            tvWaktu.setText(waktu);
            tvDeskripsi.setText(deskripsi);
            loadImage(id);
        }

    }
    private void loadImage(String produkId) {
        // Referensi ke produk berdasarkan ID
        DatabaseReference dbRef = FirebaseDatabase.getInstance(DBURL).getReference("produk").child(produkId);

        dbRef.child("gambar").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                // Ambil gambar dalam format Base64
                String base64Image = dataSnapshot.getValue(String.class);

                // Konversi Base64 ke Bitmap
                Bitmap bitmap = Base64Image.base64ToBitmap(base64Image);

                // Set gambar ke ImageView
                ivGambar.setImageBitmap(bitmap);
            } else {
                // Gambar tidak ditemukan
                ivGambar.setImageResource(R.drawable.holder);
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            ivGambar.setImageResource(R.drawable.ic_launcher_background);
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}