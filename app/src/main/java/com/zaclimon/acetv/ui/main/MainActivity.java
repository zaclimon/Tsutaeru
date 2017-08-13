package com.zaclimon.acetv.ui.main;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.ui.auth.AuthActivityTv;
import com.zaclimon.acetv.util.ActivityUtil;
import com.zaclimon.acetv.util.Constants;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;


/**
 * First activity upon opening the application. Decides of the layouts to use and if
 * a sign-in is required.
 *
 * @author zaclimon
 * Creation date: 20/06/17
 */

public class MainActivity extends Activity {

    private final int AUTH_REQUEST = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Realm.init(this);

        setContentView(R.layout.activity_main);

        if (ActivityUtil.isUsernamePasswordEmpty(this) && ActivityUtil.isTvMode(this)) {

            ContentResolver contentResolver = getContentResolver();
            Intent intent = new Intent(this, AuthActivityTv.class);
            String inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT);

            // Delete all channels in case of where the user data has been cleared.
            contentResolver.delete(TvContract.buildChannelsUriForInput(inputId), null, null);
            startActivityForResult(intent, AUTH_REQUEST);
        } else {
            configureLayout();
        }
    }

    /**
     * Adds the correct layout based on the display type
     */
    private void configureLayout() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (ActivityUtil.isTvMode(this)) {
            fragmentTransaction.add(R.id.activity_fragment_holder, new MainTvFragment());
        } else {
            Toast.makeText(this, R.string.android_tv_only_toast, Toast.LENGTH_SHORT).show();
            finish();
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AUTH_REQUEST && resultCode == Activity.RESULT_OK) {
            configureLayout();
        } else {
            finish();
        }

    }
}
