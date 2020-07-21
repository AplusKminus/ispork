package app.pmsoft.ispork.transaction.scheduled

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.FullSubTransactionTemplate
import app.pmsoft.ispork.data.ScheduledRhythm

class ViewPagerAdapter(
  fragmentManager: FragmentManager,
  private val context: Context
) : FragmentPagerAdapter(
  fragmentManager,
  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

  private val subTransactionListFragment = SubTransactionListFragment()
  private val scheduleFragment = ScheduleFragment()

  override fun getItem(position: Int): Fragment {
    return when (position) {
      0 -> subTransactionListFragment
      else -> scheduleFragment
    }
  }

  override fun getCount(): Int = 2

  fun setData(
    subTransactionTemplates: List<SubTransactionTemplateEditWrapper>,
    schedule: List<ScheduledRhythm>
  ) {
    scheduleFragment.schedule = schedule
    //TODO set data to subTransactionListFragment
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return when (position) {
      0 -> context.resources.getString(R.string.transaction)
      1 -> context.resources.getString(R.string.schedule)
      else -> context.resources.getString(R.string.unknown_error)
    }
  }
}
