package core.interfaces

import enums.PurificationDegree

interface FileManagement {
    fun uploadFile(url: String, fileName: String, extension: String)
    fun createDirectory(path: String)
    fun clearDirectory(path: String, purificationDegree: PurificationDegree)
}