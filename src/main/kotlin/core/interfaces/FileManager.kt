package core.interfaces

import core.enums.TypeCleaning

interface FileManager {
    fun uploadFile(url: String, fileName: String, extension: String, destination: String)
    fun createFile(date: String, fileName: String, extension: String, destination: String)
    fun deleteFiles(path: String, fileName: String)
    fun createDirectory(path: String)
    fun clearDirectory(path: String, typeCleaning: TypeCleaning)
}