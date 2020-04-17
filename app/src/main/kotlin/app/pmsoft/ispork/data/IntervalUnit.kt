package app.pmsoft.ispork.data

import androidx.room.TypeConverter

enum class IntervalUnit {
  DAY,
  WEEK,
  MONTH,
  YEAR;

  companion object {

    public class Converter {
      @TypeConverter
      fun toInt(unit: IntervalUnit): Int {
        return unit.ordinal
      }

      @TypeConverter
      fun fromInt(int: Int): IntervalUnit {
        return IntervalUnit.values()[int]
      }
    }
  }
}
