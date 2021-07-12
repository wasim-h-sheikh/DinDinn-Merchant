package com.dindinn.merchant.util

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Insets
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.dindinn.merchant.R
import com.dindinn.merchant.model.Order
import com.dindinn.merchant.ui.ingredients.IngredientFragment
import com.dindinn.merchant.ui.orders.OrderFragment
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}


fun String.getDateWithServerTimeStamp(): Date? {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm+SS'Z'",
        Locale.getDefault()
    )
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    try {
        return dateFormat.parse(this)
    } catch (e: ParseException) {
        return null
    }
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}


fun Activity.getScreenWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

fun String.toTimerFormat(): String {
    val parts = split(":")
    if (parts[0] == "0") {
        return parts[1] + " s"
    } else {
        return parts[0] + " mins"
    }
}

fun getIndicatorImage(time: String): Int {

    when (time.split(":")[0].toInt()) {
        1 -> return R.drawable.indicator1
        2 -> return R.drawable.indicator2
        3 -> return R.drawable.indicator3
        4 -> return R.drawable.indicator4
        5 -> return R.drawable.indicator5
    }
    return R.drawable.indicator1
}

fun TextView.setTextFromResource(resId: Int?) {
    resId?.let {
        text = context.resources.getString(it)
    }
}

fun View.getStringFromResource(resId: Int?): String? {
    resId?.let {
        return context.resources.getString(it)
    }
    return null
}

fun TextView.setTextColorFromResource(resId: Int?) {
    resId?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(context.resources.getColor(it, context.resources.newTheme()))
        } else {
            setTextColor(context.resources.getColor(it))
        }
    }
}

// Extension property to get default ringtone
val Context.defaultRingtone: Ringtone
    get() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return RingtoneManager.getRingtone(this, uri)
    }

fun Activity.setAlarm(order: Order) {

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(this, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(this, order.id!!, intent, 0)

    val alarmTimeAtUTC = order.alerted_at?.getDateWithServerTimeStamp()?.time ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC,
            pendingIntent
        )
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent)
    }


}

fun Activity.cancelAlarm(orderId: Int) {
    // Get AlarmManager instance
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, orderId, intent, 0)
    alarmManager.cancel(pendingIntent)

}

fun ImageView.loadImageFromUrl(url: String) {
    Glide.with(this).load(url).into(this)
}


const val ARG_CATEGORY_ID = "category_id"

const val ORDER_FRAGMENT_TAG = "order_fragment_tag"
const val INGREDIENT_FRAGMENT_TAG = "ingredient_fragment_tag"

fun FragmentManager.manageFragmentTransaction(selectedFrag: String) {
    when (selectedFrag) {
        ORDER_FRAGMENT_TAG -> {
            if (findFragmentByTag(ORDER_FRAGMENT_TAG) != null) {
                //if the fragment exists, show it.
                findFragmentByTag(ORDER_FRAGMENT_TAG)?.let { beginTransaction().show(it).commit() }
            } else {
                //if the fragment does not exist, add it to fragment manager.
                beginTransaction().add(
                    R.id.nav_host_fragment_content,
                    OrderFragment(),
                    ORDER_FRAGMENT_TAG
                )
                    .commit()
            }
            if (findFragmentByTag(INGREDIENT_FRAGMENT_TAG) != null) {
                //if the other fragment is visible, hide it.
                findFragmentByTag(INGREDIENT_FRAGMENT_TAG)?.let {
                    beginTransaction().hide(it).commit()
                }
            }
        }
        INGREDIENT_FRAGMENT_TAG -> {
            if (findFragmentByTag(INGREDIENT_FRAGMENT_TAG) != null) {
                //if the fragment exists, show it.
                findFragmentByTag(INGREDIENT_FRAGMENT_TAG)?.let {
                    beginTransaction().show(it).commit()
                }
            } else {
                //if the fragment does not exist, add it to fragment manager.
                beginTransaction().add(
                    R.id.nav_host_fragment_content,
                    IngredientFragment(),
                    INGREDIENT_FRAGMENT_TAG
                ).commit()
            }
            if (findFragmentByTag(ORDER_FRAGMENT_TAG) != null) {
                //if the other fragment is visible, hide it.
                findFragmentByTag(ORDER_FRAGMENT_TAG)?.let {
                    beginTransaction().hide(it).commit()
                }
            }
        }
    }
}