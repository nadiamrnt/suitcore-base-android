package com.suitcore.helper.inappupdates

import android.content.IntentSender.SendIntentException
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.suitcore.R
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonConstant.UpdateMode
import timber.log.Timber

/**
 * Created by @dodydmw19 on 24, March, 2021
 */

class InAppUpdateManager : LifecycleObserver {

    interface InAppUpdateHandler {
        fun onInAppUpdateError(code: Int, error: Throwable?)
        fun onInAppUpdateStatus(status: InAppUpdateStatus?)
    }

    private var activity: AppCompatActivity
    private var appUpdateManager: AppUpdateManager? = null
    private var requestCode = 101
    private var snackBarMessage = "An update has just been downloaded."
    private var snackBarAction = "RESTART"
    private var mode = UpdateMode.FLEXIBLE
    private var resumeUpdates = true
    private var useCustomNotification = false
    private var handler: InAppUpdateHandler? = null
    private var snackbar: Snackbar? = null
    private val inAppUpdateStatus = InAppUpdateStatus()

    private val installStateUpdatedListener: InstallStateUpdatedListener = InstallStateUpdatedListener { state ->
        inAppUpdateStatus.setInstallState(state)
        reportStatus()

        // Show module progress, log state, or install the update.
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForUserConfirmation()
        }
    }

    private constructor(activity: AppCompatActivity) {
        this.activity = activity
        setupSnackBar()
        init()
    }

    private constructor(activity: AppCompatActivity, requestCode: Int) {
        this.activity = activity
        this.requestCode = requestCode
        init()
    }

    private fun init() {
        setupSnackBar()
        appUpdateManager = AppUpdateManagerFactory.create(activity)
        activity.lifecycle.addObserver(this)
        if (mode === UpdateMode.FLEXIBLE) appUpdateManager?.registerListener(installStateUpdatedListener)
        checkForUpdate(false)
    }

    //endregion
    // region Setters
    /**
     * Set the update mode.
     *
     * @param mode the update mode
     * @return the update manager instance
     */
    fun mode(mode: UpdateMode): InAppUpdateManager {
        this.mode = mode
        return this
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * If the update is downloaded but not installed, will notify
     * the user to complete the update.
     *
     * @param resumeUpdates the resume updates
     * @return the update manager instance
     */
    fun resumeUpdates(resumeUpdates: Boolean): InAppUpdateManager {
        this.resumeUpdates = resumeUpdates
        return this
    }

    /**
     * Set the callback handler
     *
     * @param handler the handler
     * @return the update manager instance
     */
    fun handler(handler: InAppUpdateHandler?): InAppUpdateManager {
        this.handler = handler
        return this
    }

    /**
     * Use custom notification for the user confirmation needed by the [CommonConstant.UpdateMode.FLEXIBLE] flow.
     * If this will set to true, need to implement the [InAppUpdateHandler] and listen for the [InAppUpdateStatus.isDownloaded] status
     * via [InAppUpdateHandler.onInAppUpdateStatus] callback. Then a notification (or some other UI indication) can be used,
     * to inform the user that installation is ready and requests user confirmation to restart the app. The confirmation must
     * call the [.completeUpdate] method to finish the update.
     *
     * @param useCustomNotification use custom user confirmation
     * @return the update manager instance
     */
    fun useCustomNotification(useCustomNotification: Boolean): InAppUpdateManager {
        this.useCustomNotification = useCustomNotification
        return this
    }

    fun snackBarMessage(snackBarMessage: String): InAppUpdateManager {
        this.snackBarMessage = snackBarMessage
        setupSnackBar()
        return this
    }

    fun snackBarAction(snackBarAction: String): InAppUpdateManager {
        this.snackBarAction = snackBarAction
        setupSnackBar()
        return this
    }

    fun snackBarActionColor(color: Int): InAppUpdateManager {
        snackbar?.setActionTextColor(color)
        return this
    }

    //endregion
    //region Lifecycle
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (resumeUpdates) checkNewAppVersionState()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        unregisterListener()
    }
    //endregion
    //region Methods
    /**
     * Check for update availability. If there will be an update available
     * will start the update process with the selected [CommonConstant.UpdateMode].
     */
    fun checkForAppUpdate() {
        checkForUpdate(true)
    }

    /**
     * Triggers the completion of the app update for the flexible flow.
     */
    fun completeUpdate() {
        appUpdateManager?.completeUpdate()
    }
    //endregion
    //region Private Methods
    /**
     * Check for update availability. If there will be an update available
     * will start the update process with the selected [CommonConstant.UpdateMode].
     */
    private fun checkForUpdate(startUpdate: Boolean) {

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo


        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            inAppUpdateStatus.setAppUpdateInfo(appUpdateInfo)
            if (startUpdate) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (mode === UpdateMode.FLEXIBLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo)
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        startAppUpdateImmediate(appUpdateInfo)
                    }
                    Timber.d("checkForAppUpdate(): Update available. Version Code: %s", appUpdateInfo.availableVersionCode())
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                    Timber.d("checkForAppUpdate(): No Update available. Code: %s", appUpdateInfo.updateAvailability())
                }
            }
            reportStatus()
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,  // The current activity making the update request.
                    activity,  // Include a request code to later monitor this update request.
                    requestCode)
        } catch (e: SendIntentException) {
            Timber.e(e, "error in startAppUpdateImmediate")
            reportUpdateError(CommonConstant.UPDATE_ERROR_START_APP_UPDATE_IMMEDIATE, e)
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,  // The current activity making the update request.
                    activity,  // Include a request code to later monitor this update request.
                    requestCode)
        } catch (e: SendIntentException) {
            Timber.e(e, "error in startAppUpdateFlexible")
            reportUpdateError(CommonConstant.UPDATE_ERROR_START_APP_UPDATE_FLEXIBLE, e)
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private fun popupSnackbarForUserConfirmation() {
        if (!useCustomNotification) {
            if (snackbar != null && snackbar!!.isShownOrQueued) snackbar!!.dismiss()
            snackbar!!.show()
        }
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private fun checkNewAppVersionState() {
        appUpdateManager
                ?.appUpdateInfo
                ?.addOnSuccessListener { appUpdateInfo ->
                    inAppUpdateStatus.setAppUpdateInfo(appUpdateInfo)

                    //FLEXIBLE:
                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForUserConfirmation()
                        reportStatus()
                        Timber.d("checkNewAppVersionState(): resuming flexible update. Code: %s", appUpdateInfo.updateAvailability())
                    }

                    //IMMEDIATE:
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        startAppUpdateImmediate(appUpdateInfo)
                        Timber.d("checkNewAppVersionState(): resuming immediate update. Code: %s", appUpdateInfo.updateAvailability())
                    }
                }
    }

    private fun setupSnackBar() {
        snackbar = Snackbar.make(activity.findViewById(R.id.content),
                snackBarMessage,
                Snackbar.LENGTH_INDEFINITE)
        snackbar?.setAction(snackBarAction) { // Triggers the completion of the update of the app for the flexible flow.
            appUpdateManager?.completeUpdate()
        }
    }

    private fun unregisterListener() {
        if (appUpdateManager != null) appUpdateManager?.unregisterListener(installStateUpdatedListener)
    }

    private fun reportUpdateError(errorCode: Int, error: Throwable) {
        if (handler != null) {
            handler?.onInAppUpdateError(errorCode, error)
        }
    }

    private fun reportStatus() {
        if (handler != null) {
            handler?.onInAppUpdateStatus(inAppUpdateStatus)
        }
    } //endregion

    companion object {
        // region Declarations
        private const val LOG_TAG = "InAppUpdateManager"

        //endregion
        //region Constructor
        private var instance: InAppUpdateManager? = null

        /**
         * Creates a builder that uses the default requestCode.
         *
         * @param activity the activity
         * @return a new [InAppUpdateManager] instance
         */
        fun builder(activity: AppCompatActivity): InAppUpdateManager? {
            if (instance == null) {
                instance = InAppUpdateManager(activity)
            }
            return instance
        }

        /**
         * Creates a builder
         *
         * @param activity    the activity
         * @param requestCode the request code to later monitor this update request via onActivityResult()
         * @return a new [InAppUpdateManager] instance
         */
        fun builder(activity: AppCompatActivity, requestCode: Int): InAppUpdateManager? {
            if (instance == null) {
                instance = InAppUpdateManager(activity, requestCode)
            }
            return instance
        }
    }
}