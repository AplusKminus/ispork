package app.pmsoft.ispork.transaction.scheduled

import android.content.Context
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.IntervalUnit
import app.pmsoft.ispork.data.OffsetType
import app.pmsoft.ispork.data.ScheduledRhythm
import app.pmsoft.ispork.util.DateHandler.plusDays
import app.pmsoft.ispork.util.DateHandler.plusMonths
import app.pmsoft.ispork.util.DateHandler.plusYears
import app.pmsoft.ispork.util.StringHelpers.joinToTextList

object RhythmHelper {

  private const val MONDAY_PATTERN = 1 shl 0
  private const val TUESDAY_PATTERN = 1 shl 1
  private const val WEDNESDAY_PATTERN = 1 shl 2
  private const val THURSDAY_PATTERN = 1 shl 3
  private const val FRIDAY_PATTERN = 1 shl 4
  private const val SATURDAY_PATTERN = 1 shl 5
  private const val SUNDAY_PATTERN = 1 shl 6
  private val patterns = mapOf(
    MONDAY_PATTERN to R.string.monday,
    TUESDAY_PATTERN to R.string.tuesday,
    WEDNESDAY_PATTERN to R.string.wednesday,
    THURSDAY_PATTERN to R.string.thursday,
    FRIDAY_PATTERN to R.string.friday,
    SATURDAY_PATTERN to R.string.saturday,
    SUNDAY_PATTERN to R.string.sunday
  )

  lateinit var context: Context

  fun describe(rhythm: ScheduledRhythm): String {
    when (rhythm.intervalUnit) {
      IntervalUnit.DAY -> {
        return describeDailyRhythm(rhythm)
      }
      IntervalUnit.WEEK -> {
        return describeWeeklyRhythm(rhythm)
      }
      IntervalUnit.MONTH -> {
        return describeMonthlyRhythm(rhythm)
      }
      IntervalUnit.YEAR -> {
        return describeYearlyRhythm(rhythm)
      }
      else -> {
        return context.resources.getString(R.string.unknown_error)
      }
    }
  }

  fun describeDailyRhythm(rhythm: ScheduledRhythm): String {
    return context.resources.getQuantityString(
      R.plurals.daily_rhythm_description,
      rhythm.intervalLength,
      rhythm.intervalLength
    ) + context.resources.getString(R.string.sentence_end)
  }

  fun describeWeeklyRhythm(rhythm: ScheduledRhythm): String {
    val weekdayStrings = patterns.mapNotNull {
      if (it.key and rhythm.pattern != 0) context.resources.getString(it.value) else null
    }
    return context.resources.getString(R.string.weekly_rhythm_description) +
      weekdayStrings.joinToTextList(
        context.resources.getString(
          R.string.and
        )
      ) + context.resources.getString(R.string.sentence_end)
  }

  fun describeMonthlyRhythm(rhythm: ScheduledRhythm): String {
    var description = context.resources.getQuantityString(
      R.plurals.monthly_rhythm_description,
      rhythm.intervalLength,
      rhythm.intervalLength
    )
    description += context.resources.getString(offsetToStringId(rhythm.dayOffset))
    description += ' '
    description += context.resources.getString(offsetTypeToStringId(rhythm.offsetType))
    if (rhythm.waitForWorkday) {
      description += context.resources.getString(R.string.workday_adjustment_rhythm_description)
    }
    description += context.resources.getString(R.string.sentence_end)
    return description
  }

  fun describeYearlyRhythm(rhythm: ScheduledRhythm): String {
    var description = context.resources.getQuantityString(
      R.plurals.yearly_rhythm_description,
      rhythm.intervalLength,
      rhythm.intervalLength
    )
    if (rhythm.waitForWorkday) {
      description += context.resources.getString(R.string.workday_adjustment_rhythm_description)
    }
    description += context.resources.getString(R.string.sentence_end)
    return description
  }

