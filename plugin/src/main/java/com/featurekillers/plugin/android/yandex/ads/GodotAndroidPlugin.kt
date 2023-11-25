package com.featurekillers.plugin.android.yandex.ads

import android.app.Activity
import android.util.Log
import android.view.View
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot) {
    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var interstitialAdUnitId: String? = null
    private var interstitialAdAutoload: Boolean = false
    private var interstitialAdLoading: Boolean = false
    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(
            SignalInfo("interstitial_ad_loaded"),
            SignalInfo("interstitial_ad_failed_to_load"),
            SignalInfo("interstitial_ad_shown"),
            SignalInfo("interstitial_ad_failed_to_show"),
            SignalInfo("interstitial_ad_dismissed"),
            SignalInfo("interstitial_ad_clicked"),
            SignalInfo("interstitial_ad_impression")
        )
    }

    @UsedByGodot
    private fun setInterstitialAdAutoload(adAutoload: Boolean) {
        interstitialAdAutoload = adAutoload
    }

    @UsedByGodot
    private fun setInterstitialAdUnitId(adUnitId: String) {
        interstitialAdUnitId = adUnitId
    }

    @UsedByGodot
    private fun initInterstitialAdLoader() {
        // Interstitial ads loading should occur after initialization of the SDK.
        // Initialize SDK as early as possible, for example in Application.onCreate or Activity.onCreate
        interstitialAdLoader ?: run {
            activity?.let {
                interstitialAdLoader = InterstitialAdLoader(it).apply {
                    setAdLoadListener(object : InterstitialAdLoadListener {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            interstitialAd = ad
                            interstitialAdLoading = false
                            emitSignal("interstitial_ad_loaded")
                            Log.v(pluginName, "InterstitialAdLoaded")
                            // The ad was loaded successfully. Now you can show loaded ad.
                        }

                        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                            interstitialAdLoading = false
                            emitSignal("interstitial_ad_failed_to_load")
                            Log.v(pluginName, "InterstitialAdFailedToLoad")
                            // Ad failed to load with AdRequestError.
                            // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                        }
                    })
                }
            }
        }
    }

    @UsedByGodot
    private fun loadInterstitialAd() {
        interstitialAd ?: run {
            interstitialAdUnitId?.let {
                val adRequestConfiguration = AdRequestConfiguration.Builder(it).build()
                interstitialAdLoader?.let { adLoader ->
                    if(!interstitialAdLoading) {
                        interstitialAdLoading = true
                        adLoader.loadAd(adRequestConfiguration)
                    }
                }
            }
        }
    }

    @UsedByGodot
    private fun showInterstitialAd() {
        activity?.let {
            interstitialAd?.apply {
                setAdEventListener(object : InterstitialAdEventListener {
                    override fun onAdShown() {
                        emitSignal("interstitial_ad_shown")
                        Log.v(pluginName, "InterstitialAdShown")
                        // Called when ad is shown.
                    }

                    override fun onAdFailedToShow(adError: AdError) {
                        // Called when an InterstitialAd failed to show.
                        // Clean resources after Ad dismissed
                        interstitialAd?.setAdEventListener(null)
                        interstitialAd = null

                        // Now you can preload the next interstitial ad.
                        if(interstitialAdAutoload) loadInterstitialAd()

                        emitSignal("interstitial_ad_failed_to_show")
                        Log.v(pluginName, "InterstitialAdFailedToShow")
                    }

                    override fun onAdDismissed() {
                        // Called when ad is dismissed.
                        // Clean resources after Ad dismissed
                        interstitialAd?.setAdEventListener(null)
                        interstitialAd = null

                        // Now you can preload the next interstitial ad.
                        if(interstitialAdAutoload) loadInterstitialAd()

                        emitSignal("interstitial_ad_dismissed")
                        Log.v(pluginName, "InterstitialAdDismissed")
                    }

                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        emitSignal("interstitial_ad_clicked")
                        Log.v(pluginName, "InterstitialAdClicked")
                    }

                    override fun onAdImpression(impressionData: ImpressionData?) {
                        // Called when an impression is recorded for an ad.
                        emitSignal("interstitial_ad_impression")
                        Log.v(pluginName, "InterstitialAdImpression")
                    }
                })
                // now you can use ads
                show(it)
            }
            interstitialAd ?: run {
                if(interstitialAdAutoload) loadInterstitialAd()
            }
        }
    }

    @UsedByGodot
    private fun destroyInterstitialAdLoader() {
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        destroyInterstitialAd()
    }

    @UsedByGodot
    private fun destroyInterstitialAd() {
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }
}
