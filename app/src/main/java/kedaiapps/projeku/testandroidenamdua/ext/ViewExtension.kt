package kedaiapps.projeku.testandroidenamdua.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import kedaiapps.projeku.testandroidenamdua.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.setColor(@ColorRes colorRes: Int) {
    return this.setBackgroundColor(ContextCompat.getColor(context, colorRes))
}
fun TextView.setColor(@ColorRes colorRes: Int) {
    return this.setTextColor(ContextCompat.getColor(context, colorRes))
}
fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = resources.getColor(R.color.primary_base)
                textPaint.isUnderlineText = false
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}


fun String.toHtml(): String {
   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        "${Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)}"
    } else {
        "${Html.fromHtml(this)}"
    }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun TextView.currency(number: Double){
    val formatter = DecimalFormat("#,###")
    this.setText("Rp ${formatter.format(number)}")
}

fun getDateNow():String{
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val now = Date()
    return sdf.format(now)
}

fun getTimeNow():String{
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val now = Date()
    return sdf.format(now)
}

fun getRupiah(number: Double): String{
    val localeID =  Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(number).toString()
}

@SuppressLint("SimpleDateFormat")
fun getConvertDate(start: String, end: String, nilai: String):String{

    val fmt = SimpleDateFormat(start)
    val d = fmt.parse(nilai)
    val format = SimpleDateFormat(end)

    return format.format(d!!)
}

@SuppressLint("SimpleDateFormat")
fun getConvertTime(start: String, end: String, nilai: String): String{

    val fmt2 = SimpleDateFormat(start)
    val d2 = fmt2.parse(nilai)
    val format2 = SimpleDateFormat(end)

    return format2.format(d2!!)
}

@SuppressLint("SimpleDateFormat")
fun getformatDateTime(orderTime: String, format: String): String{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat(format)

    var formattedDate=""
    if(format == "HH:mm"){
        val date = inputFormat.parse(orderTime)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, 7)
        formattedDate = outputFormat.format(calendar.time)
    }else {
        val date = inputFormat.parse(orderTime)
        formattedDate = outputFormat.format(date)
    }
    return formattedDate
}

@SuppressLint("SimpleDateFormat")
fun getformatDateTimePayment(orderTime: String, format: String): String{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat(format)
    val date = inputFormat.parse(orderTime)
    val formattedDate = outputFormat.format(date)
//    println(formattedDate) // prints 10-04-2018

    return formattedDate
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun NavController.safeNavigate(direction: NavDirections) {
//    Log.d(clickTag, "Click happened")
    currentDestination?.getAction(direction.actionId)?.run {
//        Log.d(clickTag, "Click Propagated")
        navigate(direction)
    }
}

fun View.blockingClickListener(debounceTime: Long = 1200L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            val timeNow = SystemClock.elapsedRealtime()
            val elapsedTimeSinceLastClick = timeNow - lastClickTime
//            Log.d(clickTag, """
//                        DebounceTime: $debounceTime
//                        Time Elapsed: $elapsedTimeSinceLastClick
//                        Is within debounce time: ${elapsedTimeSinceLastClick < debounceTime}
//                    """.trimIndent())

            if (elapsedTimeSinceLastClick < debounceTime) {
//                Log.d(clickTag, "Double click shielded")
                return
            }
            else {
//                Log.d(clickTag, "Click happened")
                action()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun Long.toCurrency(currency: String = "", locale: Locale = Locale("id")): String {
    val formatter = NumberFormat.getCurrencyInstance(locale) as DecimalFormat
    with(formatter) {
        decimalFormatSymbols = decimalFormatSymbols.apply {
            currencySymbol = currency
        }
        maximumFractionDigits = 0
    }
    return "Rp ${formatter.format(this)}"
}

fun String.toRupiah(currency: String = "0"): String {
    val check = this.indexOf('.')
    return if (check < 0) this.toLong().toCurrency() else this.substring(0, check).toLong().toCurrency()
}

fun Long.toRupiah(): String {
    return this.toCurrency()
}

fun getDateTimeNow():String{
    val sdf = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault())
    val now = Date()
    return sdf.format(now)
}


