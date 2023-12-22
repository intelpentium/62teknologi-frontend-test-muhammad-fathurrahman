package kedaiapps.projeku.testandroidenamdua.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kedaiapps.projeku.testandroidenamdua.common.ActionLiveData
import kedaiapps.projeku.testandroidenamdua.common.UiState
import kedaiapps.projeku.testandroidenamdua.ext.errorMesssage
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHome
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeDetail
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeDetailReviews
import kedaiapps.projeku.testandroidenamdua.services.rest.MainRest
import kedaiapps.projeku.testandroidenamdua.utils.DownloadRepository
import kedaiapps.projeku.testandroidenamdua.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRest: MainRest,
    private val repo: DownloadRepository,
    private val fileUtils: FileUtils
) : ViewModel() {

    val loadState = ActionLiveData<UiState>()

    val responseHome = ActionLiveData<ResponseHome>()
    val responseHomeDetail = ActionLiveData<ResponseHomeDetail>()
    val responseHomeDetailReviews = ActionLiveData<ResponseHomeDetailReviews>()

//    private val fileUtils : FileUtils = FileUtils(viewModelScope, applicationContext)

    fun home(location: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            loadState.sendAction(UiState.Loading)
            try {
                val response = mainRest.home(location, offset, limit)
                responseHome.value = response
                loadState.sendAction(UiState.Success)
            } catch (e: Exception) {
                loadState.sendAction(UiState.Error(e.errorMesssage))
            }
        }
    }

    fun homeDetail(bisnisId: String) {
        viewModelScope.launch {
            loadState.sendAction(UiState.Loading)
            try {
                val response = mainRest.homeDetail(bisnisId)
                responseHomeDetail.value = response
                loadState.sendAction(UiState.Success)
            } catch (e: Exception) {
                loadState.sendAction(UiState.Error(e.errorMesssage))
            }
        }
    }

    fun homeDetailReview(bisnisId: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            loadState.sendAction(UiState.Loading)
            try {
                val response = mainRest.homeDetailReviews(bisnisId, offset, limit)
                responseHomeDetailReviews.value = response
                loadState.sendAction(UiState.Success)
            } catch (e: Exception) {
                loadState.sendAction(UiState.Error(e.errorMesssage))
            }
        }
    }
}