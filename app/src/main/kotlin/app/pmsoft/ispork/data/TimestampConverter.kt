package app.pmsoft.ispork.data

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimestampConverter {
  // TODO change to UTC
  private val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  @TypeConverter
  fun fromTimestamp(value: String?): Date? = value?.let { dateFormat.parse(it) }

  @TypeConverter
  fun toTimestamp(date: Date?): String? = date?.let { dateFormat.format(it) }
}
