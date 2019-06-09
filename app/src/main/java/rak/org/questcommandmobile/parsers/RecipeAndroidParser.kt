package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList
import crafting.Recipe
import crafting.RecipeParser
import java.io.InputStreamReader

class RecipeAndroidParser(private val assets: AssetManager) : RecipeParser {
    private fun parseFile(path: String): List<Recipe> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun loadRecipes(): NameSearchableList<Recipe> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/recipes", ::parseFile))
    }

}