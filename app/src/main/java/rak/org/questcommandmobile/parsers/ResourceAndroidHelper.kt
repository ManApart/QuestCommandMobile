package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import core.utility.ResourceHelper

class ResourceAndroidHelper(private val assets: AssetManager) : ResourceHelper {

    override fun getResourceFiles(path: String, recursive: Boolean): List<String> {
//        println("Getting Files for $path")
        val cleanPath = path.drop(1)
        val files = getAllFiles(cleanPath)

        files.forEach {
            println("Full Path: $it")
        }

        return files
    }

    private fun getAllFiles(path: String): List<String> {
        val list = assets.list(path) ?: arrayOf()

        return list.map {
            getFiles(path, it)
        }.flatten()
    }

    private fun getFiles(path: String, fileName: String): List<String> {
//        println("FileName: $fileName")
        val fullPath = "$path/$fileName"

        return if (assets.list(fullPath)?.size ?: 0 > 0) {
            getAllFiles(fullPath)
        } else {
            listOf(fullPath)
        }
    }

}