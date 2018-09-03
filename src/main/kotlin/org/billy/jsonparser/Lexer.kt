package org.billy.jsonparser

import org.billy.jsonparser.TokenType.*

class Lexer(str: String) {
    private val tokens: TokenList = TokenList()
    private val scanner: Scanner = Scanner(str)

    fun buildTokens(): TokenList {
        return if (tokens.isEmpty()) process() else tokens
    }

    private fun process(): TokenList {
        var token: Token = cut()
        tokens.add(token)
        while (token.type != JSON_END) {
            token = cut()
            tokens.add(token)
        }
        return tokens
    }

    private fun cut(): Token {
        val c = scanner.next(true)
        return when {
            c == Scanner.EOT -> Token(JSON_END, null)
            c == '{' -> Token(OBJ_START, null)
            c == '}' -> Token(OBJ_END, null)
            c == '[' -> Token(ARR_START, null)
            c == ']' -> Token(ARR_END, null)
            c == ':' -> Token(COLON, null)
            c == ',' -> Token(COMMA, null)
            c in arrayOf('f', 't') -> readBool(c)
            c == 'n' -> readNull()
            c == '"' -> readString()
            c == '-' || c.isDigit() -> readNumber(c)
            else -> throw RuntimeException("Invalid json")
        }
    }


    private fun readNumber(x: Char): Token {
        val sb = StringBuilder().append(x)
        var c = scanner.next()
        while (c.isDigit()) {
            sb.append(c)
            c = scanner.next()
        }
        scanner.back()
        return Token(V_NUMBER, sb.toString())
    }

    private fun readString(): Token {
        val sb = StringBuilder()
        var c: Char? = scanner.next()
        while (c != '"') {
            notExpectedEnd(c)
            sb.append(c)
            c = scanner.next()
        }
        return Token(V_STRING, sb.toString())
    }

    private fun readNull(): Token {
        expected('u', scanner.next())
        expected('l', scanner.next())
        expected('l', scanner.next())
        return Token(V_NULL, "null")
    }

    private fun readBool(c: Char): Token {
        return when (c) {
            't' -> {
                expected('r', scanner.next())
                expected('u', scanner.next())
                expected('e', scanner.next())
                Token(V_BOOLEAN, "true")
            }
            'f' -> {
                expected('a', scanner.next())
                expected('l', scanner.next())
                expected('s', scanner.next())
                expected('e', scanner.next())
                Token(V_BOOLEAN, "false")
            }
            else -> throw RuntimeException("Invalid json")
        }
    }

    private fun expected(c: Char, next: Char?) {
        if (c != next) throw RuntimeException("Expected $c but got $next")
    }

    private fun notExpectedEnd(next: Char?) {
        if (next == null) throw RuntimeException("Not Expected End")
    }
}