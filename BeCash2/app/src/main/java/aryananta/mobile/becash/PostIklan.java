package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class PostIklan extends AppCompatActivity implements View.OnClickListener {
    private Button btnPost;
    private ImageButton btnBack;
    private EditText etJudulBarang, etDeskripsi, etHargaOpenBid, etHargaBuyNow, etTanggal, etWaktu, et_nama;
    private ProdukDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_iklan);

        btnPost = this.findViewById(R.id.btn_post_iklan);
        btnBack = this.findViewById(R.id.backButton);


        et_nama = findViewById(R.id.et_nama);
        etJudulBarang = findViewById(R.id.et_judul_barang);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHargaOpenBid = findViewById(R.id.et_harga_open_bid);
        etHargaBuyNow = findViewById(R.id.et_harga_buy_now);
        etTanggal = findViewById(R.id.et_tanggal);
        etWaktu = findViewById(R.id.et_waktu);

        btnBack.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        etTanggal.setOnClickListener(this);
        etWaktu.setOnClickListener(this);
    }

    private void showDatePicker() {
        // Mengambil tanggal saat ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(PostIklan.this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Mengatur tanggal yang dipilih ke EditText
            etTanggal.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Mengambil waktu saat ini
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Membuat TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(PostIklan.this, (view, selectedHour, selectedMinute) -> {
            // Mengatur waktu yang dipilih ke EditText
            etWaktu.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void showSuccessDialog() {
        // Membuat dan menampilkan AlertDialog
        new AlertDialog.Builder(PostIklan.this)
                .setTitle("Sukses")
                .setMessage("Iklan berhasil diposting!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tutup dialog ketika tombol OK ditekan
                        Intent intent = new Intent(PostIklan.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private boolean areFieldsValid() {
        // Memeriksa apakah semua field terisi
        return !et_nama.getText().toString().trim().isEmpty() &&
                !etJudulBarang.getText().toString().trim().isEmpty() &&
                !etDeskripsi.getText().toString().trim().isEmpty() &&
                !etHargaOpenBid.getText().toString().trim().isEmpty() &&
                !etHargaBuyNow.getText().toString().trim().isEmpty() &&
                !etTanggal.getText().toString().trim().isEmpty() &&
                !etWaktu.getText().toString().trim().isEmpty();
    }
    private void showValidationError() {
        new AlertDialog.Builder(PostIklan.this)
                .setTitle("Error")
                .setMessage("Semua field harus diisi!")
                .setPositiveButton("OK", null)
                .show();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_post_iklan) {
            if (!areFieldsValid()) {
                // Jika field belum valid, tampilkan dialog kesalahan
                showValidationError();
                return;
            }
            new Thread(()->{
                db = DBInstance.getInstance(this);
                Produk produk = new Produk(
                        etJudulBarang.getText().toString(),
                        "",
                        et_nama.getText().toString(),
                        etDeskripsi.getText().toString(),
                        "Universitas Brawijaya",
                        etHargaOpenBid.getText().toString(),
                        "",
                        etTanggal.getText().toString(),
                        "",
                        etWaktu.getText().toString(),
                        "",
                        ""
                );
                db.produkDao().insert(produk);
            }).start();
            showSuccessDialog();
        }
        if (view.getId() == R.id.backButton) {
            Intent intent = new Intent(PostIklan.this, MainActivity.class);
            startActivity(intent);
            finish(); // Menutup ActivityB jika tidak ingin kembali lagi
        }
        if (view.getId() == R.id.et_tanggal) {
            showDatePicker();
        }
        if (view.getId() == R.id.et_waktu) {
            showTimePicker();
        }
    }
}