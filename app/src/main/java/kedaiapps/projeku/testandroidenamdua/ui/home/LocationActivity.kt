package kedaiapps.projeku.testandroidenamdua.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kedaiapps.projeku.testandroidenamdua.databinding.ActivityLocationBinding
import kedaiapps.projeku.testandroidenamdua.helper.DataLocation
import kedaiapps.projeku.testandroidenamdua.modules.base.BaseActivity
import kedaiapps.projeku.testandroidenamdua.ui.home.adapter.AdapterLocation
import java.util.Locale

class LocationActivity : BaseActivity() {
    lateinit var mBinding: ActivityLocationBinding

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AdapterLocation(::onClick)
    }

    private var location = ""
    private var lat = 0.0
    private var lon = 0.0
    private val LOCATION_PERMISSION_CODE = 100
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initToolbar()
        initView()
    }

    private fun initToolbar() {
        mBinding.tlbr.apply {
            tvTitle.text = "List Location"
        }
    }

    private fun initView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mBinding.rv.let {
            it.adapter = adapter
            adapter.insertData(DataLocation().getData())
        }

        mBinding.lineLocation.setOnClickListener {
            val intent = Intent()
            intent.putExtra("data", location)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun onClick(data: DataLocation) {
        val intent = Intent()
        intent.putExtra("data", data.title)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        if (checkForInternet(this)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                val dialog = DialogPrivacyLocation(this)
                dialog.listener = object: DialogPrivacyLocation.Listener{
                    override fun onYes() {
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
                    }
                }
                dialog.show()
            }else {
                getLastLocation()
            }
        }
//        else{
//            this.toast("Internet tidak tersedia")
//        }
    }

    // checking internet
    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun getData(lat1: Double, lon1: Double){

        lat = lat1
        lon = lon1

        val geocoder = Geocoder(this, Locale.getDefault())

        if(lat != 0.0 || lon != 0.0) {
            val address = geocoder.getFromLocation(lat!!, lon!!, 1)
            val alamat = address[0].locality

            location = alamat
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        getData(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation
            getData(location.latitude, location.longitude)
//            Log.d("BISMILLAH2", location.latitude.toString())
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}