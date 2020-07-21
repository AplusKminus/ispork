package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize

/**
 * An interval consists of a multiplier ([length]) and a [unit].
 */
@Parcelize
class Interval(
  @ColumnInfo(name = "length")
  var length: Int,
  @ColumnInfo(name = "unit")
  var unit: Unit? = null
) : Parcelable {

  @Ignore
  constructor() : this(
    1,
    null
  )

  /**
   * Specifies the base unit of an interval.
   *
   * It is often required to specify the unit as these are not convertible into each other (except day & week).
   *
   * This class offers its own [androidx.room.TypeConverter].
   */
  enum class Unit {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    class Converter {
      @TypeConverter
      fun toInt(unit: Unit): Int {
        return unit.ordinal
      }

      @TypeConverter
      fun fromInt(int: Int): Unit {
        return values()[int]
      }
    }
  }
}

