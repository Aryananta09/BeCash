package aryananta.mobile.becash;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private ImageView btnBack;
    private TextView tvNama, tvPenjual, tvAlamat, tvBidPrice, tvBuyNowPrice, tvDeskripsi;
    private ImageView ivImageDetail;
    private Button btnBid;

    private DatabaseReference dbRef; // Firebase reference untuk Bid dan Produk
    private String produkId, judul, penjual, alamat, tanggal, waktu, bidPrice, buyNowPrice, deskripsi, gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnBack = findViewById(R.id.btBackToMain1);
        btnBid = findViewById(R.id.btBid);
        tvNama = findViewById(R.id.tvNamaDetail);
        tvPenjual = findViewById(R.id.tvPenjual);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvBidPrice = findViewById(R.id.tvBidPrice);
        tvBuyNowPrice = findViewById(R.id.tvBuyNowPrice);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        ivImageDetail = findViewById(R.id.ivImageDetail);

        // Mengambil referensi ke Firebase
        dbRef = FirebaseDatabase.getInstance(DBURL).getReference("bid");

        // Mendapatkan data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            produkId = extras.getString("id");
            judul = extras.getString("judul");
            penjual = extras.getString("penjual");
            alamat = extras.getString("alamat");
            tanggal = extras.getString("tanggal");
            waktu = extras.getString("waktu");
            bidPrice = extras.getString("hargaOpen");
            buyNowPrice = extras.getString("hargaBuyNow");
            deskripsi = extras.getString("deskripsi");
            gambar = extras.getString("gambar");

            tvNama.setText(judul);
            tvPenjual.setText(penjual);
            tvAlamat.setText(alamat);
            tvBidPrice.setText(bidPrice);
            tvBuyNowPrice.setText(buyNowPrice);
            tvDeskripsi.setText(deskripsi);

            loadImage(produkId);
        }

        btnBack.setOnClickListener(v -> onBackPressed());
        btnBid.setOnClickListener(this);
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
                ivImageDetail.setImageBitmap(bitmap);
            } else {
                // Gambar tidak ditemukan
                ivImageDetail.setImageResource(R.drawable.holder);
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            ivImageDetail.setImageResource(R.drawable.ic_launcher_background);
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btBid) {
            // Membuat EditText untuk input harga bid dan nama penawar
            EditText inputHarga = new EditText(this);
            EditText inputNama = new EditText(this);

            inputHarga.setHint("Masukkan harga bid");
            inputNama.setHint("Masukkan nama penawar");

            // Membuat LinearLayout untuk menampung kedua EditText
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(inputNama);
            layout.addView(inputHarga);

            // Membuat AlertDialog untuk input bid
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Masukkan Detail Bid");
            builder.setView(layout);

            builder.setPositiveButton("Selesai", (dialog, which) -> {
                String hargaBid = inputHarga.getText().toString();
                String namaBid = inputNama.getText().toString();

                // Validasi input
                if (TextUtils.isEmpty(namaBid) || TextUtils.isEmpty(hargaBid)) {
                    Toast.makeText(this, "Nama dan harga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mendapatkan tanggal dan waktu saat ini
                String tanggalBid = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String waktuBid = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                String bidId = dbRef.push().getKey();
                Bid newBid = new Bid(bidId, produkId, judul, penjual,namaBid, alamat,hargaBid, tanggalBid, waktuBid, "");

                // Simpan bid baru di Firebase
                dbRef.child(bidId).setValue(newBid)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Bid berhasil diperbarui!", Toast.LENGTH_SHORT).show();

                            // Memperbarui harga produk terkait setelah bid diperbarui
                            updateProduk(produkId, hargaBid, namaBid, tanggalBid, waktuBid);
                            Intent intent = new Intent(this, LelangLive.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Terjadi kesalahan saat memperbarui bid!", Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("Batal", null);

            builder.show();
        }
    }

    // Fungsi untuk memperbarui harga produk di Firebase setelah bid berhasil
    private void updateProduk(String produkId, String hargaBid, String namaPenawar, String tanggalBid, String waktuBid) {
        DatabaseReference dbProdukRef = FirebaseDatabase.getInstance().getReference("produk");

        dbProdukRef.child(produkId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Produk produk = snapshot.getValue(Produk.class);
                    if (produk != null) {
                        // Update harga produk dengan harga bid terbaru
                        produk.setTawaran(hargaBid);
                        produk.setNama_penawar(namaPenawar); // Nama penawar yang diinputkan
                        produk.setTanggal_bid(tanggalBid);   // Tanggal bid
                        produk.setWaktu_bid(waktuBid);       // Waktu bid

                        dbProdukRef.child(produkId).setValue(produk)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(DetailActivity.this, "Produk berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(DetailActivity.this, "Terjadi kesalahan saat memperbarui produk!", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Produk tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching product data: " + error.getMessage());
            }
        });
    }
}
