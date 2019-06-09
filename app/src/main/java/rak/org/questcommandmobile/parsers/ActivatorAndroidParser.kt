package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Target
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList
import system.activator.ActivatorParser
import java.io.InputStreamReader

class ActivatorAndroidParser(private val assets: AssetManager) : ActivatorParser {
    private fun parseFile(path: String): List<Target> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadActivators(): NameSearchableList<Target> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/activators", ::parseFile).asSequence().onEach { it.properties.tags.add("Activator") }.toList())
    }

}