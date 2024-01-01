extends Control



func _on_show_rewarded_pressed() -> void:
	YandexAds.showRewardedAd()


func _on_show_interstitial_pressed() -> void:
	YandexAds.showInterstitialAd()
