package rak.org.questcommandmobile

import android.content.res.AssetManager
import android.os.AsyncTask
import core.gameState.quests.QuestParser
import core.utility.ResourceHelper
import core.utility.reflection.GeneratedReflections
import core.utility.reflection.Reflections
import crafting.RecipeParser
import rak.org.questcommandmobile.parsers.*
import system.DependencyInjector
import system.EventManager
import system.GameManager
import system.activator.ActivatorParser
import system.ai.AIParser
import system.behavior.BehaviorParser
import system.body.BodyParser
import system.creature.CreatureParser
import system.item.ItemParser
import system.location.LocationParser
import status.effects.EffectParser

class AppStarter(private val parent: MainActivity, private val assets: AssetManager) : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String?): String {
        injectDependencies()
        EventManager.registerListeners()
        GameManager.newGame()
        return "Done"
    }

    override fun onPostExecute(result: String?) {
        parent.setStarted()
    }

    private fun injectDependencies() {
        DependencyInjector.setImplementation(ActivatorParser::class.java, ActivatorAndroidParser(assets))
        DependencyInjector.setImplementation(AIParser::class.java, AIAndroidParser(assets))
        DependencyInjector.setImplementation(BehaviorParser::class.java, BehaviorAndroidParser(assets))
        DependencyInjector.setImplementation(BodyParser::class.java, BodyAndroidParser(assets))
        DependencyInjector.setImplementation(CreatureParser::class.java, CreatureAndroidParser(assets))
        DependencyInjector.setImplementation(EffectParser::class.java, EffectAndroidParser(assets))
        DependencyInjector.setImplementation(ItemParser::class.java, ItemAndroidParser(assets))
        DependencyInjector.setImplementation(LocationParser::class.java, LocationAndroidParser(assets))
        DependencyInjector.setImplementation(QuestParser::class.java, QuestAndroidParser(assets))
        DependencyInjector.setImplementation(RecipeParser::class.java, RecipeAndroidParser(assets))
        DependencyInjector.setImplementation(Reflections::class.java, GeneratedReflections())
        DependencyInjector.setImplementation(ResourceHelper::class.java, ResourceAndroidHelper(assets))
    }
}