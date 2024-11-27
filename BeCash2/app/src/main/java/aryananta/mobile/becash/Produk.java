package aryananta.mobile.becash;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Produk {

    @PrimaryKey (autoGenerate = true)
    public int id;
    @ColumnInfo(name = "nama_barang")
    public String nama_barang;
    @ColumnInfo(name = "nama_penawar")
    public String nama_penawar;
    @ColumnInfo(name = "nama_penjual")
    public String nama_penjual;
    @ColumnInfo(name = "deskripsi")
    public String deskripsi;
    @ColumnInfo(name = "alamat")
    public String alamat;
    @ColumnInfo(name = "harga")
    public String harga;
    @ColumnInfo(name = "tawaran")
    public String tawaran;
    @ColumnInfo(name = "tanggal")
    public String tanggal;
    @ColumnInfo(name = "tanggal_bid")
    public String tanggal_bid;
    @ColumnInfo(name = "waktu")
    public String waktu;
    @ColumnInfo(name = "waktu_bid")
    public String waktu_bid;
    @ColumnInfo(name = "gambar")
    public String gambar;

    public Produk(String nama_barang, String nama_penawar, String nama_penjual, String deskripsi, String alamat, String harga, String tawaran, String tanggal, String tanggal_bid, String waktu, String waktu_bid, String gambar) {
        this.id = id;
        this.nama_barang = nama_barang;
        this.nama_penawar = nama_penawar;
        this.nama_penjual = nama_penjual;
        this.deskripsi = deskripsi;
        this.alamat = alamat;
        this.harga = harga;
        this.tawaran = tawaran;
        this.tanggal = tanggal;
        this.tanggal_bid = tanggal_bid;
        this.waktu = waktu;
        this.waktu_bid = waktu_bid;
        this.gambar = gambar;
    }
    public int getId() {
        return id;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public String getNama_penawar() {
        return nama_penawar;
    }

    public String getNama_penjual() {
        return nama_penjual;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getHarga() {
        return harga;
    }

    public String getTawaran() {
        return tawaran;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTanggal_bid() {
        return tanggal_bid;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getWaktu_bid() {
        return waktu_bid;
    }

    public String getGambar() {
        return gambar;
    }

}
