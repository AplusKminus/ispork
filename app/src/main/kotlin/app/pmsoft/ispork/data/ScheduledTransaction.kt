package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
  tableName = "scheduledTransaction"
)
@Parcelize
open class ScheduledTransaction(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  open var name: String
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    ""
  )
}

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = ScheduledTransaction::class,
      parentColumns = ["id"],
      childColumns = ["scheduled_transaction_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("scheduled_transaction_id")],
  tableName = "scheduledOccurrence"
)
@Parcelize
open class ScheduledOccurrence(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "scheduled_transaction_id")
  open var scheduledTransactionId: Long,

  @ColumnInfo(name = "start_date")
  open var startDate: Date?,

  @ColumnInfo(name = "end_date")
  open var endDate: Date?,

  @Embedded
  open var cadence: Cadence?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    null,
    null,
    null
  )
}

@Parcelize
open class Cadence(

  @ColumnInfo(name = "interval_unit")
  var timeUnit: IntervalUnit?,

  @ColumnInfo(name = "offset")
  var offset: Int?,

  @ColumnInfo(name = "workdays_only")
  var workdayOnly: Boolean
) : Parcelable {

  @Ignore
  constructor() : this(
    null,
    null,
    false
  )
}

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = Participant::class,
      parentColumns = ["id"],
      childColumns = ["participant_id"]
    ),
    ForeignKey(
      entity = ScheduledTransaction::class,
      parentColumns = ["id"],
      childColumns = ["scheduled_transaction_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("participant_id"), Index("scheduled_transaction_id")],
  tableName = "subTransactionTemplate"
)
@Parcelize
open class SubTransactionTemplate(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "amount")
  open var amount: Long,

  @ColumnInfo(name = "scheduled_transaction_id")
  open var scheduledTransactionId: Long,

  @ColumnInfo(name = "participant_id")
  open var participantId: Long?,

  @ColumnInfo(name = "notes_template")
  open var notesTemplate: String?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    null,
    null
  )
}

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"]
    ),
    ForeignKey(
      entity = SubTransactionTemplate::class,
      parentColumns = ["id"],
      childColumns = ["sub_transaction_template_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("budget_pot_id"), Index("sub_transaction_template_id")],
  tableName = "budgetPotAnnotationTemplate"
)
@Parcelize
open class BudgetPotAnnotationTemplate(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "amount")
  open var amount: Long,

  @ColumnInfo(name = "sub_transaction_template_id")
  open var subTransactionTemplateId: Long,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?,

  @ColumnInfo(name = "notes_template")
  open var notesTemplate: String?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    0,
    null
  )
}
