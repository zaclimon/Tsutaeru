package com.zaclimon.aceiptv.main;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.auth.AuthActivityTv;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.ActivityUtil;


/**
 * First activity upon opening the application. Decides of the layouts to use and if
 * a sign-in is required.
 *
 * @author zaclimon
 * Creation date: 20/06/17
 */

public class MainActivity extends Activity {

    private final int AUTH_REQUEST = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityUtil.isUsernamePasswordEmpty(this)) {

            ContentResolver contentResolver = getContentResolver();
            Intent intent = new Intent(this, AuthActivityTv.class);
            String inputId = TvContract.buildInputId(AceChannelUtil.TV_INPUT_SERVICE_COMPONENT);

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
        }

        fragmentTransaction.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AUTH_REQUEST && resultCode == Activity.RESULT_OK) {
            configureLayout();
        } else {
            finish();
        }

    }
}
