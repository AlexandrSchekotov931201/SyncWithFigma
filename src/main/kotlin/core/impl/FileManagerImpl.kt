package core.impl

import core.interfaces.FileManager
import enums.TypeCleaning
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.io.path.Path

class FileManagerImpl(
    private val destination: String = ""
) : FileManager {

    private val networkRequest: NetworkRequests = NetworkRequests()

    //TODO обернуть в try/catch
    override fun uploadFile(url: String, fileName: String, extension: String) {
        networkRequest.executeRequest(url).body?.apply {
            val fileOutputStream = FileOutputStream("$destination/$fileName$extension")
            val buffer = string().toByteArray()
            fileOutputStream.write(buffer)
            fileOutputStream.close()
        }
    }

    override fun createDirectory(path: String) {
        Files.createDirectory(Path(path))
    }

    override fun clearDirectory(path: String, typeCleaning: TypeCleaning) {
        val clearDirectory = File(path)
        if (clearDirectory.isDirectory) {
            when(typeCleaning) {
                TypeCleaning.FILES_ONLY -> clearFilesInDirectory(path)
                TypeCleaning.ALL_DIRECTORY -> {
                    clearFilesInDirectory(path)
                    clearDirectory(path)
                }
            }
        }
    }

    private fun clearDirectory(path: String) {
        Files.delete(Path(path))
    }

    private fun clearFilesInDirectory(path: String) {
        val clearDirectory = File(path)
        clearDirectory.list()?.forEach {
            Files.delete(Path("$path/$it"))
        }
    }

}