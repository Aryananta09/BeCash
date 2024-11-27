package aryananta.mobile.becash;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class Produk {

    public String id;
    public String nama_barang;
    public String nama_penawar;
    public String nama_penjual;
    public String deskripsi;
    public String alamat;
    public String harga;
    public String tawaran;
    public String tanggal;
    public String tanggal_bid;
    public String waktu;
    public String waktu_bid;
    public String gambar;

    public Produk(){}

    public Produk(String id, String nama_barang, String nama_penawar, String nama_penjual, String deskripsi, String alamat, String harga, String tawaran, String tanggal, String tanggal_bid, String waktu, String waktu_bid, String gambar) {
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
    public String getId() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public void setNama_penawar(String nama_penawar) {
        this.nama_penawar = nama_penawar;
    }

    public void setNama_penjual(String nama_penjual) {
        this.nama_penjual = nama_penjual;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setTawaran(String tawaran) {
        this.tawaran = tawaran;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setTanggal_bid(String tanggal_bid) {
        this.tanggal_bid = tanggal_bid;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public void setWaktu_bid(String waktu_bid) {
        this.waktu_bid = waktu_bid;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
