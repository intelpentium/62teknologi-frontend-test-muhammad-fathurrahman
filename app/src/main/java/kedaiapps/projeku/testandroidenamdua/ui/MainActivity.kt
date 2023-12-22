package kedaiapps.projeku.testandroidenamdua.ui

import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.databinding.ActivityMainBinding
import kedaiapps.projeku.testandroidenamdua.ext.toast
import kedaiapps.projeku.testandroidenamdua.modules.base.BaseActivity

class MainActivity : BaseActivity() {
    lateinit var mBinding: ActivityMainBinding

    // --- untuk back to close app ---
    private var mRecentlyBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navController = findNavController(R.id.navHost)
        navController.setGraph(R.navigation.navigation)
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.navHost)

        if(navController.graph.startDestinationId == navController.currentDestination?.id){
            if (mRecentlyBackPressed) {
                ActivityCompat.finishAffinity(this)
            } else {
                toast("Tekan sekali lagi untuk keluar")
                mRecentlyBackPressed = true
            }
        }else {
            super.onBackPressed()
            mRecentlyBackPressed = false
        }
    }
}