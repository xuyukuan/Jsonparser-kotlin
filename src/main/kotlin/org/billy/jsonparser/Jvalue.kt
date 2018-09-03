package org.billy.jsonparser

sealed class Jvalue {
    abstract fun value(): Any?
}

data class Jobject(val entries: NonNullMap<String, Jvalue>): Jvalue() {
    override fun value(): NonNullMap<String, Jvalue> {
        return entries
    }
}

data class Jarray(val values: ArrayList<Jvalue>): Jvalue() {
    override fun value(): ArrayList<Jvalue> {
        return values
    }
}

data class Jbool(val b: Boolean): Jvalue() {
    override fun value(): Boolean {
        return b
    }
}

data class Jstring(val str: String): Jvalue() {
    override fun value(): String {
        return str
    }
}

data class Jnumber(val n: Number): Jvalue() {
    override fun value(): Number {
        return n
    }
}

data class Jnull(val v: Nothing? = null): Jvalue() {
    override fun value(): Nothing? {
        return null
    }
}

class NonNullMap<K, V>(private val map: HashMap<K, V>) : Map<K, V> by map {
    override operator fun get(key: K): V {
        return map[key]!! // Force an NPE if the key doesn't exist
    }

    operator fun set(key: K?, value: V) {
        map[key!!] = value
    }
}