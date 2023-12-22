package kedaiapps.projeku.testandroidenamdua.ui.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import kedaiapps.projeku.testandroidenamdua.databinding.DialogPrivacyLocationBinding

class DialogPrivacyLocation (val context: Activity): Dialog(context) {

    lateinit var mBinding: DialogPrivacyLocationBinding
    interface Listener {
        fun onYes()
    }

    var listener : Listener?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mBinding = DialogPrivacyLocationBinding.inflate(LayoutInflater.from(getContext()))
        setContentView(mBinding.root)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        mBinding.btnSubmit.setOnClickListener {
            listener?.onYes()
            dismiss()
        }

        mBinding.tvBack.setOnClickListener {
            dismiss()
        }
    }
}