package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter {

    private final Context ctx;
    private final List<Produk> koleksi;
    private DatabaseReference dbRef;

    public MainAdapter(Context ctx, List<Produk> koleksi){
        this.ctx = ctx;
        this.koleksi = koleksi;
    }

    public void setDbRef(DatabaseReference dbRef) {
        this.dbRef = dbRef;
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvJudul;
        private final TextView tvDeskripsi;
        private final TextView tvHarga;
        private final TextView tvTanggal;
        private final TextView tvWaktu;
        private final ImageView gambar;
        private final Button btBid;
        private Produk itemProduk;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvWaktu = itemView.findViewById(R.id.tvWaktu);
            gambar = itemView.findViewById(R.id.imageView2);
            btBid = itemView.findViewById(R.id.btBid);
            btBid.setOnClickListener(this);
        }
        private void setProduk(Produk p){
            this.itemProduk = p;
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
                        SimpleDateFormat sdfTanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        SimpleDateFormat sdfWaktu = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        Date sekarang = new Date();

                        tanggalBid = sdfTanggal.format(sekarang); // Format tanggal
                        waktuBid = sdfWaktu.format(sekarang);     // Format waktu
                    } catch (Exception e) {
                        tanggalBid = "";
                        waktuBid = "";
                    }

                    // Update Firebase Database
                    if (dbRef != null && itemProduk != null) {
                        // Data untuk diupdate
                        String bidId = itemProduk.getId(); // Pastikan `Produk` memiliki metode getId()
                        if (bidId == null) {
                            Toast.makeText(ctx, "ID produk tidak valid!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Produk updatedProduk = new Produk(
                                itemProduk.getId(),
                                itemProduk.getNama_barang(),
                                namaBid,
                                itemProduk.getNama_penjual(),
                                itemProduk.getDeskripsi(),
                                itemProduk.getAlamat(),
                                itemProduk.getHarga(),
                                hargaBid,
                                itemProduk.getTanggal(),
                                tanggalBid,
                                itemProduk.getWaktu(),
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
        View v = LayoutInflater.from(this.ctx).inflate(R.layout.item_main, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Produk p =this.koleksi.get(position);
        VH vh = (VH) holder;
        vh.tvJudul.setText(p.nama_barang);
        vh.tvDeskripsi.setText(p.deskripsi);
        vh.tvHarga.setText("RP " + p.harga);
        vh.tvTanggal.setText(p.tanggal);
        vh.tvWaktu.setText(p.waktu);
        switch (p.nama_barang) {
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
            default:
                vh.gambar.setImageResource(R.drawable.holder);
                break;
        }
        vh.setProduk(p);
    }

    @Override
    public int getItemCount() {
        return this.koleksi.size();
    }
}
