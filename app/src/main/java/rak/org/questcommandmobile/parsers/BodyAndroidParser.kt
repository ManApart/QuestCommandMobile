package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.body.BodyPart
import core.gameState.location.LocationNode
import core.utility.JsonDirectoryParser
import system.body.BodyParser
import java.io.InputStreamReader

class BodyAndroidParser(private val assets: AssetManager) : BodyParser {
    private fun parseBodiesFile(path: String): List<LocationNode> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))
    private fun parsePartsFile(path: String): List<BodyPart> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadBodyParts(): List<BodyPart> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/parts", ::parsePartsFile)
    }

    override fun loadBodies(): List<LocationNode> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/bodies", ::parseBodiesFile)
    }


}