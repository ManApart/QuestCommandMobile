package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Target
import core.utility.JsonDirectoryParser
import system.creature.CreatureParser
import java.io.InputStreamReader

class CreatureAndroidParser(private val assets: AssetManager) : CreatureParser {
    private fun parseFile(path: String): List<Target> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadCreatures(): List<Target> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/creatures", ::parseFile)
    }
}