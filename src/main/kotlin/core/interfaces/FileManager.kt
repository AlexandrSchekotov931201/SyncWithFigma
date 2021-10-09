package core.interfaces

import enums.TypeCleaning

interface FileManager {
    fun uploadFile(url: String, fileName: String, extension: String)
    fun createDirectory(path: String)
    fun clearDirectory(path: String, typeCleaning: TypeCleaning)
}