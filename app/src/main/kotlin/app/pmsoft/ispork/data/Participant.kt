package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
  tableName = "participant"
)
@Parcelize
@TypeConverters(Participant.Type.Converter::class)
open class Participant(
  @ColumnInfo(name = "type")
  open val type: Type,
  @PrimaryKey(autoGenerate = true)
  override var id: Long,
  @ColumnInfo(name = "name")
  open var name: String,
  @ColumnInfo(name = "starting_balance")
  open var startingBalance: Long?
) : Parcelable,
  ISporkEntry {

  @Parcelize
  enum class Type(val internal: Boolean) : Parcelable {
    ACCOUNT(true), PERSON(true), PAYEE(false);

    class Converter {

      @TypeConverter
      fun fromString(value: String?): Type? {
        if (value == null) {
          return null
        }
        try {
          return valueOf(value)
        } catch (e: IllegalArgumentException) {
          e.printStackTrace()
        }
        return null
      }

      @TypeConverter
      fun toString(type: Type?): String? = type?.name
    }
  }

  @Ignore
  constructor(type: Type) : this(
    type,
    0,
    "",
    if (type.internal) null else 0L
  )
}
