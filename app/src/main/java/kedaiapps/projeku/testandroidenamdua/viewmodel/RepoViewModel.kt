package kedaiapps.projeku.testandroidenamdua.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kedaiapps.projeku.testandroidenamdua.common.storage.Preferences
import kedaiapps.projeku.testandroidenamdua.db.EnamDuaDB
import kedaiapps.projeku.testandroidenamdua.db.table.ListBisnisTable
import kedaiapps.projeku.testandroidenamdua.ext.ioThread
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    val application: Application,
    val preferences: Preferences
) : ViewModel() {

    val db = EnamDuaDB.getDatabase(this.application)

    //======================= Local Database ListBisnis ===================
    fun setListBisnis(bisnisId: String, name: String, rating: String, phone: String, display_phone: String, image: String) {
        ioThread {
            db.daoListBisnis().insert(ListBisnisTable(0, bisnisId, name, rating, phone, display_phone, image))
        }
    }

    fun getListBisnis(): LiveData<List<ListBisnisTable>> {
        return db.daoListBisnis().getAll()
    }

    fun getListBisnisId(fav_id: String): LiveData<ListBisnisTable> {
        return db.daoListBisnis().getById(fav_id)
    }

//    fun updateListBisnis(fav_id: String, status: String){
//        ioThread {
//            db.daoListBisnis().update(fav_id, status)
//        }
//    }

    fun deleteListBisnis() {
        ioThread {
            db.daoListBisnis().deleteAll()
        }
    }

    fun setCurrentPage(currentPage: Int) {
        preferences.currentPage = currentPage
    }
    fun getCurrentPage() = preferences.currentPage
}