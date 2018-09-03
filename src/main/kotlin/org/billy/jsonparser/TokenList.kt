package org.billy.jsonparser

class TokenList: ArrayList<Token>() {
    var pos: Int = -1

    fun next(): Token {
        if (pos < this.size - 1) {
            forward()
        }
        return this.get(pos)
    }

    private fun forward() {
        pos++
    }

    fun back() {
        pos--
    }
}