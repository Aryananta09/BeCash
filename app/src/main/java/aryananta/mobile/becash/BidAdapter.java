package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.content.Context;
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

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter {

    private final Context ctx;
    private final List<Produk> koleksi;
    private DatabaseReference dbRef;

    public BidAdapter(Context ctx, List<Produk> koleksi){
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

        private Produk bid;

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
        private void setBid(Produk b){

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

                    // Update Firebase Database
                    if (dbRef != null && bid != null) {
                        // Data untuk diupdate
                        String bidId = bid.getId(); // Pastikan `Produk` memiliki metode getId()
                        if (bidId == null) {
                            Toast.makeText(ctx, "ID produk tidak valid!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Produk updatedProduk = new Produk(
                                bid.getId(),
                                bid.getNama_barang(),
                                namaBid,
                                bid.getNama_penjual(),
                                bid.getDeskripsi(),
                                bid.getAlamat(),
                                bid.getHarga(),
                                hargaBid,
                                bid.getTanggal(),
                                tanggalBid,
                                bid.getWaktu(),
                                waktuBid,
                                ""
                        );

                        dbRef.child(bidId).setValue(updatedProduk)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(ctx, "Bid berhasil diperbarui!", Toast.LENGTH_SHORT).show())
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
        Produk b =this.koleksi.get(position);
        VH vh = (VH) holder;
        vh.nama_barang.setText(b.nama_barang);
        vh.nama_penjual.setText(b.nama_penjual);
        vh.harga.setText((b.tawaran == null || b.tawaran.isEmpty()) ? "Belum Ada Tawaran" : "RP " + b.tawaran);
        vh.alamat.setText(b.alamat);
        vh.tanggal.setText(b.tanggal_bid);
        vh.waktu.setText(b.waktu_bid);
//        vh.gambar.setImageResource(R.drawable.holder);

        switch (b.nama_barang){
            case "Sepatu Nike":
                vh.gambar.setImageResource(R.drawable.sepatu_nike);
                break;
            case "Sepatu Converse":
                vh.gambar.setImageResource(R.drawable.sepatu_converse);
                break;
            case "Televisi":
                vh.gambar.setImageResource(R.drawable.img_tv);
                break;
            case "Iphone 12":
                vh.gambar.setImageResource(R.drawable.img_iphone);
                break;
            case "Rubicon":
                vh.gambar.setImageResource(R.drawable.rubicon);
                break;
            case "Innova":
                vh.gambar.setImageResource(R.drawable.innova);
                break;
            case "Kursi Kayu":
                vh.gambar.setImageResource(R.drawable.kursi_kayu);
                break;
            case "Lemari Antik":
                vh.gambar.setImageResource(R.drawable.lemari_antik);
                break;
            case "Meja Belajar":
                vh.gambar.setImageResource(R.drawable.meja_belajar);
                break;
            case "Rak Meja":
                vh.gambar.setImageResource(R.drawable.img_tv);
                break;
            default: vh.gambar.setImageResource(R.drawable.holder);
            break;
        }

        vh.setBid(b);
    }

    @Override
    public int getItemCount() {
        return koleksi.size();
    }
}
