package org.billy.jsonparser

class Scanner(private val str: String) {
    private val chars = arrayOf(' ', '\r', '\n')
    private var pos: Int = -1

    /**
     * skipSP should be true when scan next token
     */
    fun next(skipSP: Boolean = false): Char? {
        do {
            forward()
            if (pos == str.length)
                return null
        } while (skipSP and (str[pos] in chars))
        return str[pos]
    }

    fun forward() {
        pos++
    }

    fun back() {
        pos--
    }
}