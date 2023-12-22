package kedaiapps.projeku.testandroidenamdua.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.common.UiState
import kedaiapps.projeku.testandroidenamdua.databinding.FragmentHomeDetailBinding
import kedaiapps.projeku.testandroidenamdua.ext.getDateTimeNow
import kedaiapps.projeku.testandroidenamdua.ext.observe
import kedaiapps.projeku.testandroidenamdua.ext.toast
import kedaiapps.projeku.testandroidenamdua.modules.base.BaseFragment
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeDetail
import kedaiapps.projeku.testandroidenamdua.ui.home.adapter.AdapterReviews
import kedaiapps.projeku.testandroidenamdua.viewmodel.MainViewModel


class HomeDetailFragment : BaseFragment(), OnMapReadyCallback {
    lateinit var mBinding: FragmentHomeDetailBinding
    private val viewModel by viewModels<MainViewModel>()
    private val args by navArgs<HomeDetailFragmentArgs>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AdapterReviews()
    }

    private var dataImages : List<String> = emptyList()

    private var offset = 0
    private var limit = 10
    private var total = 800
    private var isLoading = true
    private val layoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    private var lat:Double? = 0.0
    private var lon:Double? = 0.0
    lateinit var gMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeDetailBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initView()
        handleState()
    }

    private fun initToolbar() {
        mBinding.tlbr.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvTitle.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initView() {
        viewModel.homeDetail(args.bisnisId)
        viewModel.homeDetailReview(args.bisnisId, offset, limit)

        mBinding.carouselView.setViewListener { position ->
            val custom: View = layoutInflater.inflate(R.layout.item_list_banner, null)

            val ivBanner = custom.findViewById<ImageView>(R.id.ivBanner)

            Glide.with(custom.context).load(dataImages[position])
                .apply(
                    RequestOptions()
                        .transform(RoundedCorners(16))
                        .error(R.drawable.im_dummy)
                        .dontAnimate()
                ).into(ivBanner)
            return@setViewListener custom
        }

        mBinding.rv.layoutManager = layoutManager
        mBinding.rv.adapter = adapter
//        mBinding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                if (isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1 &&
//                    offset < total
//                ) {
//
//                    offset += limit
//                    isLoading = false
//                    mBinding.progress.progress.isVisible = true
//
//                    viewModel.homeDetailReview(args.bisnisId, offset, limit)
//                }
//            }
//        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun handleState() {
        observe(viewModel.responseHomeDetail) {
            if (it != null) {
                setData(it)
            }
        }

        observe(viewModel.responseHomeDetailReviews) {
            if (it != null) {
                if (offset == 0) {
                    adapter.clearData()
                    adapter.insertData(it.reviews)
                } else {
                    adapter.insertData(it.reviews)
                    isLoading = true
                    mBinding.progress.progress.isVisible = false
                }

                total = it.total
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
                    mBinding.progress.progress.isVisible = false
                    requireActivity().toast(it.message)
                }
            }
        }

    }

    private fun setData(data: ResponseHomeDetail) {

        mBinding.tlbr.tvTitle.text = data.name

        dataImages = data.photos
        mBinding.carouselView.pageCount = data.photos.size

        mBinding.name.text = data.name
        mBinding.name.text = data.name
        mBinding.rating.text = "Rating : "+data.rating

        lat = data.coordinates.latitude.toDouble()
        lon = data.coordinates.longitude.toDouble()

        gMap.addMarker(MarkerOptions().position(LatLng(lat!!, lon!!)))
        zoomCamera(lat!!, lon!!)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
    }

    private fun zoomCamera(lat: Double, lon: Double){
        val location = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 13f)
        gMap.animateCamera(location)
    }
}