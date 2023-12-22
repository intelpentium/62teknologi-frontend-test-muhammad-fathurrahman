package kedaiapps.projeku.testandroidenamdua.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.common.UiState
import kedaiapps.projeku.testandroidenamdua.databinding.FragmentHomeBinding
import kedaiapps.projeku.testandroidenamdua.db.table.ListBisnisTable
import kedaiapps.projeku.testandroidenamdua.ext.hideKeyboard
import kedaiapps.projeku.testandroidenamdua.ext.observe
import kedaiapps.projeku.testandroidenamdua.ext.toast
import kedaiapps.projeku.testandroidenamdua.modules.base.BaseFragment
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeItem
import kedaiapps.projeku.testandroidenamdua.ui.home.adapter.AdapterHome
import kedaiapps.projeku.testandroidenamdua.viewmodel.MainViewModel
import kedaiapps.projeku.testandroidenamdua.viewmodel.RepoViewModel
import java.util.Collections
import java.util.Locale
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {
    lateinit var mBinding: FragmentHomeBinding
    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AdapterHome(::onClick)
    }

    private var list: List<ResponseHomeItem> = ArrayList()
    private var sort: Boolean = true

    // home
    private var location = "New York City"
    private var offset = 0
    private var limit = 10
    private var total = 800
    private var isLoading = true
    private val layoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleState()
    }

    private fun initView() {

        mBinding.tvLocation.text = location
        viewModel.home(location, offset, limit)

        mBinding.tvLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivityForResult(intent, 1);
        }

        mBinding.search.addTextChangedListener {
            val filteredModelList: List<ResponseHomeItem> = filter(list, it.toString())
            adapter.clearData()
            adapter.insertData(filteredModelList)
        }

        mBinding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        mBinding.ivSort.setOnClickListener {
            mBinding.search.setText("")
            if(sort){
                sortAsc()
            }else{
                sortDesc()
            }
        }

        // home
        mBinding.rv.layoutManager = layoutManager
        mBinding.rv.adapter = adapter
        mBinding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1 &&
                    offset < total && mBinding.search.text.toString() == ""
                ) {

                    offset += limit
                    isLoading = false
                    mBinding.progress.progress.isVisible = true

                    viewModel.home(location, offset, limit)
                }
            }
        })
    }

    private fun filter(
        models: List<ResponseHomeItem>,
        query: String
    ): List<ResponseHomeItem> {

        val filteredModelList: MutableList<ResponseHomeItem> = ArrayList()
        for (model in models) {
            val name: String = model.name.toLowerCase()
            if (name.contains(query.toLowerCase())) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    private fun sortAsc() {
        sort = false
        val sortAZ =
            Comparator<ResponseHomeItem> { data1, data2 -> data1.name.compareTo(data2.name) }
        Collections.sort(list, sortAZ)
        adapter.clearData()
        adapter.insertData(list)

        mBinding.ivSort.setImageResource(R.drawable.ic_sort_down)
    }

    private fun sortDesc() {
        sort = true
        val sortZA =
            Comparator<ResponseHomeItem> { data1, data2 -> data2.name.compareTo(data1.name) }
        Collections.sort(list, sortZA)
        adapter.clearData()
        adapter.insertData(list)

        mBinding.ivSort.setImageResource(R.drawable.ic_sort_up)
    }

    private fun handleState() {

        //home
        observe(viewModel.responseHome) {
            if(it != null){
                if(offset == 0) {
                    adapter.clearData()
                    adapter.insertData(it.businesses)
                } else {
                    adapter.insertData(it.businesses)
                    isLoading = true
                    mBinding.progress.progress.isVisible = false
                }

                total = it.total
                list = it.businesses
            }
        }

        // loading
        observe(viewModel.loadState) {
            when (it) {
                UiState.Loading -> mBinding.progress.progress.isVisible = true
                UiState.Success -> {
                    mBinding.progress.progress.isVisible = false
                }

                is UiState.Error -> {
                    adapter.clearData()
                    mBinding.progress.progress.isVisible = false
                    requireActivity().toast("Data tidak tersedia")
                }
            }
        }
    }

    private fun onClick(data: ResponseHomeItem) {
        mBinding.rv.layoutManager = null
        mBinding.search.setText("")
        offset = 0
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToHomeDetailFragment(
                data.id
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            if (data != null) {
                location = data.extras!!.get("data").toString()
            }

            adapter.clearData()
            offset = 0
            mBinding.tvLocation.text = location
            viewModel.home(location, offset, limit)
        }
    }
}