package utils.ex

const val UNDERSCORE = "_"
const val EMPTY_STRING = ""
const val DELIMITER_COMMA = ","

val spaceRegex = "\\s".toRegex()
val specialCharactersRegex = "\\W".toRegex()

private val numbersRegex = "\\d".toRegex()
private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

fun String.extractNameOnly(): String {
    return this.replace(specialCharactersRegex, EMPTY_STRING)
        .replace(spaceRegex, EMPTY_STRING)
        .replace(numbersRegex, EMPTY_STRING)
}

fun String.toSnakeCase(): String {
    val preparedString = this.replace(spaceRegex, EMPTY_STRING).trim()
    return camelRegex.replace(preparedString) { UNDERSCORE + it.value }.lowercase()
}

fun String.appendPrefix(prefix: String): String {
    return prefix.plus(this)
}

fun CharSequence?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}