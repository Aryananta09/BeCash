package aryananta.mobile.becash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentProduct extends Fragment {

    private RecyclerView rvProduct;
    private ArrayList<Produk> dataset;
    private ProductAdapter koleksiProductAdapter;
    private Handler handler;
    private ProdukDatabase db;

    public FragmentProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        rvProduct = v.findViewById(R.id.rvProduct);
        dataset = new ArrayList<>();
        koleksiProductAdapter = new ProductAdapter(getContext(), dataset);
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProduct.setAdapter(koleksiProductAdapter);

        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                koleksiProductAdapter.notifyDataSetChanged();
            }
        };
        this.db = DBInstance.getInstance(requireContext());

        Thread t = new Thread(() -> {
            List<Produk> data = this.db.produkDao().getAll();
            this.dataset.clear();
            this.dataset.addAll(data);
            this.handler.sendMessage(this.handler.obtainMessage());
        });
        t.start();
        return v;
    }
}