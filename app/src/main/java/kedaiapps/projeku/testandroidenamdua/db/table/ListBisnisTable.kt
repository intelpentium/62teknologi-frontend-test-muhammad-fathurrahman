package kedaiapps.projeku.testandroidenamdua.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListBisnisTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bisnisId: String,
    val name: String,
    val rating: String,
    val phone: String,
    val display_phone: String,
    val image: String,
)