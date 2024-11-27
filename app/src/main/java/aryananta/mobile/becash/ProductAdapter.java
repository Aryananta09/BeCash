package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Produk> dataset;
    private DatabaseReference dbRef;

    public ProductAdapter(Context context, List<Produk> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    public void setDBRef(DatabaseReference dbRef) {
        this.dbRef = dbRef;
    }

    private class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nama_barang;
        private final TextView nama_penawar;
        private final TextView harga;
        private final TextView tanggal;
        private final TextView waktu;
        private final ImageView gambar;
        private final Button btEnd;

        private Produk produk;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.nama_barang = itemView.findViewById(R.id.tv_namaBarang);
            this.nama_penawar = itemView.findViewById(R.id.tv_penawar);
            this.harga = itemView.findViewById(R.id.tvHarga);
            this.tanggal = itemView.findViewById(R.id.tv_Tanggal);
            this.waktu = itemView.findViewById(R.id.tv_Waktu);
            this.gambar = itemView.findViewById(R.id.imageView);
            this.btEnd = itemView.findViewById(R.id.btEnd);

            this.btEnd.setOnClickListener(this);
        }

        private void setProduk(Produk b) {
            this.produk = b;
        }

        @Override
        public void onClick(View view) {
            // Membuat AlertDialog untuk konfirmasi
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus item ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Jika pengguna menekan "Ya", hapus data
                        dbRef.child(this.produk.getId()).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(view.getContext(), "Item berhasil dihapus", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(view.getContext(), "Gagal menghapus item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Tidak", (dialog, which) -> {
                        // Jika pengguna menekan "Tidak", tutup dialog
                        dialog.dismiss();
                    })
                    .show();
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Produk p = this.dataset.get(position);
        VH vh = (VH) holder;
        vh.nama_barang.setText(p.nama_barang);
        vh.nama_penawar.setText(p.nama_penawar);
        vh.harga.setText((p.tawaran == null || p.tawaran.isEmpty()) ? "Belum Ada Tawaran" : "RP " + p.tawaran);
        vh.tanggal.setText(p.tanggal_bid);
        vh.waktu.setText(p.waktu_bid);

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
        return dataset.size();
    }

}
