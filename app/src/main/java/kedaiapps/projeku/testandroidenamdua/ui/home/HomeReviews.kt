package kedaiapps.projeku.testandroidenamdua.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.common.UiState
import kedaiapps.projeku.testandroidenamdua.databinding.FragmentReviewBinding
import kedaiapps.projeku.testandroidenamdua.ext.observe
import kedaiapps.projeku.testandroidenamdua.ext.toast
import kedaiapps.projeku.testandroidenamdua.modules.base.BaseFragment
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeDetail
import kedaiapps.projeku.testandroidenamdua.ui.home.adapter.AdapterReviews
import kedaiapps.projeku.testandroidenamdua.viewmodel.MainViewModel

class HomeReviews : BaseFragment() {
    lateinit var mBinding: FragmentReviewBinding
    private val viewModel by viewModels<MainViewModel>()
    private val args by navArgs<HomeDetailFragmentArgs>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AdapterReviews()
    }

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
        mBinding = FragmentReviewBinding.inflate(inflater, container, false)
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
        viewModel.homeDetailReview(args.bisnisId, offset, limit)

        mBinding.rv.layoutManager = layoutManager
        mBinding.rv.adapter = adapter
        mBinding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1 &&
                    offset < total
                ) {

                    offset += limit
                    isLoading = false
                    mBinding.progress.progress.isVisible = true

                    viewModel.homeDetailReview(args.bisnisId, offset, limit)
                }
            }
        })
    }

    private fun handleState() {
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
}