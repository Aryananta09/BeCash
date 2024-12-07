package aryananta.mobile.becash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MainLiveAdapter extends RecyclerView.Adapter {

    private final Context ctx;
    private final List<Produk> koleksi;
    private DatabaseReference dbRef;

    public MainLiveAdapter(Context ctx, List<Produk> koleksi) {
        this.ctx = ctx;
        this.koleksi = koleksi;
    }
    public void setDbRef(DatabaseReference dbRef) {
        this.dbRef = dbRef;
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ivGambar;
        private final TextView tvJudul, tvHarga;
        private Produk itemProduk;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            itemView.setOnClickListener(this);
        }
        private void setProduk(Produk p){
            this.itemProduk = p;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ctx, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(itemProduk.getId()));
            bundle.putString("judul", itemProduk.getNama_barang());
            bundle.putString("penjual", itemProduk.getNama_penjual());
            bundle.putString("alamat", itemProduk.getAlamat());
            bundle.putString("hargaOpen", itemProduk.getHargaOpen());
            bundle.putString("hargaBuyNow", itemProduk.getHargaBuyNow());
            bundle.putString("tanggal", itemProduk.getTanggal());
            bundle.putString("waktu", itemProduk.getWaktu());
            bundle.putString("deskripsi", itemProduk.getDeskripsi());
            bundle.putString("gambar", itemProduk.getGambar());

            intent.putExtras(bundle);
            ctx.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.ctx).inflate(R.layout.item_mainlive, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Produk p =this.koleksi.get(position);
        VH vh = (VH) holder;
        vh.tvJudul.setText(p.nama_barang);
        vh.tvHarga.setText(p.hargaOpen);
        String base64Image = p.gambar;
        if (base64Image != null) {
            Bitmap bitmap = Base64Image.base64ToBitmap(base64Image); // Konversi Base64 ke Bitmap
            vh.ivGambar.setImageBitmap(bitmap); // Tampilkan di ImageView
        }
        else {
        vh.ivGambar.setImageResource(R.drawable.holder);}
        vh.setProduk(p);
    }

    @Override
    public int getItemCount() {
        return this.koleksi.size();
    }
}
