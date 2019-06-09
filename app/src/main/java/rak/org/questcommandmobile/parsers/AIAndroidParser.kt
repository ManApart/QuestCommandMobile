package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.AIBase
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList
import system.ai.AIParser
import java.io.InputStreamReader

class AIAndroidParser(private val assets: AssetManager) : AIParser {
    private fun parseFile(path: String): List<AIBase> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadAI(): NameSearchableList<AIBase> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/ai", ::parseFile))
    }
}