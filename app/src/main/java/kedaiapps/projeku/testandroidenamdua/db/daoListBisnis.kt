package kedaiapps.projeku.testandroidenamdua.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kedaiapps.projeku.testandroidenamdua.db.table.ListBisnisTable

@Dao
interface daoListBisnis {
    @Query("SELECT * FROM ListBisnisTable")
    fun getAll() : LiveData<List<ListBisnisTable>>

    @Query("SELECT * FROM ListBisnisTable WHERE id=:id")
    fun getById(id: String): LiveData<ListBisnisTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: ListBisnisTable)

//    @Query("UPDATE ListBisnisTable SET status=:status WHERE id=:id")
//    fun update(id: String, status: String)

    @Query("DELETE FROM ListBisnisTable")
    fun deleteAll()
}