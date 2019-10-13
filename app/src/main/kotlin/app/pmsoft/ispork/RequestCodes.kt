package app.pmsoft.ispork

class RequestCodes private constructor() {
  companion object {
    const val NONE = -1
    const val CATEGORY_SELECTION_REQUEST_CODE = 1
    const val TRANSACTION_PARTICIPANTS_SELECTION_REQUEST_CODE = 2
    const val CATEGORY_CREATION_REQUEST_CODE = 3
    const val CATEGORY_EDITING_REQUEST_CODE = 4
    const val ACCOUNT_CREATION_REQUEST_CODE = 5
    const val ACCOUNT_EDITING_REQUEST_CODE = 6
    const val PAYEE_CREATION_REQUEST_CODE = 7
    const val PAYEE_EDITING_REQUEST_CODE = 8
    const val TRANSACTION_CREATION_REQUEST_CODE = 9
    const val TRANSACTION_EDITING_REQUEST_CODE = 10
  }
}
