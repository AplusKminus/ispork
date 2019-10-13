package app.pmsoft.ispork.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class ZeroIsNullLongLiveDataTest {

  @Test
  fun testInitialValueZero() {
    /* arrange */
    val zero = 0L

    /* act */
    val data = ZeroIsNullLongLiveData(zero)

    /* assert */
    assertNull(data.value)
  }

  @Test
  fun testInitialValueNonZero() {
    /* arrange */
    val value = 42L

    /* act */
    val data = ZeroIsNullLongLiveData(value)

    /* assert */
    assertEquals(
      value,
      data.value
    )
  }
}
