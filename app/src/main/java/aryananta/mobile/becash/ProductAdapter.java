package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
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
                        // Ambil produkId dari produk yang dihapus
                        String produkId = produk.getId();
                        // Hapus bid terkait produk tersebut
                        deleteRelatedBids(produkId, view);

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
        String base64Image = p.gambar;
        if (base64Image != null) {
            Bitmap bitmap = Base64Image.base64ToBitmap(base64Image); // Konversi Base64 ke Bitmap
            vh.gambar.setImageBitmap(bitmap); // Tampilkan di ImageView
        }
        else {
            vh.gambar.setImageResource(R.drawable.holder);}
        vh.setProduk(p);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
    // Fungsi untuk menghapus bid terkait produkId
    private void deleteRelatedBids(String produkId, View view) {
        DatabaseReference bidsRef = FirebaseDatabase.getInstance(DBURL).getReference("bid");

        // Cari semua bid yang terkait dengan produkId yang diberikan
        bidsRef.orderByChild("produkId").equalTo(produkId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Loop untuk menghapus setiap bid yang terkait dengan produkId
                            for (DataSnapshot bidSnapshot : dataSnapshot.getChildren()) {
                                bidSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            // Hapus bid berhasil
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(view.getContext(), "Gagal menghapus bid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(view.getContext(), "Gagal mengambil data bid: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
