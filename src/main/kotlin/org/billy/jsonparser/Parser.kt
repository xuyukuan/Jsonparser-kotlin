package org.billy.jsonparser
import org.billy.jsonparser.TokenType.*

class Parser {
    fun parse(tokens: TokenList): Jvalue {
        val token = tokens.next()
        return when (token.type) {
            OBJ_START -> parseObject(tokens)
            ARR_START -> parseArray(tokens)
            else -> throw RuntimeException("Invalid token")
        }
    }

    private fun parseObject(tokens: TokenList): Jobject {
        var obj = Jobject(NonNullMap(hashMapOf()))
        var token = tokens.next()
        expectedToken(token, OBJ_END, V_STRING)
        if (token.type != OBJ_END) {
            tokens.back()
            parseKeyValue(tokens, obj)
        }
        return obj
    }

    private fun parseArray(tokens: TokenList): Jarray {
        val arr = Jarray(arrayListOf())
        var token = tokens.next()
        if (token.type != ARR_END) {
            tokens.back()
            do {
                arr.values.add(parseValue(tokens))
                token = tokens.next()
                expectedToken(token, COMMA, ARR_END)
            } while (token.type != ARR_END)
        }
        return arr
    }

    private fun parseKeyValue(tokens: TokenList, obj: Jobject) {
        var token = tokens.next()
        val key = token.value
        expectedToken(token, V_STRING)
        expectedToken(tokens.next(), COLON)
        obj.entries[key]= parseValue(tokens)
        token = tokens.next()
        expectedToken(token, COMMA, OBJ_END)
        if (token.type != OBJ_END) {
            expectedToken(token, COMMA)
            parseKeyValue(tokens, obj)
        }
    }

    private fun parseValue(tokens: TokenList): Jvalue {
        var token = tokens.next()
        expectedToken(token, V_STRING, V_NUMBER, OBJ_START, ARR_START, V_BOOLEAN, V_NULL)
        return when (token.type) {
            V_STRING -> Jstring(token.value!!)
            V_NUMBER -> Jnumber(Integer.parseInt(token.value!!))
            OBJ_START -> parseObject(tokens)
            ARR_START -> parseArray(tokens)
            V_BOOLEAN -> Jbool(token.value!!.toBoolean())
            V_NULL -> Jnull()
            else -> throw RuntimeException("Invalid token")
        }
    }

    private fun expectedToken(token: Token, vararg exps: TokenType) {
        if (token.type !in exps) {
            throw throw RuntimeException("Expected ${exps.toList()}, but got ${token.type}")
        }
    }
}