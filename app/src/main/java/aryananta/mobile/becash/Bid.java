package aryananta.mobile.becash;

public class Bid {
    private String id;
    private String produkid;
    private String nama_barang;
    private String nama_penjual;
    private String penawar;
    private String alamat;
    private String harga;
    private String tanggal;
    private String waktu;
    private String gambar;

    // Constructor
    public Bid() {}
    public Bid(String id,String produkid, String nama_barang, String nama_penjual,String penawar, String alamat, String harga, String tanggal, String waktu, String gambar) {
        this.id = id;
        this.produkid = produkid;
        this.nama_barang = nama_barang;
        this.nama_penjual = nama_penjual;
        this.penawar = penawar;
        this.alamat = alamat;
        this.harga = harga;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.gambar = gambar;
    }


    public String getId() {
        return id;
    }

    public String getProdukid() {
        return produkid;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public String getNama_penjual() {
        return nama_penjual;
    }

    public String getPenawar() {
        return penawar;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getHarga() {
        return harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getGambar() {
        return gambar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProdukid(String produkid) {
        this.produkid = produkid;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public void setNama_penjual(String nama_penjual) {
        this.nama_penjual = nama_penjual;
    }

    public void setPenawar(String penawar) {
        this.penawar = penawar;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
