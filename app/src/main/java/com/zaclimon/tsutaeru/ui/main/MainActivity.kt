package com.zaclimon.tsutaeru.ui.main

import android.content.Intent
import android.media.tv.TvContract
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.ui.auth.AuthActivityTv
import com.zaclimon.tsutaeru.util.ActivityUtil
import com.zaclimon.tsutaeru.util.Constants

/**
 * First activity upon opening the application. Decides of the layouts to use and if
 * a sign-in is required.
 *
 * @author zaclimon
 */
class MainActivity : FragmentActivity() {

    companion object {
        private const val REQUEST_AUTH: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityUtil.areCredentialsEmpty(this) && ActivityUtil.isTvMode(this)) {
            val intent = Intent(this, AuthActivityTv::class.java)
            val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)

            // Delete all channels in case of where the user data has been cleared.
            contentResolver.delete(TvContract.buildChannelsUriForInput(inputId), null, null)
            startActivityForResult(intent, REQUEST_AUTH)
        } else {
            configureLayout()
        }
    }

    private fun configureLayout() {

        if (ActivityUtil.isTvMode(this)) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.activity_fragment_holder, MainTvFragment())
            transaction.commit()
        } else {
            Toast.makeText(this, R.string.android_tv_only_toast, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_AUTH && resultCode == FragmentActivity.RESULT_OK) {
            // Recreate the activity so we won't get a state loss after onSaveInstanceState is called.
            recreate()
        } else {
            finish()
        }
    }

}