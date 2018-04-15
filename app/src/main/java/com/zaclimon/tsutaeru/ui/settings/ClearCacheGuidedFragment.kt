package com.zaclimon.tsutaeru.ui.settings

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.widget.Toast
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.data.TsutaeruDatabase

/**
 * Guided Fragment responsible for clearing all cache inside the application.
 *
 * @author zaclimon
 */
class ClearCacheGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.clear_cache_title)
        val description = getString(R.string.clear_cache_description)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val yesAction = GuidedAction.Builder(context).apply {
            title(R.string.yes_text)
            id(GuidedAction.ACTION_ID_YES)
        }.build()

        val noAction = GuidedAction.Builder(context).apply {
            title(R.string.no_text)
            id(GuidedAction.ACTION_ID_NO)
        }.build()

        actions.add(yesAction)
        actions.add(noAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id

        if (id == GuidedAction.ACTION_ID_YES) {
            val contentDao = TsutaeruDatabase.getInstance(context!!).avContentDao()
            contentDao.deleteAll()
            Toast.makeText(context, R.string.clear_cache_toast, Toast.LENGTH_SHORT).show()
        }
        activity?.finish()
    }

}