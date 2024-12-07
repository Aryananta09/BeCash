package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LelangAdapter extends RecyclerView.Adapter<LelangAdapter.LelangViewHolder> {
    private Context ctx;
    private DatabaseReference dbRefProduk, dbRefBid;
    private List<Produk> lelangItems;
    private OnItemClickListener onItemClickListener;

    public LelangAdapter(Context ctx, List<Produk> lelangItems, OnItemClickListener onItemClickListener) {
        this.ctx = ctx;
        this.lelangItems = lelangItems;
        this.onItemClickListener = onItemClickListener;
    }
    public void setDbRef(DatabaseReference dbRef, DatabaseReference dbRefBid) {
        this.dbRefProduk = dbRef;
        this.dbRefBid = dbRefBid;
    }

    public class LelangViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNama, tvHarga;
        ImageView ivImage;
        Button btTawar;
        Produk produk;

        public LelangViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivImage = itemView.findViewById(R.id.ivImage);
            btTawar = itemView.findViewById(R.id.btBid);
            btTawar.setOnClickListener(this);
        }
        private void setProduk(Produk p){
            this.produk = p;
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
                    if (dbRefProduk != null && produk != null) {
                        // Data untuk diupdate
                        String produkId = produk.getId(); // Pastikan `Produk` memiliki metode getId()
                        if (produkId == null) {
                            Toast.makeText(ctx, "ID produk tidak valid!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Produk updatedProduk = new Produk(
                                produk.getId(),
                                produk.getStatus(),
                                produk.getNama_barang(),
                                namaBid,
                                produk.getNama_penjual(),
                                produk.getDeskripsi(),
                                produk.getAlamat(),
                                produk.getHargaOpen(),
                                produk.getHargaBuyNow(),
                                hargaBid,
                                produk.getTanggal(),
                                tanggalBid,
                                produk.getWaktu(),
                                waktuBid,
                                produk.getGambar()
                        );

                        dbRefProduk.child(produkId).setValue(updatedProduk)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(ctx, "Bid berhasil diperbarui!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseError", "Error updating bid", e);
                                    Toast.makeText(ctx, "Terjadi kesalahan saat memperbarui bid!", Toast.LENGTH_SHORT).show();
                                });
                        String bidId = dbRefBid.push().getKey();
                        Bid bid = new Bid(
                                bidId,
                                produk.getId(),
                                produk.getNama_barang(),
                                produk.getNama_penjual(),
                                namaBid,
                                produk.getAlamat(),
                                hargaBid,
                                tanggalBid,
                                waktuBid,
                                produk.getGambar()
                        );
                        dbRefBid.child(bidId).setValue(bid);

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

    @Override
    public LelangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lelang, parent, false);
        return new LelangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LelangViewHolder holder, int position) {
        Produk item = lelangItems.get(position);
        holder.tvNama.setText(item.nama_barang);
        holder.tvHarga.setText(item.hargaOpen);
        String img = item.getGambar();
        if (img != null) {
            Bitmap bitmap = Base64Image.base64ToBitmap(img); // Konversi Base64 ke Bitmap
            holder.ivImage.setImageBitmap(bitmap); // Tampilkan di ImageView
        }
        else {
            holder.ivImage.setImageResource(R.drawable.holder);}

        holder.setProduk(item);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lelangItems.size();
    }

    public void setLelangList(List<Produk> lelangItems) {
        this.lelangItems = lelangItems;
        notifyDataSetChanged(); // Perbarui tampilan RecyclerView
    }



    public interface OnItemClickListener {
        void onItemClick(Produk item);
    }
}
