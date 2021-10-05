package utils.ex

val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
val spaceRegex = "\\s".toRegex()
val numbersRegex = "\\d".toRegex()
val specialCharactersRegex = "\\W".toRegex()
const val underscore = "_"
const val emptyString = ""

fun String.extractNameOnly(): String {
    return this.replace(specialCharactersRegex, emptyString)
        .replace(spaceRegex, emptyString)
        .replace(numbersRegex, emptyString)
}

fun String.toSnakeCase(): String {
    val preparedString = this.replace(spaceRegex, emptyString).trim()
    return camelRegex.replace(preparedString) { underscore + it.value }.lowercase()
}

fun String.appendPrefix(prefix: String): String {
    return prefix.plus(this)
}