package rak.org.questcommandmobile

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import core.commands.CommandParser
import core.utility.ReflectionParser
import kotlinx.android.synthetic.main.activity_main.*
import system.DependencyInjector
import system.EventManager
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        testReadAssetFiles()

        DependencyInjector.setImplementation(ReflectionParser::class.java, ReflectionAndroidParser(assets))
        EventManager.registerListeners()
//        GameManager.newGame()
//        CommandParser.parseInitialCommand(arrayOf())

        fab.setOnClickListener { view ->
            CommandParser.parseCommand("Look")
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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
