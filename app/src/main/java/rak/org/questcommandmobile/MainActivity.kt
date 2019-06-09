package rak.org.questcommandmobile

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import core.commands.CommandParser
import core.gameState.quests.QuestParser
import core.utility.ReflectionParser
import core.utility.ResourceHelper
import crafting.RecipeParser
import kotlinx.android.synthetic.main.activity_main.*
import rak.org.questcommandmobile.parsers.*
import status.effects.EffectParser
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
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        testReadAssetFiles()

        injectDependencies()
        EventManager.registerListeners()
        GameManager.newGame()
        CommandParser.parseInitialCommand(arrayOf("ls"))

        fab.setOnClickListener { view ->
            CommandParser.parseCommand("Look")
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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
        DependencyInjector.setImplementation(ReflectionParser::class.java, ReflectionAndroidParser(assets))
        DependencyInjector.setImplementation(ResourceHelper::class.java, ResourceAndroidHelper(assets))
    }

    private fun testReadAssetFiles() {
        listOf(
            "data/generated/events.txt",
            "data/generated/commands.txt",
            "data/generated/eventListeners.txt"
        ).forEach { testReadAssetFile(it) }

    }

    private fun testReadAssetFile(fileName: String) {
        val ins = InputStreamReader(assets.open(fileName))

        BufferedReader(ins).use { br ->
            var line = br.readLine()
            while (line != null) {
                println(line)
                line = br.readLine()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
