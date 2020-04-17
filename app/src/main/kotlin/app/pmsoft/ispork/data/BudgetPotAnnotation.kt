package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = SubTransaction::class,
      parentColumns = ["id"],
      childColumns = ["sub_transaction_id"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"]
    )
  ],
  indices = [Index("sub_transaction_id"), Index("budget_pot_id")],
  tableName = "budgetPotAnnotation"
)
@Parcelize
open class BudgetPotAnnotation(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "sub_transaction_id")
  open var subTransactionId: Long,

  @ColumnInfo(name = "amount")
  open var amount: Long,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?,

  @ColumnInfo(name = "notes")
  open var notes: String?
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
