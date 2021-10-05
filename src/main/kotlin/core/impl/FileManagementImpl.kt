package core.impl

import core.interfaces.FileManagement
import enums.PurificationDegree
import java.io.*
import java.nio.file.Files
import kotlin.io.path.Path

class FileManagementImpl(
    private val destination: String = "",
) : FileManagement {

    private val networkRequest: NetworkRequests = NetworkRequests()

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

    override fun clearDirectory(path: String, purificationDegree: PurificationDegree) {
        val clearDirectory = File(path)
        if (clearDirectory.isDirectory) {
            when(purificationDegree) {
                PurificationDegree.FILES_ONLY -> clearFilesInDirectory(path)
                PurificationDegree.WHOLE_DIRECTORY -> {
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