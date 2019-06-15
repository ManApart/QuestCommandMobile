package rak.org.questcommandmobile

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TableRow
import core.commands.CommandParser
import core.gameState.quests.QuestParser
import core.history.ChatHistory
import core.utility.ReflectionParser
import core.utility.ResourceHelper
import crafting.RecipeParser
import kotlinx.android.synthetic.main.content_main.*
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


class MainActivity : AppCompatActivity() {
    private var inited = false

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        history.movementMethod = ScrollingMovementMethod()
        commandText.setOnEditorActionListener { v, _, _ ->
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            execute(v.text.toString())
            true
        }

        executeCommand.setOnClickListener {
            execute(commandText.text.toString())
        }

        buildButtons(listOf("Help", "Commands", "Look"))

        initApp()
    }

    private fun initApp() {
        injectDependencies()
        EventManager.registerListeners()
        GameManager.newGame()
        CommandParser.parseInitialCommand(arrayOf("ls"))
        history.text = getOutput()
        inited = true
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

    @SuppressLint("SetTextI18n")
    private fun execute(command: String) {
        if (inited) {
            println("Executing: $command")
            CommandParser.parseCommand(command)
            if (!getOutput().isBlank()){
                history.append("\n\n${getOutput()}")
            }
            commandText.text.clear()
            refreshOptions()
        } else {
            println("Not inited, so skipping command $command")
        }
    }

    private fun getOutput(): String {
        return ChatHistory.getCurrent().outPut.joinToString("\n")
    }

    private fun refreshOptions() {
        if (CommandParser.responseRequest == null){
            execute("Commands")
        } else {
            buildButtons(CommandParser.responseRequest?.getOptions() ?: listOf())
        }
    }

    private fun buildButtons(words: List<String>) {
        btnTable.removeAllViews()
        val columns = 4
        val rows = words.size/columns
        for (row in 0..rows){
            val rowTable = TableRow(this)
            btnTable.addView(rowTable)

            val start = columns * row
            val end = Math.min(start + columns, words.size)
            words.subList(start, end).forEach { buildButton(it, rowTable) }
        }

    }

    private fun buildButton(word: String, view: TableRow) {
        val btn = Button(this)
        btn.text = word
        btn.setOnClickListener {
            execute(word)
        }
        view.addView(btn)
    }

}
