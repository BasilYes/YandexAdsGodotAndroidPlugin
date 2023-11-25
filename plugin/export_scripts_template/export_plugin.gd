@tool
extends EditorPlugin

# A class member to hold the editor export plugin during its lifecycle.
var export_plugin : YandexAdsExportPlugin

func _enter_tree():
	# Initialization of the plugin goes here.
	export_plugin = YandexAdsExportPlugin.new()
	add_autoload_singleton("YandexAds",
			get_script().get_path().get_base_dir().path_join("yandex_ads_autoload.gd"))
	add_export_plugin(export_plugin)
	
	var setting_path: = "ads/yandex/interstitial unit id"
	if not ProjectSettings.has_setting(setting_path):
		ProjectSettings.set(setting_path, "your-ad-unit-id")
	ProjectSettings.set_initial_value(setting_path, "your-ad-unit-id")
	ProjectSettings.set_as_basic(setting_path, true)
	ProjectSettings.add_property_info({ "name": setting_path, "type": TYPE_STRING})
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
	
	ProjectSettings.save()


func _exit_tree():
	# Clean-up of the plugin goes here.
	ProjectSettings.set_as_internal("ads/yandex/interstitial unit id", true)
	ProjectSettings.set_as_internal("ads/yandex/interstitial autostart", true)
	ProjectSettings.save()
	remove_autoload_singleton("YandexAds")
	remove_export_plugin(export_plugin)
	export_plugin = null


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
		return ["com.yandex.android:mobileads:6.2.0"]
