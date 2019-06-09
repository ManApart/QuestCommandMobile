package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList
import system.location.LocationParser
import java.io.InputStreamReader

class LocationAndroidParser(private val assets: AssetManager) : LocationParser {
    private fun parseLocationFile(path: String): List<Location> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))
    private fun parseLocationNodeFile(path: String): List<LocationNode> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadLocations(): NameSearchableList<Location> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/location/locations", ::parseLocationFile))
    }

    override fun loadLocationNodes(): List<LocationNode> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/location/location-nodes", ::parseLocationNodeFile)
    }
}