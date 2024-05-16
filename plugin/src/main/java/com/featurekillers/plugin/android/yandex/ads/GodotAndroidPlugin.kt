package com.featurekillers.plugin.android.yandex.ads

import android.util.Log
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
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

    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardedAdUnitId: String? = null
    private var rewardedAdAutoload: Boolean = false
    private var rewardedAdLoading: Boolean = false

    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(
            SignalInfo("interstitial_ad_loaded"),
            SignalInfo("interstitial_ad_failed_to_load"),
            SignalInfo("interstitial_ad_shown"),
            SignalInfo("interstitial_ad_failed_to_show"),
            SignalInfo("interstitial_ad_dismissed"),
            SignalInfo("interstitial_ad_clicked"),
            SignalInfo("interstitial_ad_impression"),
            SignalInfo("rewarded_ad_loaded"),
            SignalInfo("rewarded_ad_failed_to_load"),
            SignalInfo("rewarded_ad_shown"),
            SignalInfo("rewarded_ad_failed_to_show"),
            SignalInfo("rewarded_ad_dismissed"),
            SignalInfo("rewarded_ad_clicked"),
            SignalInfo("rewarded_ad_impression"),
            SignalInfo("rewarded_ad_rewarded"),
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
                    if (!interstitialAdLoading) {
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
                        destroyInterstitialAd()

                        // Now you can preload the next interstitial ad.
                        if (interstitialAdAutoload) loadInterstitialAd()

                        emitSignal("interstitial_ad_failed_to_show")
                        Log.v(pluginName, "InterstitialAdFailedToShow")
                    }

                    override fun onAdDismissed() {
                        // Called when ad is dismissed.
                        // Clean resources after Ad dismissed
                        interstitialAd?.setAdEventListener(null)
                        interstitialAd = null

                        // Now you can preload the next interstitial ad.
                        if (interstitialAdAutoload) loadInterstitialAd()

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

    @UsedByGodot
    private fun setRewardedAdAutoload(adAutoload: Boolean) {
        rewardedAdAutoload = adAutoload
    }

    @UsedByGodot
    private fun setRewardedAdUnitId(adUnitId: String) {
        rewardedAdUnitId = adUnitId
    }

    @UsedByGodot
    private fun initRewardedAdLoader() {
        rewardedAdLoader ?: run {
            activity?.let {
                rewardedAdLoader = RewardedAdLoader(it).apply {
                    setAdLoadListener(object : RewardedAdLoadListener {
                        override fun onAdLoaded(ad: RewardedAd) {
                            rewardedAd = ad
                            rewardedAdLoading = false
                            emitSignal("rewarded_ad_loaded")
                            Log.v(pluginName, "RewardedAdLoaded")
                            // The ad was loaded successfully. Now you can show loaded ad.
                        }

                        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                            rewardedAdLoading = false
                            emitSignal("rewarded_ad_failed_to_load")
                            Log.v(pluginName, "RewardedAdFailedToLoad")
                            // Ad failed to load with AdRequestError.
                            // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                        }
                    })
                }
            }
        }
    }

    @UsedByGodot
    private fun loadRewardedAd() {
        rewardedAd ?: run {
            rewardedAdUnitId?.let {
                val adRequestConfiguration = AdRequestConfiguration.Builder(it).build()
                rewardedAdLoader?.let { adLoader ->
                    if (!rewardedAdLoading) {
                        rewardedAdLoading = true
                        adLoader.loadAd(adRequestConfiguration)
                    }
                }
            }
        }
    }

    @UsedByGodot
    private fun showRewardedAd() {
        activity?.let {
            rewardedAd?.apply {
                setAdEventListener(object : RewardedAdEventListener {
                    override fun onAdShown() {
                        emitSignal("rewarded_ad_shown")
                        Log.v(pluginName, "RewardedAdShown")
                        // Called when ad is shown.
                    }

                    override fun onAdFailedToShow(adError: AdError) {
                        // Called when an RewardedAd failed to show
                        rewardedAd?.setAdEventListener(null)
                        rewardedAd = null

                        // Now you can preload the next rewarded ad.
                        if (rewardedAdAutoload) loadRewardedAd()

                        emitSignal("rewarded_ad_failed_to_show")
                        Log.v(pluginName, "RewardedAdFailedToShow")
                    }

                    override fun onAdDismissed() {
                        // Called when ad is dismissed.
                        // Clean resources after Ad dismissed
                        rewardedAd?.setAdEventListener(null)
                        rewardedAd = null

                        // Now you can preload the next rewarded ad.
                        if (rewardedAdAutoload) loadRewardedAd()

                        emitSignal("rewarded_ad_dismissed")
                        Log.v(pluginName, "RewardedAdDismissed")
                    }

                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        emitSignal("rewarded_ad_clicked")
                        Log.v(pluginName, "RewardedAdClicked")
                    }

                    override fun onAdImpression(impressionData: ImpressionData?) {
                        // Called when an impression is recorded for an ad.
                        emitSignal("rewarded_ad_impression")
                        Log.v(pluginName, "RewardedAdImpression")
                    }

                    override fun onRewarded(reward: Reward) {
                        // Called when the user can be rewarded.
                        emitSignal("rewarded_ad_rewarded")
                        Log.v(pluginName, "RewardedAdRewarded")
                    }
                })
                show(it)
            }
        }
    }

    @UsedByGodot
    private fun destroyRewardedAdLoader() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        destroyRewardedAd()
    }

    @UsedByGodot
    private fun destroyRewardedAd() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }
}
