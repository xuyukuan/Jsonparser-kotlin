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
        val json = parser.parse(lexer.buildTokens())
        val map= json.getValue() as HashMap<String, Jvalue>
        Assert.assertEquals("John", map["firstName"]?.getValue())
        Assert.assertEquals("Smith", map["lastName"]?.getValue())
        Assert.assertEquals(true, map["isAlive"]?.getValue())
        Assert.assertEquals(25, map["age"]?.getValue())
        Assert.assertEquals(167, map["height_cm"]?.getValue())
        Assert.assertEquals(null, map["spouse"]?.getValue())
        val addrMap = map["address"]?.getValue() as HashMap<String, Jvalue>
        Assert.assertEquals("21 2nd Street", addrMap["streetAddress"]?.getValue())
        Assert.assertEquals("New York", addrMap["city"]?.getValue())
        Assert.assertEquals("NY", addrMap["state"]?.getValue())
        Assert.assertEquals("10021-3100", addrMap["postalCode"]?.getValue())
        val phoneNumbers = map["phoneNumbers"]?.getValue() as ArrayList<Jvalue>
        val phone0 = phoneNumbers[0].getValue() as HashMap<String, Jvalue>
        val phone1 = phoneNumbers[1].getValue() as HashMap<String, Jvalue>
        Assert.assertEquals("home", phone0["type"]?.getValue())
        Assert.assertEquals("212 555-1234", phone0["number"]?.getValue())
        Assert.assertEquals("office", phone1["type"]?.getValue())
        Assert.assertEquals("646 555-4567", phone1["number"]?.getValue())
        val children = map["children"]?.getValue() as ArrayList<Jvalue>
        Assert.assertEquals(0, children.size)
    }
}