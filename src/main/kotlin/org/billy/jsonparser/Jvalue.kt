package org.billy.jsonparser

sealed class Jvalue {
    abstract fun getValue(): Any?
}

data class Jobject(val entries: HashMap<String, Jvalue>): Jvalue() {
    override fun getValue(): HashMap<String, Jvalue> {
        return entries
    }
}

data class Jarray(val values: ArrayList<Jvalue>): Jvalue() {
    override fun getValue(): ArrayList<Jvalue> {
        return values
    }
}

data class Jbool(val b: Boolean): Jvalue() {
    override fun getValue(): Boolean {
        return b
    }
}

data class Jstring(val str: String): Jvalue() {
    override fun getValue(): String {
        return str
    }
}

data class Jnumber(val n: Number): Jvalue() {
    override fun getValue(): Number {
        return n
    }
}

data class Jnull(val v: Nothing? = null): Jvalue() {
    override fun getValue(): Nothing? {
        return null
    }
}