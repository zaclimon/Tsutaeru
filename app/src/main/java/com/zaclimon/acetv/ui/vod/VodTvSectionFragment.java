package com.zaclimon.acetv.ui.vod;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.ProgressBarManager;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.ScaleFrameLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.data.AvContent;
import com.zaclimon.acetv.ui.playback.PlaybackActivity;
import com.zaclimon.acetv.ui.presenter.cardview.CardViewPresenter;
import com.zaclimon.acetv.ui.presenter.cardview.PicassoCardViewImageProcessor;
import com.zaclimon.acetv.util.AvContentUtil;
import com.zaclimon.acetv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Base class in which VOD-like (Video on demand) fragments can base off in order to have a complete
 * list of content based on their provider's catalog.
 *
 * @author zaclimon
 * Creation date: 05/07/17
 */

public abstract class VodTvSectionFragment extends RowsFragment {

    /**
     * Variable for accessing an {@link AvContent} title
     */
    public static final String AV_CONTENT_TITLE_BUNDLE = "av_content_title";

    /**
     * Variable for accessing an {@link AvContent} logo url
     */
    public static final String AV_CONTENT_LOGO_BUNDLE = "av_content_logo";

    /**
     * Variable for accessing an {@link AvContent} content url
     */
    public static final String AV_CONTENT_LINK_BUNDLE = "av_content_link";

    /**
     * Variable for accessing an {@link AvContent} group (provider)
     */
    public static final String AV_CONTENT_GROUP_BUNDLE = "av_content_group";

    private final String LOG_TAG = getClass().getSimpleName();

    private ArrayObjectAdapter mRowsAdapter;
    private ProgressBarManager mProgressBarManager;
    private AsyncProcessAvContent mAsyncProcessAvContent;
    private ScaleFrameLayout mScaleFrameLayout;
    private Realm mRealm;

    /**
     * Gets the link to retrieve an M3U playlist from a given endpoint
     *
     * @return the link to to retrieve VOD content.
     */
    protected abstract String getVodContentApiLink();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mProgressBarManager = ((BrowseFragment) getParentFragment()).getProgressBarManager();
        mScaleFrameLayout = getActivity().findViewById(R.id.scale_frame);
        mRealm = mRealm.getDefaultInstance();
        mProgressBarManager.setRootView((ViewGroup) getActivity().findViewById(R.id.browse_container_dock));
        setOnItemViewClickedListener(new AvContentTvItemClickListener());
        setAdapter(mRowsAdapter);

