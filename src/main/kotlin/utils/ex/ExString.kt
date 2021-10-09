package utils.ex

const val UNDERSCORE = "_"
const val EMPTY_STRING = ""
const val DELIMITER_COMMA = ","

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val spaceRegex = "\\s".toRegex()
private val numbersRegex = "\\d".toRegex()
private val specialCharactersRegex = "\\W".toRegex()

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