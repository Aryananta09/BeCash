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

public class FragmentBid extends Fragment {
    private RecyclerView rvBid;
    private BidAdapter koleksiBidAdapter;
    private ArrayList<Produk> koleksi;
    private ProdukDatabase db;
    private Handler handler;

    public FragmentBid() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bid, container, false);

        koleksi = new ArrayList<>();
        rvBid = v.findViewById(R.id.rvProduct);
        koleksiBidAdapter = new BidAdapter(getContext(), koleksi);
        rvBid.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBid.setAdapter(koleksiBidAdapter);

        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                koleksiBidAdapter.notifyDataSetChanged();
            }
        };
        this.db = DBInstance.getInstance(requireContext());

        Thread t = new Thread(() -> {
            List<Produk> data = this.db.produkDao().getAll();
            this.koleksi.clear();
            this.koleksi.addAll(data);
            this.handler.sendMessage(this.handler.obtainMessage());
        });
        t.start();

        return v;
    }
}