        mAsyncProcessAvContent = new AsyncProcessAvContent();
        mAsyncProcessAvContent.execute();

    }

    /**
     * Updates the main RowAdapter of the Fragment.
     */
    private void updateRowsAdapter() {

        final List<String> avGroups = new ArrayList<>();
        final List<ArrayObjectAdapter> avAdapters = new ArrayList<>();
        final RealmResults<AvContent> contents = mRealm.where(AvContent.class).equalTo("mContentCategory", VodTvSectionFragment.this.getClass().getSimpleName()).findAllSortedAsync("mGroup");

        contents.addChangeListener(new RealmChangeListener<RealmResults<AvContent>>() {
            @Override
            public void onChange(RealmResults<AvContent> foundContents) {
                String currentGroup = foundContents.get(0).getGroup();
                ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new CardViewPresenter(new PicassoCardViewImageProcessor()));
                avGroups.add(currentGroup);

                for (AvContent content : foundContents) {
                    if (!currentGroup.equals(content.getGroup())) {
                        avAdapters.add(arrayObjectAdapter);
                        avGroups.add(content.getGroup());
                        arrayObjectAdapter = new ArrayObjectAdapter(new CardViewPresenter(new PicassoCardViewImageProcessor()));
                        currentGroup = content.getGroup();
                    }
                    arrayObjectAdapter.add(content);
                }

                if (mRowsAdapter.size() == 0) {
                    for (int i = 0; i < avAdapters.size(); i++) {
                        HeaderItem catchupItem = new HeaderItem(avGroups.get(i));
                        mRowsAdapter.add(new ListRow(catchupItem, avAdapters.get(i)));
                    }
                } else {
                    mRowsAdapter.notifyArrayItemRangeChanged(0, contents.size());
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        // Cancel the processing and hide the progress bar if we're changing rows for example.
        if (mAsyncProcessAvContent.getStatus() == AsyncTask.Status.RUNNING) {
            mAsyncProcessAvContent.cancel(true);
            mProgressBarManager.hide();
        }

        if (mRowsAdapter.size() == 0 && mScaleFrameLayout != null) {
            mScaleFrameLayout.removeAllViews();
        }

        mRealm.close();
    }

    /**
     * Shows a view in which content is not available.
     */
    private void showErrorView() {
        if (isAdded()) {
            View view = View.inflate(getActivity(), R.layout.view_content_unavailable, null);
            mScaleFrameLayout.addView(view);
        }
    }

    /**
     * Async class that will process everything for a given content list. This way, we
     * don't break on the user experience.
     */
    private class AsyncProcessAvContent extends AsyncTask<Void, Void, Boolean> {

        @Override
        public void onPreExecute() {
            long realmContentsSize = mRealm.where(AvContent.class).equalTo("mContentCategory", VodTvSectionFragment.this.getClass().getSimpleName()).count();

            if (realmContentsSize > 0) {
                updateRowsAdapter();
            } else {
                mProgressBarManager.show();
            }
        }

        @Override
        public Boolean doInBackground(Void... params) {
            String avContentLink = getVodContentApiLink();
            Realm realm = Realm.getDefaultInstance();

            try {
                RealmResults<AvContent> realmContents = realm.where(AvContent.class).equalTo("mContentCategory", VodTvSectionFragment.this.getClass().getSimpleName()).findAll();


                if (!isCancelled()) {

                    /*
                    It might be risky to insert data without updating and there might be duplicate values.

                    That said, in order to have AvContent with distinct content categories, having
                    duplicate values might not be a problem if we erase all the contents first.
                    */

                    InputStream catchupInputStream = RichFeedUtil.getInputStream(avContentLink);
                    List<AvContent> avContents = AvContentUtil.getAvContentsList(catchupInputStream, VodTvSectionFragment.this.getClass().getSimpleName());

                    if (avContents.size() != realmContents.size()) {
                        realm.beginTransaction();
                        realmContents.deleteAllFromRealm();
                        realm.insert(avContents);
                        realm.commitTransaction();
                        publishProgress();
                    }
                }

                return (true);
            } catch (IOException io) {
                Crashlytics.logException(io);
                return (false);
            } finally {
                realm.close();
            }

        }

        @Override
        public void onProgressUpdate(Void... progress) {
            updateRowsAdapter();
            mProgressBarManager.hide();
        }

        @Override
        public void onPostExecute(Boolean result) {

            if (!result) {
                Log.e(LOG_TAG, "Couldn't parse contents");
                Log.e(LOG_TAG, "Api Link: " + getVodContentApiLink());
                showErrorView();
            }
        }
    }

    /**
     * Class acting as a onItemViewClickedListener to play an {@link AvContent}
     *
     * @author zaclimon
     *         Creation date: 02/07/17
     */
    private class AvContentTvItemClickListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof AvContent) {
                // The item comes from an AvContent element.
                AvContent avContent = (AvContent) item;
                Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AV_CONTENT_TITLE_BUNDLE, avContent.getTitle());
                bundle.putString(AV_CONTENT_LOGO_BUNDLE, avContent.getLogo());
                bundle.putString(AV_CONTENT_LINK_BUNDLE, avContent.getContentLink());
                bundle.putString(AV_CONTENT_GROUP_BUNDLE, avContent.getGroup());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}
