extends Node

## Interface used to access the functionality provided by this plugin

signal interstitial_ad_loaded()
signal interstitial_ad_failed_to_load()
signal interstitial_ad_shown()
signal interstitial_ad_failed_to_show()
signal interstitial_ad_dismissed()
signal interstitial_ad_clicked()
signal interstitial_ad_impression()

var _plugin_name: = "YandexAdsGodotPlugin"
var _plugin_singleton: Object


func _init():
	if Engine.has_singleton(_plugin_name):
		_plugin_singleton = Engine.get_singleton(_plugin_name)
		_plugin_singleton.interstitial_ad_loaded.connect(
				func(): interstitial_ad_loaded.emit())
		_plugin_singleton.interstitial_ad_failed_to_load.connect(
				func(): interstitial_ad_failed_to_load.emit())
		_plugin_singleton.interstitial_ad_shown.connect(
				func(): interstitial_ad_shown.emit())
		_plugin_singleton.interstitial_ad_failed_to_show.connect(
				func(): interstitial_ad_failed_to_show.emit())
		_plugin_singleton.interstitial_ad_dismissed.connect(
				func(): interstitial_ad_dismissed.emit())
		_plugin_singleton.interstitial_ad_clicked.connect(
				func(): interstitial_ad_clicked.emit())
		_plugin_singleton.interstitial_ad_impression.connect(
				func(): interstitial_ad_impression.emit())
		
		_plugin_singleton.setInterstitialAdUnitId(
				ProjectSettings.get_setting("ads/yandex/interstitial unit id"))
		_plugin_singleton.setInterstitialAdAutoload(
				ProjectSettings.get_setting("ads/yandex/interstitial autoload ads"))
		if ProjectSettings.get_setting("ads/yandex/interstitial autostart"):
			_plugin_singleton.initInterstitialAdLoader()
			_plugin_singleton.loadInterstitialAd()
		
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func initInterstitialAdLoader():
	if _plugin_singleton:
		_plugin_singleton.initInterstitialAdLoader()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func loadInterstitialAd():
	if _plugin_singleton:
		_plugin_singleton.loadInterstitialAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func showInterstitialAd():
	if _plugin_singleton:
		_plugin_singleton.showInterstitialAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func destroyInterstitialAdLoader():
	if _plugin_singleton:
		_plugin_singleton.destroyInterstitialAdLoader()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func destroyInterstitialAd():
	if _plugin_singleton:
		_plugin_singleton.destroyInterstitialAd()
	else:
		push_warning("Unable to access the java YandexAds logic")
