package aryananta.mobile.becash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Produk> dataset;

    public ProductAdapter(Context context, List<Produk> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    private class VH extends RecyclerView.ViewHolder{
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
        }
        private void setProduk(Produk b){
            this.produk = b;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_product, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Produk p = this.dataset.get(position);
        VH vh = (VH) holder;
        vh.nama_barang.setText(p.nama_barang);
        vh.nama_penawar.setText(p.nama_penawar);
        if (p.tawaran.isEmpty()) {
            vh.harga.setText("-");
        }
        else {
            vh.harga.setText("RP " +p.tawaran);
        }
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
        vh.btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) { // Pastikan posisi valid
                    Produk toDelete = dataset.get(currentPosition);

                    // Tampilkan pesan Toast
                    Toast.makeText(context, "Anda telah mengakhiri lelang produk ini", Toast.LENGTH_SHORT).show();

                    // Hapus dari database di thread terpisah
                    new Thread(() -> {
                        ProdukDatabase db = DBInstance.getInstance(context);
                        db.produkDao().delete(toDelete);
                    }).start();

                    // Hapus dari koleksi dan perbarui tampilan
                    dataset.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
