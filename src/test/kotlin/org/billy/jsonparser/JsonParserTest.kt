package org.billy.jsonparser

import org.junit.Assert
import org.junit.Test
import java.io.File

import org.billy.jsonparser.TokenType.*

class JsonParserTest {
    @Test
    fun testLexer() {
        val lexer = Lexer(File("src/test/resources/lexerTest.json").readText())
        val tokens = lexer.buildTokens()
        assertToken(tokens.next(), OBJ_START)
        assertToken(tokens.next(), V_STRING, "number")
        assertToken(tokens.next(), COLON)
        assertToken(tokens.next(), V_NUMBER, "1")
        assertToken(tokens.next(), COMMA)
        assertToken(tokens.next(), V_STRING, "boolAndNull")
        assertToken(tokens.next(), COLON)
        assertToken(tokens.next(), ARR_START)
        assertToken(tokens.next(), V_BOOLEAN, "true")
        assertToken(tokens.next(), COMMA)
        assertToken(tokens.next(), V_BOOLEAN, "false")
        assertToken(tokens.next(), COMMA)
        assertToken(tokens.next(), V_NULL, "null")
        assertToken(tokens.next(), ARR_END)
        assertToken(tokens.next(), COMMA)
        assertToken(tokens.next(), V_STRING, "objs")
        assertToken(tokens.next(), COLON)
        assertToken(tokens.next(), ARR_START)
        assertToken(tokens.next(), OBJ_START)
        assertToken(tokens.next(), V_STRING, "x")
        assertToken(tokens.next(), COLON)
        assertToken(tokens.next(), V_STRING, "hello")
        assertToken(tokens.next(), OBJ_END)
        assertToken(tokens.next(), COMMA)
        assertToken(tokens.next(), OBJ_START)
        assertToken(tokens.next(), V_STRING, "y")
        assertToken(tokens.next(), COLON)
        assertToken(tokens.next(), V_STRING, "world")
        assertToken(tokens.next(), OBJ_END)
        assertToken(tokens.next(), ARR_END)
        assertToken(tokens.next(), OBJ_END)
        assertToken(tokens.next(), JSON_END)
    }

    fun assertToken(token: Token, type: TokenType, expected: String? = null) {
        Assert.assertEquals(type, token.type)
        Assert.assertEquals(expected, token.value)
    }

    @Test
    fun testParser() {
        val lexer = Lexer(File("src/test/resources/obj.json").readText())
        val parser = Parser()
        val json = parser.parse(lexer.buildTokens()) as Jobject
        val map= json.value()
        Assert.assertEquals("John", map["firstName"].value())
        Assert.assertEquals("Smith", map["lastName"].value())
        Assert.assertEquals(true, map["isAlive"].value())
        Assert.assertEquals(25, map["age"].value())
        Assert.assertEquals(167, map["height_cm"].value())
        Assert.assertEquals(null, map["spouse"].value())
        val addrMap = (map["address"] as Jobject).value()
        Assert.assertEquals("21 2nd Street", addrMap["streetAddress"].value())
        Assert.assertEquals("New York", addrMap["city"].value())
        Assert.assertEquals("NY", addrMap["state"].value())
        Assert.assertEquals("10021-3100", addrMap["postalCode"].value())
        val phoneNumbers = (map["phoneNumbers"] as Jarray).value()
        val phone0 = (phoneNumbers[0] as Jobject).value()
        val phone1 = (phoneNumbers[1] as Jobject).value()
        Assert.assertEquals("home", phone0["type"].value())
        Assert.assertEquals("212 555-1234", phone0["number"].value())
        Assert.assertEquals("office", phone1["type"].value())
        Assert.assertEquals("646 555-4567", phone1["number"].value())
        val children = (map["children"] as Jarray).value()
        Assert.assertEquals(0, children.size)
    }
}