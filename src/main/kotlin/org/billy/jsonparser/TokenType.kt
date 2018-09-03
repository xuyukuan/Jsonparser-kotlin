package org.billy.jsonparser

enum class TokenType {
    OBJ_START,
    OBJ_END,
    ARR_START,
    ARR_END,
    V_STRING,
    V_NUMBER,
    V_BOOLEAN,
    V_NULL,
    COLON,
    COMMA,
    JSON_END
}