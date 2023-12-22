package kedaiapps.projeku.testandroidenamdua.common.storage

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(private val prefs: SharedPreferences) {

    var token: String by PreferenceData(prefs, "token", "")
    var currentPage: Int by PreferenceData(prefs, "currentPage", 1)

    fun clear() {
        prefs.edit().clear().apply()
    }
}