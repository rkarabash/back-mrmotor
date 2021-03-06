package com.example.mrmotor.constants

/***
 * Класс-перечисление, представляющий коды обработанных исключений
 */
enum class ResponseConstants(val value: String) {
    USERNAME_UNAVAILABLE("USR_001"),
    INVALID_USER_ID("USR_002"),
    EMPTY_NAME("USR_003"),
    EMPTY_PASSWORD("USR_004"),
    ACCOUNT_DEACTIVATED("GLO_001"),
    INVALID_POST_ID("PST_001"),
    POST_DATA_EMPTY("PST_002"),
    NO_LIKE_FOUND("PST_003"),
    SEALED_LIKE_FOUND("PST_OO4"),
    INVALID_QUIZ_ID("QZ_001"),
    FRAUD_AUTHOR("QZ_002"),
    INVALID_QUIZ_DATA("QZ_003"),
    SEALED_QUIZ_ITEMS("QZ_004"),
    NO_QUIZ_RESULT_FOUND("QZ_005")
}