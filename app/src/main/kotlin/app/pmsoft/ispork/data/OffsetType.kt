package app.pmsoft.ispork.data

import androidx.room.TypeConverter
import app.pmsoft.ispork.data.OffsetType.DAY
import app.pmsoft.ispork.data.OffsetType.MONDAY

/**
 * Specifies what to count when measuring an offset.
 *
 * Type [DAY] means that every day is counted.
 *
 * Types [MONDAY], etc. signify that only that particular weekday is counted.
 *
 * This class offers its own [androidx.room.TypeConverter].
 */
enum class OffsetType {
  DAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

  class Converter {

    @TypeConverter
    fun toInt(type: OffsetType): Int {
      return type.ordinal
    }

    @TypeConverter
    fun fromInt(int: Int): OffsetType {
      return values()[int]
    }
  }
}
