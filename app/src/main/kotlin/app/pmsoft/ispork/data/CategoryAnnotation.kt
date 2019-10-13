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
      entity = Category::class,
      parentColumns = ["id"],
      childColumns = ["category_id"]
    )
  ],
  indices = [Index("sub_transaction_id"), Index("category_id")],
  tableName = "categoryAnnotation"
)
@Parcelize
open class CategoryAnnotation(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "sub_transaction_id")
  open var subTransactionId: Long,

  @ColumnInfo(name = "amount")
  open var amount: Long,

  @ColumnInfo(name = "category_id")
  open var categoryId: Long?,

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
