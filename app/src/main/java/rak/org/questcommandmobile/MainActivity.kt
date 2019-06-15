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
import core.history.ChatHistory
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var started = false

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
        AppStarter(this, assets).execute()
    }

    fun setStarted() {
        started = true
        execute("ls")
    }

    @SuppressLint("SetTextI18n")
    private fun execute(command: String) {
        if (started) {
            println("Executing: $command")
            CommandParser.parseCommand(command)
            if (!getOutput().isBlank()){
                history.append("\n\n${getOutput()}")
            }
            commandText.text.clear()
            refreshOptions()
        } else {
            println("Not started, so skipping command $command")
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
