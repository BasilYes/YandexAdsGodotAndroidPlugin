extends Node

## Interface used to access the functionality provided by this plugin

signal interstitial_ad_loaded()
signal interstitial_ad_failed_to_load()
signal interstitial_ad_shown()
signal interstitial_ad_failed_to_show()
signal interstitial_ad_dismissed()
signal interstitial_ad_clicked()
signal interstitial_ad_impression()

signal rewarded_ad_loaded()
signal rewarded_ad_failed_to_load()
signal rewarded_ad_shown()
signal rewarded_ad_failed_to_show()
signal rewarded_ad_dismissed()
signal rewarded_ad_clicked()
signal rewarded_ad_impression()
signal rewarded_ad_rewarded()

var _plugin_name: = "YandexAdsGodotPlugin"
var _plugin_singleton: Object = null

func is_working() -> bool:
	return _plugin_singleton != null

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
		
		_plugin_singleton.rewarded_ad_loaded.connect(
				func(): rewarded_ad_loaded.emit())
		_plugin_singleton.rewarded_ad_failed_to_load.connect(
				func(): rewarded_ad_failed_to_load.emit())
		_plugin_singleton.rewarded_ad_shown.connect(
				func(): rewarded_ad_shown.emit())
		_plugin_singleton.rewarded_ad_failed_to_show.connect(
				func(): rewarded_ad_failed_to_show.emit())
		_plugin_singleton.rewarded_ad_dismissed.connect(
				func(): rewarded_ad_dismissed.emit())
		_plugin_singleton.rewarded_ad_clicked.connect(
				func(): rewarded_ad_clicked.emit())
		_plugin_singleton.rewarded_ad_impression.connect(
				func(): rewarded_ad_impression.emit())
		_plugin_singleton.rewarded_ad_rewarded.connect(
				func(): rewarded_ad_rewarded.emit())
		
		_plugin_singleton.setInterstitialAdUnitId(
			ProjectSettings.get_setting("ads/yandex/interstitial unit id", "demo-interstitial-yandex")
		)
		_plugin_singleton.setInterstitialAdAutoload(
			ProjectSettings.get_setting("ads/yandex/interstitial autoload ads", false)
		)
		if ProjectSettings.get_setting("ads/yandex/interstitial autostart", false):
			_plugin_singleton.initInterstitialAdLoader()
			_plugin_singleton.loadInterstitialAd()
		
		_plugin_singleton.setRewardedAdUnitId(
			ProjectSettings.get_setting("ads/yandex/rewarded unit id", "demo-rewarded-yandex")
		)
		_plugin_singleton.setRewardedAdAutoload(
			ProjectSettings.get_setting("ads/yandex/rewarded autoload ads", false)
		)
		if ProjectSettings.get_setting("ads/yandex/rewarded autostart"):
			_plugin_singleton.initRewardedAdLoader()
			_plugin_singleton.loadRewardedAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func init_interstitial_ad_loader():
	if _plugin_singleton:
		_plugin_singleton.initInterstitialAdLoader()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func load_interstitial_ad():
	if _plugin_singleton:
		_plugin_singleton.loadInterstitialAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func show_interstitial_ad():
	if _plugin_singleton:
		_plugin_singleton.showInterstitialAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func destroy_interstitial_ad_loader():
	if _plugin_singleton:
		_plugin_singleton.destroyInterstitialAdLoader()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func destroy_interstitial_ad():
	if _plugin_singleton:
		_plugin_singleton.destroyInterstitialAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func load_rewarded_ad():
	if _plugin_singleton:
		_plugin_singleton.loadRewardedAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func show_rewarded_ad():
	if _plugin_singleton:
		_plugin_singleton.showRewardedAd()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func destroy_rewarded_ad_loader():
	if _plugin_singleton:
		_plugin_singleton.destroyRewardedAdLoader()
	else:
		push_warning("Unable to access the java YandexAds logic")

## 
func destroy_rewarded_ad():
	if _plugin_singleton:
		_plugin_singleton.destroyRewardedAd()
	else:
		push_warning("Unable to access the java YandexAds logic")
