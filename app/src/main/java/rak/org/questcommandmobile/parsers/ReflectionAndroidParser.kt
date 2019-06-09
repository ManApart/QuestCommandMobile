package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import core.commands.Command
import core.events.Event
import core.events.EventListener
import core.utility.ReflectionParser
import java.io.BufferedReader
import java.io.InputStreamReader

class ReflectionAndroidParser(private val assets: AssetManager) : ReflectionParser {
    override val events: List<Class<out Event>> by lazy { getAllEvents() }
    override val commands: List<Class<out Command>> by lazy { getAllCommands() }
    override val eventListeners: List<Class<out EventListener<*>>> by lazy { getAllEventListeners() }

    private val commandsFile = "data/generated/commands.txt"
    private val eventsFile = "data/generated/events.txt"
    private val eventListenersFile = "data/generated/eventListeners.txt"

    override fun getEvent(className: String): Class<out Event> {
        return events.first { className == it.name.substring(it.name.lastIndexOf(".") + 1) }
    }

    private fun getAllCommands(): List<Class<out Command>> {
        return getClassesFromFile(commandsFile)
    }

    private fun getAllEvents(): List<Class<out Event>> {
        return getClassesFromFile(eventsFile)
    }

    private fun getAllEventListeners(): List<Class<out EventListener<*>>> {
        return getClassesFromFile(eventListenersFile)
    }

    private fun <E> getClassesFromFile(file: String): List<Class<E>> {
        val classes = mutableListOf<Class<E>>()
        val ins = InputStreamReader(assets.open(file))

        BufferedReader(ins).use { br ->
            var line = br.readLine()
            while (!line.isNullOrBlank()) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    val kClass = Class.forName(line) as Class<E>
                    classes.add(kClass)
                } catch (e: ClassNotFoundException) {
                    println("Couldn't find class $line")
                    throw e
                }
                line = br.readLine()
            }
        }
        return classes.toList()
    }
}