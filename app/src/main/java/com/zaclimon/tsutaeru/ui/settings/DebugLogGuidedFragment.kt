package com.zaclimon.tsutaeru.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.zaclimon.tsutaeru.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Setting section where the users can create a log file that can be sent over for debugging
 * purposes.
 * <p>
 * The logs are generated using Logcat. Note that since Android 4.1, applications can only see
 * the debugging information related to their process. If one wants to see a device's activity,
 * he/she needs to use adb for further needs.
 *
 * @author zaclimon
 */
class DebugLogGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        private const val REQUEST_WRITE_PERMISSION = 0
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.clear_cache_title)
        val description = getString(R.string.clear_cache_description)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val yesAction = GuidedAction.Builder(context)
        val noAction = GuidedAction.Builder(context)
        yesAction.clickAction(GuidedAction.ACTION_ID_YES)
        noAction.clickAction(GuidedAction.ACTION_ID_NO)
        actions.add(yesAction.build())
        actions.add(noAction.build())
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id

        if (id == GuidedAction.ACTION_ID_YES) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AsyncLoadLogcat().execute()
            } else {
                Toast.makeText(context, R.string.write_permissions_denied_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Class that gets the output from the logcat and then saves it in the internal storage of a
     * given device.
     * <p>
     * Made in an asynchronous fashion in order to keep a consistent UI experience.
     *
     * @author zaclimon
     */
    inner class AsyncLoadLogcat : AsyncTask<Void, Void, Boolean>() {

        private val logcatCommand = "logcat -d"
        private val logPath = Environment.getExternalStorageDirectory().absolutePath + "/Tsutaeru-Log" + SimpleDateFormat("dd-MM-YYYY-HH:mm:ss", Locale.ENGLISH).format(Date()) + ".txt"

        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                val process = ProcessBuilder(logcatCommand).start()
                val outputFile = File(logPath)
                process.inputStream.bufferedReader().use {
                    outputFile.appendText(it.readText())
                }
            } catch (io: IOException) {
                return false
            }
            return true
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                Toast.makeText(context, getString(R.string.file_saved_storage, logPath), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.file_saving_error, Toast.LENGTH_SHORT).show()
            }
            activity?.finish()
        }
    }
}