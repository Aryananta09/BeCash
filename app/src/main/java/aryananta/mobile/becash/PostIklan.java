package aryananta.mobile.becash;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class PostIklan extends AppCompatActivity implements View.OnClickListener {
    public static final String
            DBURL = "https://becash-8f35d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final int PICK_IMAGE_REQUEST = 1;

    public String base64Image;
    private Button btnPost;
    private ImageButton btnBack;
    private EditText etJudulBarang, etDeskripsi, etHargaOpenBid, etHargaBuyNow, etTanggal, etWaktu, et_nama, etAlamat;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private ImageButton btnPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_iklan);

        btnPost = this.findViewById(R.id.btn_post_iklan);
        btnBack = this.findViewById(R.id.backButton);
        btnPhoto = this.findViewById(R.id.bt_foto_barang);


        et_nama = findViewById(R.id.et_nama);
        etJudulBarang = findViewById(R.id.et_judul_barang);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHargaOpenBid = findViewById(R.id.et_harga_open_bid);
        etHargaBuyNow = findViewById(R.id.et_harga_buy_now);
        etTanggal = findViewById(R.id.et_tanggal);
        etWaktu = findViewById(R.id.et_waktu);
        etAlamat = findViewById(R.id.et_alamat);

        btnBack.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        etTanggal.setOnClickListener(this);
        etWaktu.setOnClickListener(this);

        this.db = FirebaseDatabase.getInstance(DBURL);
        this.dbRef = this.db.getReference("produk");

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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                // Konversi URI ke Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // Tampilkan gambar pada ImageButton
                btnPhoto.setImageBitmap(bitmap);

                // Encode gambar ke Base64
                base64Image = Base64Image.bitmapToBase64(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_post_iklan) {
            if (!areFieldsValid()) {
                // Jika field belum valid, tampilkan dialog kesalahan
                showValidationError();
                return;
            }
            String id = this.dbRef.push().getKey();
            Produk p = new Produk();
            p.setId(id);
            p.setStatus("tidak aktif");
            p.setNama_barang(etJudulBarang.getText().toString());
            p.setNama_penjual(et_nama.getText().toString());
            p.setNama_penawar("Belum ada penawar");
            p.setDeskripsi(etDeskripsi.getText().toString());
            p.setAlamat(etAlamat.getText().toString());
            p.setHargaOpen(etHargaOpenBid.getText().toString());
            p.setHargaBuyNow(etHargaBuyNow.getText().toString());
            p.setTanggal(etTanggal.getText().toString());
            p.setWaktu(etWaktu.getText().toString());
            p.setGambar(base64Image);

            this.dbRef.child(id).setValue(p);
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
        if (view.getId() == R.id.bt_foto_barang) {
            openGallery();
        }
    }
}