package com.marvel.characters.util

import android.R.id.message
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.marvel.characters.R
import com.google.android.material.appbar.AppBarLayout
import java.util.*


object ActivityUtils {
    fun shareApplication(activity: AppCompatActivity) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val palystoreApplink =
            "http://play.google.com/store/apps/details?id=".plus(activity.packageName)
        val subject = activity.getString(R.string.app_name)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n" + palystoreApplink)
        activity.startActivity(
            Intent.createChooser(
                sharingIntent,
                activity.getString(R.string.share_via)
            )
        )
    }

    fun shareToOtherApps(activity: AppCompatActivity, text: String) {
        // share
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun openLink(activity: AppCompatActivity, url: String) {
        try {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            showMessage(
                activity, activity.getString(R.string.error),
                activity.getString(R.string.error_while_open_link), null, false,
                activity.getString(R.string.cancel), activity.getString(R.string.ok), true
            )
        }
    }

    fun sendEmail(activity: AppCompatActivity, email: String, subject: String, message: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        //need this to prompts email client only
        emailIntent.type = "message/rfc822"

        activity.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }

    fun chatViaWhatsApp(activity: AppCompatActivity, whatsAppNumber: String) {
        try {
            var number = whatsAppNumber.replace(" ", "").replace("+", "")

            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net"
            )
            activity.startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(activity, "ERROR OPEN WhatsApp", Toast.LENGTH_LONG).show()
        }
    }

    fun openGoogleMap(
        activity: AppCompatActivity,
        latitude: String,
        longitude: String,
        isUrl: Boolean,
        locationUrl: String
    ) {
        try {
            if (isUrl) {
                try {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl)))
                } catch (e: Exception) {
                    showMessage(
                        activity, activity.getString(R.string.error),
                        activity.getString(R.string.error_while_open_link), null, false,
                        activity.getString(R.string.cancel), activity.getString(R.string.ok), true
                    )
                }
            } else {
                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                activity.startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "ERROR OPEN Google Map", Toast.LENGTH_LONG).show()
        }
    }

    fun setSwipeRefreshLayoutColor(
        activity: AppCompatActivity,
        swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    ) {
        var colorPrimaryDark = 0
        colorPrimaryDark = ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        swipeRefreshLayout.setColorSchemeColors(
            colorPrimaryDark,
            colorPrimaryDark,
            colorPrimaryDark
        )
    }

    fun sendSMSTo(activity: AppCompatActivity, Number: String, subject: String) {
        val uri = Uri.parse("smsto:$Number")
        val smsIntent = Intent(Intent.ACTION_SENDTO, uri)
        smsIntent.putExtra("sms_body", subject)
        activity.startActivity(smsIntent)
    }

    fun replaceCurrentFragment(
        activity: AppCompatActivity,
        container: Int,
        targetFragment: Fragment,
        tag: String,
        addToBackStack: Boolean,
        duplicateIfInBackStack: Boolean,
        animate: Boolean
    ) {
        if (activity.isFinishing) {
            return
        }

        val manager = activity.supportFragmentManager
        var fragmentPopped = false
        if (!duplicateIfInBackStack)
            fragmentPopped = manager.popBackStackImmediate(tag, 0)
        if (duplicateIfInBackStack || (!fragmentPopped && manager.findFragmentByTag(tag) == null)) {
//        if (!fragmentPopped) {
            val ft = manager.beginTransaction()
            if (animate) ft.setCustomAnimations(
                R.anim.slide_from_right_to_left,
                R.anim.slide_in_left,
                R.anim.slide_out_left,
                R.anim.slide_from_left_to_right
            )
            ft.replace(container, targetFragment, tag)
            activity.supportFragmentManager.executePendingTransactions()
            if (addToBackStack) {
                ft.addToBackStack(tag)
            }
            ft.commitAllowingStateLoss()
        }
    }

    fun AppBarLayout.collapsingPercentage() =
        Math.abs(this.height - this.bottom) / this.totalScrollRange.toFloat()
}