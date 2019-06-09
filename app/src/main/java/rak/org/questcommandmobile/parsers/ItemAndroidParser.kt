package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Target
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList
import system.item.ItemParser
import java.io.InputStreamReader

class ItemAndroidParser(private val assets: AssetManager) : ItemParser {
    private fun parseFile(path: String): List<Target> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadItems(): NameSearchableList<Target> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/items", ::parseFile).asSequence().onEach { it.properties.tags.add("Item") }.toList())
    }

}