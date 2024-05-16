@tool
extends EditorPlugin

# A class member to hold the editor export plugin during its lifecycle.
var export_plugin : YandexAdsExportPlugin


func _enter_tree():
	# Initialization of the plugin goes here.
	export_plugin = YandexAdsExportPlugin.new()
	add_export_plugin(export_plugin)
	
	var setting_path: = "ads/yandex/interstitial unit id"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, "demo-interstitial-yandex")
	ProjectSettings.set_initial_value(setting_path, "demo-interstitial-yandex")
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({
		"name": setting_path,
		"type": TYPE_STRING,
		"hint": PROPERTY_HINT_PASSWORD
	})
	ProjectSettings.set_as_internal(setting_path, false)
	
	setting_path = "ads/yandex/interstitial autostart"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, false)
	ProjectSettings.set_initial_value(setting_path, false)
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({ "name": setting_path, "type": TYPE_BOOL})
	ProjectSettings.set_as_internal(setting_path, false)
	
	setting_path = "ads/yandex/interstitial autoload ads"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, false)
	ProjectSettings.set_initial_value(setting_path, false)
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({ "name": setting_path, "type": TYPE_BOOL})
	ProjectSettings.set_as_internal(setting_path, false)
	
	setting_path = "ads/yandex/rewarded unit id"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, "demo-rewarded-yandex")
	ProjectSettings.set_initial_value(setting_path, "demo-rewarded-yandex")
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({
		"name": setting_path,
		"type": TYPE_STRING,
		"hint": PROPERTY_HINT_PASSWORD
	})
	ProjectSettings.set_as_internal(setting_path, false)
	
	setting_path = "ads/yandex/rewarded autostart"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, false)
	ProjectSettings.set_initial_value(setting_path, false)
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({ "name": setting_path, "type": TYPE_BOOL})
	ProjectSettings.set_as_internal(setting_path, false)
	
	setting_path = "ads/yandex/rewarded autoload ads"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, false)
	ProjectSettings.set_initial_value(setting_path, false)
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({ "name": setting_path, "type": TYPE_BOOL})
	ProjectSettings.set_as_internal(setting_path, false)
	
	ProjectSettings.save()


func _exit_tree():
	# Clean-up of the plugin goes here.
	remove_export_plugin(export_plugin)
	export_plugin = null

func _enable_plugin() -> void:
	add_autoload_singleton("YandexAds",
			get_script().get_path().get_base_dir().path_join("yandex_ads.gd"))

func _disable_plugin() -> void:
	remove_autoload_singleton("YandexAds")
	
	ProjectSettings.set_as_internal("ads/yandex/interstitial unit id", true)
	ProjectSettings.set_as_internal("ads/yandex/interstitial autostart", true)
	ProjectSettings.set_as_internal("ads/yandex/interstitial autoload ads", true)
	ProjectSettings.set_as_internal("ads/yandex/rewarded unit id", true)
	ProjectSettings.set_as_internal("ads/yandex/rewarded autostart", true)
	ProjectSettings.set_as_internal("ads/yandex/rewarded autoload ads", true)
	ProjectSettings.save()


class YandexAdsExportPlugin extends EditorExportPlugin:
	var _plugin_name = "YandexAdsGodotPlugin"

	func _supports_platform(platform):
		if platform is EditorExportPlatformAndroid:
			return true
		return false

	func _get_android_libraries(platform, debug):
		if debug:
			return PackedStringArray([_plugin_name + "/bin/debug/" + _plugin_name + "-debug.aar"])
		else:
			return PackedStringArray([_plugin_name + "/bin/release/" + _plugin_name + "-release.aar"])

	func _get_name():
		return _plugin_name
	
	func _get_android_dependencies(platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
		return ["com.yandex.android:mobileads:6.3.0"]