  fun offsetTypeToStringId(offsetType: OffsetType): Int = when (offsetType) {
    OffsetType.DAY -> R.string.day
    OffsetType.MONDAY -> R.string.monday
    OffsetType.TUESDAY -> R.string.tuesday
    OffsetType.WEDNESDAY -> R.string.wednesday
    OffsetType.THURSDAY -> R.string.thursday
    OffsetType.FRIDAY -> R.string.friday
    OffsetType.SATURDAY -> R.string.saturday
    OffsetType.SUNDAY -> R.string.sunday
  }

  fun offsetToStringId(offset: Int): Int = when (offset) {
    1 -> R.string.first_day
    2 -> R.string.second_day
    3 -> R.string.third_day
    4 -> R.string.fourth_day
    5 -> R.string.fifth_day
    6 -> R.string.sixth_day
    7 -> R.string.seventh_day
    8 -> R.string.eighth_day
    9 -> R.string.ninth_day
    10 -> R.string.tenth_day
    11 -> R.string.eleventh_day
    12 -> R.string.twelfth_day
    13 -> R.string.thirteenth_day
    14 -> R.string.fourteenth_day
    15 -> R.string.fifteenth_day
    16 -> R.string.sixteenth_day
    17 -> R.string.seventeenth_day
    18 -> R.string.eighteenth_day
    19 -> R.string.nineteenth_day
    20 -> R.string.twentieth_day
    21 -> R.string.twenty_first_day
    22 -> R.string.twenty_second_day
    23 -> R.string.twenty_third_day
    24 -> R.string.twenty_fourth_day
    25 -> R.string.twenty_fifth_day
    26 -> R.string.twenty_sixth_day
    27 -> R.string.twenty_seventh_day
    28 -> R.string.twenty_eighth_day
    29 -> R.string.twenty_ninth_day
    30 -> R.string.thirtieth_day
    -1 -> R.string.last_day
    -2 -> R.string.second_last_day
    -3 -> R.string.third_last_day
    -4 -> R.string.fourth_last_day
    -5 -> R.string.fifth_last_day
    -6 -> R.string.sixth_last_day
    -7 -> R.string.seventh_last_day
    -8 -> R.string.eighth_last_day
    -9 -> R.string.ninth_last_day
    -10 -> R.string.tenth_last_day
    -11 -> R.string.eleventh_last_day
    -12 -> R.string.twelfth_last_day
    -13 -> R.string.thirteenth_last_day
    -14 -> R.string.fourteenth_last_day
    -15 -> R.string.fifteenth_last_day
    -16 -> R.string.sixteenth_last_day
    -17 -> R.string.seventeenth_last_day
    -18 -> R.string.eighteenth_last_day
    -19 -> R.string.nineteenth_last_day
    -20 -> R.string.twentieth_last_day
    -21 -> R.string.twenty_first_last_day
    -22 -> R.string.twenty_second_last_day
    -23 -> R.string.twenty_third_last_day
    -24 -> R.string.twenty_fourth_last_day
    -25 -> R.string.twenty_fifth_last_day
    -26 -> R.string.twenty_sixth_last_day
    -27 -> R.string.twenty_seventh_last_day
    -28 -> R.string.twenty_eighth_last_day
    -29 -> R.string.twenty_ninth_last_day
    -30 -> R.string.thirtieth_last_day
    else -> R.string.unknown_error
  }

  fun countOccurrences(rhythm: ScheduledRhythm): Int? {
    if (rhythm.endDate == null) {
      return null
    }
    val exclusiveEnd = rhythm.endDate!!.plusDays(1)
    var startDate = rhythm.startDate
    var occurrences = 0
    while (startDate.before(exclusiveEnd)) {
      occurrences++
      startDate = when (rhythm.intervalUnit) {
        IntervalUnit.DAY -> startDate.plusDays(rhythm.intervalLength)
        IntervalUnit.WEEK -> startDate.plusDays(7 * rhythm.intervalLength)
        IntervalUnit.MONTH -> startDate.plusMonths(rhythm.intervalLength)
        IntervalUnit.YEAR -> startDate.plusYears(rhythm.intervalLength)
      }
    }
    return occurrences
  }
}
