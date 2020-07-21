package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface CategoryDao {

  @Query("SELECT * FROM categories ORDER BY name")
  fun getAll(): List<Category>

  @Query("SELECT * FROM categories WHERE id IS :id LIMIT 1")
  fun findById(id: Long): Category?

  @Update
  fun update(category: Category)

  @Insert
  fun insert(category: Category): Long

  @Delete
  fun delete(categories: List<Category>)

  @Delete
  fun delete(category: Category)
}
