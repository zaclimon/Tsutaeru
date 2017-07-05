package com.zaclimon.aceiptv.ui.vod;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;

import com.zaclimon.aceiptv.data.AvContent;
import com.zaclimon.aceiptv.ui.playback.PlaybackActivity;
import com.zaclimon.aceiptv.ui.presenter.CardViewPresenter;
import com.zaclimon.aceiptv.util.AvContentUtil;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class in which VOD-like fragments can base off in order to have a complete list
 * of content based on their provider.
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

    /**
     * Gets the link to retrieve an M3U playlist from a given endpoint
     * @return the link to to retrieve VOD content.
     */
    protected abstract String getVodContentApiLink();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        new AsyncProcessAvContent().execute();
    }

    /**
     * Async class that will process everything for a given content list. This way, we
     * don't break on the user experience.
     */
    private class AsyncProcessAvContent extends AsyncTask<Void, Void, Boolean> {

        @Override
        public Boolean doInBackground(Void... params) {
            String avContentLink = getVodContentApiLink();

            try {
                InputStream catchupInputStream = RichFeedUtil.getInputStream(avContentLink);
                List<AvContent> avContents = AvContentUtil.getAvContentsList(catchupInputStream);
                List<String> avGroups = AvContentUtil.getAvContentsGroup(avContents);
                List<ArrayObjectAdapter> avAdapters = getProvidersContent(avContents, avGroups);

                for (int i = 0; i < avAdapters.size(); i++) {
                    HeaderItem catchupItem = new HeaderItem(avGroups.get(i));
                    mRowsAdapter.add(new ListRow(catchupItem, avAdapters.get(i)));
                }

                return (true);
            } catch (IOException io) {
                // Nothing to be done...
                return (false);
            }

        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                setAdapter(mRowsAdapter);
                setOnItemViewClickedListener(new AvContentTvItemClickListener());
                getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
            } else {
                Log.e(LOG_TAG, "Couldn't parse contents");
            }
        }

        /**
         * Gives all the contents available by a given providers for an VodTvSectionFragment
         * @param avContents All the audio visual content available
         * @param avGroups All the different providers offering the said content
         * @return the list of object adapters to be displayed in a {@link ListRow}
         */
        private List<ArrayObjectAdapter> getProvidersContent(List<AvContent> avContents, List<String> avGroups) {

            List<ArrayObjectAdapter> tempAdapters = new ArrayList<>();

            for (String group : avGroups) {
                ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new CardViewPresenter());

                for (AvContent content : avContents) {
                    if (content.getGroup().equals(group)) {
                        arrayObjectAdapter.add(content);
                    }
                }
                tempAdapters.add(arrayObjectAdapter);
            }
            return (tempAdapters);
        }

    }

    /**
     * Class acting as a onItemViewClickedListener to play an {@link AvContent}
     *
     * @author zaclimon
     * Creation date: 02/07/17
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
