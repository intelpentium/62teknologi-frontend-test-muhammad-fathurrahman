package kedaiapps.projeku.testandroidenamdua.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kedaiapps.projeku.testandroidenamdua.databinding.FragmentOnboardBinding
import kedaiapps.projeku.testandroidenamdua.modules.base.BaseFragment

class OnboardFragment : BaseFragment() {
    lateinit var mBinding: FragmentOnboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentOnboardBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        mBinding.btnSubmit.setOnClickListener {
            findNavController().navigate(OnboardFragmentDirections.actionOnboardFragmentToHomeFragment())
        }
    }
}