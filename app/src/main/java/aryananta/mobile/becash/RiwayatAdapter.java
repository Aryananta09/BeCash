package aryananta.mobile.becash;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder> {
    private final Context ctx;
    private List<Produk> lelangItems;

    public RiwayatAdapter(Context ctx, List<Produk> lelangItems) {
        this.ctx = ctx;
        this.lelangItems = lelangItems;
    }

    @Override
    public RiwayatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        return new RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiwayatViewHolder holder, int position) {
        Produk item = lelangItems.get(position);
        holder.tvNama.setText(item.getNama_barang());
        holder.tvHarga.setText(item.getHargaOpen());
        String base64Image = item.getGambar();
        if (base64Image != null) {
            Bitmap bitmap = Base64Image.base64ToBitmap(base64Image); // Konversi Base64 ke Bitmap
            holder.ivImage.setImageBitmap(bitmap); // Tampilkan di ImageView
        }
        else {
            holder.ivImage.setImageResource(R.drawable.holder);}
    }

    @Override
    public int getItemCount() {
        return lelangItems.size();
    }

    public static class RiwayatViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga;
        ImageView ivImage;

        public RiwayatViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
