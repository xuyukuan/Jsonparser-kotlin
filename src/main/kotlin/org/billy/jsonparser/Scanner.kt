package org.billy.jsonparser

class Scanner(private val str : String) {
    companion object {
        val EOT = (-1).toChar()
    }

    private val chars = arrayOf(' ', '\r', '\n', '\t')
    private var pos: Int = -1

    /**
     * skipSP should be true when scan next token
     */
    fun next(skipSP: Boolean = false): Char {
        do {
            forward()
            if (pos == str.length)
                return EOT
        } while (skipSP and (str[pos] in chars))
        return str[pos]
    }

    private fun forward() {
        pos++
    }

    fun back() {
        pos--
    }
}