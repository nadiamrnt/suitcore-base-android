package com.suitcore.helper

object CommonConstant {

    const val MAP_BOX_TOKEN = "sk.eyJ1IjoiZG9keWRtdzE5IiwiYSI6ImNrdGk5OXk2bTA4emwycnFycXU0ZDhraTUifQ.bVJdLVBM0QkMkgbajgIBQw"
    const val ONE_SIGNAL_APP_ID = "887ba0cd-f47a-435b-8142-03d3a7fc7f41"

    // for intent extras constant
    const val APP_CRASH = "app_crash"

    // -- Remote Config Params
    const val NOTIFY_NORMAL_UPDATE = "minimum_info_android"
    const val NOTIFY_FORCE_UPDATE = "minumum_force_android"
    const val NOTIFY_NORMAL_MESSAGE = "info_message"
    const val NOTIFY_FORCE_MESSAGE = "force_message"
    const val NOTIFY_UPDATE_TYPE= "update_type"
    const val NEW_BASE_URL = "endpoint_url"
    const val CHECK_APP_VERSION = "app_version"
    const val CHECK_BASE_URL = "base_url"

    //HOME MENU
    const val MENU_HOME = "home"
    const val MENU_FRAGMENT_1 = "fragment-1"
    const val MENU_FRAGMENT_2 = "fragment-2"
    const val MENU_FRAGMENT_3 = "fragment-3"

    // In-App Updates
    enum class UpdateMode {
        FLEXIBLE, IMMEDIATE
    }

    const val UPDATE_ERROR_START_APP_UPDATE_FLEXIBLE = 100
    const val UPDATE_ERROR_START_APP_UPDATE_IMMEDIATE = 101

    const val INAPPUPDATE = "InAppUpdate"
    const val REMOTECONFIG = "RemoteConfig"
    const val FLEXIBLE = "flexible"
    const val IMMEDIATE = "immedieate"


}