package aryananta.mobile.becash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

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
        private final TextView tvHarga;
        private final TextView tvTanggal;
        private final TextView tvWaktu;
        private final ImageView gambar;
        private final Button btDetail;
        private Produk itemProduk;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvWaktu = itemView.findViewById(R.id.tvWaktu);
            gambar = itemView.findViewById(R.id.ivImage);
            btDetail = itemView.findViewById(R.id.btBid);
            btDetail.setOnClickListener(this);
        }
        private void setProduk(Produk p){
            this.itemProduk = p;
        }

        @Override
        public void onClick(View view) {
            String id = this.itemProduk.getId();
            String judul = this.itemProduk.nama_barang;
            String penjual = this.itemProduk.nama_penjual;
            String alamat = this.itemProduk.alamat;
            String harga = this.itemProduk.hargaOpen;
            String tanggal = this.itemProduk.tanggal;
            String waktu = this.itemProduk.waktu;
            String deskripsi = this.itemProduk.deskripsi;

            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("judul", judul);
            bundle.putString("penjual", penjual);
            bundle.putString("alamat", alamat);
            bundle.putString("harga", harga);
            bundle.putString("tanggal", tanggal);
            bundle.putString("waktu", waktu);
            bundle.putString("deskripsi", deskripsi);

            Intent intent = new Intent(ctx, DetailItem.class);
            intent.putExtras(bundle);
            ctx.startActivity(intent);
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
        vh.tvHarga.setText("RP " + p.hargaOpen);
        vh.tvTanggal.setText(p.tanggal);
        vh.tvWaktu.setText(p.waktu);
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
        return this.koleksi.size();
    }
}
