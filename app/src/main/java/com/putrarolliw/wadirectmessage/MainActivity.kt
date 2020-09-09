package com.putrarolliw.wadirectmessage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.hbb20.BuildConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        //init Admob
        MobileAds.initialize(this)
        //set interstial
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3844487552229866/2193285089"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        et1.setDefaultCountryUsingNameCode("ID")
        btnSend.setOnClickListener {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
                openWhatsApp()
            }
        }
        ivShare.setOnClickListener {
            shareApp()
        }
        loadBannerAd()

        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(AdRequest.Builder().build())
                openWhatsApp()
            }
        }
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                print(p0)
            }
        }
    }

    private fun openWhatsApp() {
        try {
            val text = tvMessage.text // Replace with your message.
            var phone = tvNumber.text.toString()
                 // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is Indonesia and your phone number is “xxxxxxxxxx”, then you need to send “62xxxxxxxxxx”.
            if (phone?.substring(0, 1) == "0") {
                val newStr = phone.removeRange(0, 1)
                phone = et1.selectedCountryCode + newStr
            }else {
                phone = et1.selectedCountryCode + phone
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$phone&text=$text")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WhatsApp Direct Message")
        var shareMessage =
            "\nStart a new WhatsApp conversation without saving the contact number\n"
        shareMessage =
            "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".trimIndent() + "\n" + " Thanks a lot"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }
}