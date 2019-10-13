package app.pmsoft.ispork.data

import androidx.room.*
import androidx.room.Transaction

@Dao
interface CategoryAnnotationDao {

  @Query("SELECT * FROM categoryAnnotation")
  @Transaction
  fun getAll(): List<FullCategoryAnnotation>

  @Query("SELECT * FROM categoryAnnotation WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullCategoryAnnotation?

  @Insert
  fun insert(categoryAnnotations: CategoryAnnotation): Long

  @Query("UPDATE categoryAnnotation SET sub_transaction_id = :subTransactionId, category_id = :categoryId, notes = :notes, amount = :amount WHERE id = :id")
  fun update(
    id: Long,
    subTransactionId: Long,
    amount: Long,
    categoryId: Long?,
    notes: String?
  )

  @Query("DELETE FROM categoryAnnotation WHERE sub_transaction_id = :subTransactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    subTransactionId: Long,
    usedIds: List<Long>
  )

  @Delete
  fun delete(categoryAnnotation: CategoryAnnotation)
}
