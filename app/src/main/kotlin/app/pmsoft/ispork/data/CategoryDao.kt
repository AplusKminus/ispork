package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {

  @Query("SELECT * FROM category ORDER BY name")
  fun getAll(): List<Category>

  @Query("SELECT * FROM category WHERE id IS :id LIMIT 1")
  fun findById(id: Long): Category?

  @Query("UPDATE category SET name = :name WHERE id IS :id")
  fun update(
    id: Long,
    name: String
  )

  @Insert
  fun insert(category: Category): Long

  @Delete
  fun delete(categories: List<Category>)

  @Delete
  fun delete(category: Category)
}
