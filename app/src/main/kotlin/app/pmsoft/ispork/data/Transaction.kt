package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
  tableName = "transaction"
)
@Parcelize
open class Transaction(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "entry_date")
  @TypeConverters(TimestampConverter::class)
  open var entryDate: Date,

  @ColumnInfo(name = "notes")
  open var notes: String?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    Date(),
    null
  )
}
