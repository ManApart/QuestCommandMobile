package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.behavior.BehaviorBase
import core.utility.JsonDirectoryParser
import system.behavior.BehaviorParser
import java.io.InputStreamReader

class BehaviorAndroidParser(private val assets: AssetManager) : BehaviorParser {
    private fun parseFile(path: String): List<BehaviorBase> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadBehaviors(): List<BehaviorBase> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/behaviors", ::parseFile)
    }
}