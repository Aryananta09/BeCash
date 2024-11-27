package aryananta.mobile.becash;

import android.content.Context;

import androidx.room.Room;

public class DBInstance {
    private static ProdukDatabase INSTANCE;

    public static ProdukDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ProdukDatabase.class, "produk-db").build();
        }
        return INSTANCE;
    }
}
