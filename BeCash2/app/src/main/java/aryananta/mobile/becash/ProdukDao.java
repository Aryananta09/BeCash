package aryananta.mobile.becash;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProdukDao {
    @Query("SELECT * FROM Produk")
    List<Produk> getAll();

    @Insert
    void insert(Produk... produk);

    @Update
    void update(Produk... produk);

    @Query("UPDATE Produk SET nama_penawar = :namaPenawar, tawaran = :hargaTawaran, tanggal_bid = :tanggalBid, waktu_bid = :waktuBid WHERE id = :id")
    void updateBid(int id, String namaPenawar, String hargaTawaran, String tanggalBid, String waktuBid);

    @Delete
    void delete(Produk produk);
}
