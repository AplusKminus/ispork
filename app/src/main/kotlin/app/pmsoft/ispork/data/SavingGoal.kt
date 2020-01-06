package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
  tableName = "savingGoal",
  foreignKeys = [
    ForeignKey(
      entity = Category::class,
      parentColumns = ["id"],
      childColumns = ["category_id"]
    )
  ],
  indices = [Index("category_id")]
)
@Parcelize
open class SavingGoal(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  open var name: String,

  @ColumnInfo(name = "category_id")
  open var categoryId: Long?,

  @ColumnInfo(name = "rate")
  open var rate: Long?,

  @ColumnInfo(name = "target_amount")
  open var targetAmount: Long?,

  @ColumnInfo(name = "target_date")
  @TypeConverters(TimestampConverter::class)
  open var targetDate: Date?,

  @ColumnInfo(name = "notes")
  open var notes: String?
) : Parcelable,
  ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    "",
    null,
    null,
    null,
    null,
    null
  )
}
