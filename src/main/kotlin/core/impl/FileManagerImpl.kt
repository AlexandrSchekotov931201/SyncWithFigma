package core.impl

import core.interfaces.FileManager
import core.enums.TypeCleaning
import core.network.NetworkRequests
import utils.ex.isNotNullOrEmpty
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.io.path.Path

class FileManagerImpl(
    private val networkRequest: NetworkRequests
) : FileManager {

    override fun uploadFile(url: String, fileName: String, extension: String, destination: String) {
        networkRequest.executeRequest(url).body?.apply {
            createFile(string(), fileName, extension, destination)
        }
    }

    //TODO обернуть в try/catch
    override fun createFile(date: String, fileName: String, extension: String, destination: String) {
        val fileOutputStream = FileOutputStream("$destination/$fileName$extension")
        val buffer = date.toByteArray()
        fileOutputStream.write(buffer)
        fileOutputStream.close()
    }

    override fun createDirectory(path: String) {
        Files.createDirectory(Path(path))
    }

    override fun clearDirectory(path: String, typeCleaning: TypeCleaning) {
        val clearDirectory = File(path)
        if (clearDirectory.isDirectory) {
            when (typeCleaning) {
                TypeCleaning.FILES_ONLY -> clearFilesInDirectory(path)
                TypeCleaning.ALL_DIRECTORY -> {
                    clearFilesInDirectory(path)
                    clearDirectory(path)
                }
            }
        }
    }

    override fun deleteFiles(path: String, fileName: String) {
        clearFilesInDirectory(path, fileName)
    }

    private fun clearDirectory(path: String) {
        Files.delete(Path(path))
    }

    private fun clearFilesInDirectory(path: String, fileName: String? = null) {
        val clearDirectory = File(path)
        clearDirectory.list()?.forEach { fileNameFromList ->
            if (fileName.isNotNullOrEmpty()) {
                if (fileName == fileNameFromList) Files.delete(Path("$path/$fileName"))
            } else {
                Files.delete(Path("$path/$fileNameFromList"))
            }
        }
    }

}