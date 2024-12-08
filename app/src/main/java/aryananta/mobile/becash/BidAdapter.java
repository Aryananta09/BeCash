package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private final Context ctx;
    private final List<Bid> koleksi;
    private DatabaseReference dbRef;

    public BidAdapter(Context ctx, List<Bid> koleksi){
        this.ctx = ctx;
        this.koleksi = koleksi;
    }

    public void setDbRef(DatabaseReference dbRef) {
        this.dbRef = dbRef;
    }

    private class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nama_barang;
        private final TextView nama_penjual;
        private final TextView alamat;
        private final TextView harga;
        private final TextView tanggal;
        private final TextView waktu;
        private final ImageView gambar;
        private final Button btTawar;

        private Bid bid;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.nama_barang = itemView.findViewById(R.id.tv_namaBarang);
            this.nama_penjual = itemView.findViewById(R.id.tv_penjual);
            this.alamat = itemView.findViewById(R.id.tv_Alamat);
            this.harga = itemView.findViewById(R.id.tv_Harga);
            this.tanggal = itemView.findViewById(R.id.tv_Tanggal);
            this.waktu = itemView.findViewById(R.id.tv_Waktu);
            this.gambar = itemView.findViewById(R.id.imageView);
            this.btTawar = itemView.findViewById(R.id.btTawar);
            btTawar.setOnClickListener(this);
        }
        private void setBid(Bid b){

            this.bid = b;
        }

        @Override
        public void onClick(View view) {
            try {
                // Membuat EditText untuk input harga bid dan nama penawar
                EditText inputHarga = new EditText(ctx);
                EditText inputNama = new EditText(ctx);

                inputHarga.setHint("Masukkan harga bid");
                inputNama.setHint("Masukkan nama penawar");

                // Membuat LinearLayout untuk menampung kedua EditText
                LinearLayout layout = new LinearLayout(ctx);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(inputNama);
                layout.addView(inputHarga);

                // Membuat AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Masukkan Detail Bid");
                builder.setView(layout);

                builder.setPositiveButton("Selesai", (dialog, which) -> {
                    String hargaBid = inputHarga.getText().toString();
                    String namaBid = inputNama.getText().toString();

                    // Validasi input
                    if (namaBid.isEmpty() || hargaBid.isEmpty()) {
                        Toast.makeText(ctx, "Nama dan harga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Mendapatkan tanggal dan waktu saat ini
                    String tanggalBid, waktuBid;
                    try {
                        SimpleDateFormat sdfTanggal = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                        SimpleDateFormat sdfWaktu = new SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());
                        Date sekarang = new Date();

                        tanggalBid = sdfTanggal.format(sekarang); // Format tanggal
                        waktuBid = sdfWaktu.format(sekarang);     // Format waktu
                    } catch (Exception e) {
                        tanggalBid = "";
                        waktuBid = "";
                    }

                    // Update Firebase Database untuk Bid
                    if (dbRef != null && bid != null) {
                        String bidId = bid.getId(); // Pastikan `Bid` memiliki metode getId()
                        if (bidId == null) {
                            Toast.makeText(ctx, "ID produk tidak valid!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Buat objek Bid yang telah diperbarui
                        Bid updatedBid = new Bid(
                                bidId,
                                bid.getProdukid(),
                                bid.getNama_barang(),
                                bid.getNama_penjual(),
                                namaBid, // Nama penawar yang diinputkan
                                bid.getAlamat(),
                                hargaBid, // Harga bid yang diinputkan
                                tanggalBid, // Tanggal saat bid dimasukkan
                                waktuBid, // Waktu saat bid dimasukkan
                                bid.getGambar()
                        );

                        // Update bid di Firebase
                        String finalTanggalBid = tanggalBid;
                        String finalWaktuBid = waktuBid;
                        dbRef.child(bidId).setValue(updatedBid)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ctx, "Bid berhasil diperbarui!", Toast.LENGTH_SHORT).show();

                                    // Setelah bid diperbarui, update produk yang terkait
                                    updateProduk(bid.getProdukid(), hargaBid, namaBid, finalTanggalBid, finalWaktuBid);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseError", "Error updating bid", e);
                                    Toast.makeText(ctx, "Terjadi kesalahan saat memperbarui bid!", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(ctx, "Referensi database tidak ditemukan!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Batal", null);

                // Menampilkan AlertDialog
                builder.show();

            } catch (Exception e) {
                Log.e("Error", "Unexpected error occurred", e);
                Toast.makeText(ctx, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.ctx).inflate(R.layout.item_bid, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Bid b =this.koleksi.get(position);
        VH vh = (VH) holder;
        vh.nama_barang.setText(b.getNama_barang());
        vh.nama_penjual.setText(b.getNama_penjual());
        vh.harga.setText((b.getHarga() == null || b.getHarga().isEmpty()) ? "Belum Ada Tawaran" : "RP " + b.getHarga());
        vh.alamat.setText(b.getAlamat());
        vh.tanggal.setText(b.getTanggal());
        vh.waktu.setText(b.getWaktu());
        String base64Image = b.getGambar();
        if (base64Image != null) {
            Bitmap bitmap = Base64Image.base64ToBitmap(base64Image); // Konversi Base64 ke Bitmap
            vh.gambar.setImageBitmap(bitmap); // Tampilkan di ImageView
        }
        else {
            vh.gambar.setImageResource(R.drawable.holder);}
        vh.setBid(b);
    }

    @Override
    public int getItemCount() {
        return koleksi.size();
    }
    private void updateProduk(String produkId, String hargaBid, String namaPenawar, String tanggalBid, String waktuBid) {
        DatabaseReference dbProdukRef = FirebaseDatabase.getInstance(DBURL).getReference("produk");

        // Ambil produk berdasarkan produkId
        dbProdukRef.child(produkId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Produk produk = snapshot.getValue(Produk.class);
                    if (produk != null) {

                        produk.setTawaran(hargaBid);
                        produk.setNama_penawar(namaPenawar);
                        produk.setTanggal_bid(tanggalBid);
                        produk.setWaktu_bid(waktuBid);

                        // Update produk di Firebase
                        dbProdukRef.child(produkId).setValue(produk)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ctx, "Produk berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseError", "Error updating product", e);
                                    Toast.makeText(ctx, "Terjadi kesalahan saat memperbarui produk!", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(ctx, "Produk tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching product data: " + error.getMessage());
            }
        });
    }

}
