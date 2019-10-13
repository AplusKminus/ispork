package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = Transaction::class,
      parentColumns = ["id"],
      childColumns = ["transaction_id"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = Participant::class,
      parentColumns = ["id"],
      childColumns = ["participant_id"]
    )
  ],
  indices = [Index("transaction_id"), Index("participant_id")],
  tableName = "subTransaction"
)
@Parcelize
open class SubTransaction(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "transaction_id")
  open var transactionId: Long,

  @ColumnInfo(name = "amount")
  open var amount: Long,

  @ColumnInfo(name = "participant_id")
  open var participantId: Long,

  @ColumnInfo(name = "booking_date")
  @TypeConverters(TimestampConverter::class)
  open var bookingDate: Date?,

  @ColumnInfo(name = "notes")
  open var notes: String?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    0,
    null,
    null
  )
}
