package aryananta.mobile.becash;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Produk.class}, version = 1)
public abstract class ProdukDatabase extends RoomDatabase {
    public abstract ProdukDao produkDao();
}
