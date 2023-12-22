package kedaiapps.projeku.testandroidenamdua.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kedaiapps.projeku.testandroidenamdua.db.table.ListBisnisTable

@Database(entities = [ListBisnisTable::class], version = 1)
abstract class EnamDuaDB : RoomDatabase(){

    companion object {
        private var INSTANCE: EnamDuaDB? = null

        fun getDatabase(context: Context): EnamDuaDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, EnamDuaDB::class.java, "EnamDuaDB")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as EnamDuaDB
        }
    }

    abstract fun daoListBisnis() : daoListBisnis
}