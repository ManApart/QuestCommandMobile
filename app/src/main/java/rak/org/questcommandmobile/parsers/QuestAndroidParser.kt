package rak.org.questcommandmobile.parsers

import android.content.res.AssetManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.quests.Quest
import core.gameState.quests.QuestParser
import core.gameState.quests.StoryEvent
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList
import java.io.InputStreamReader

class QuestAndroidParser(private val assets: AssetManager) : QuestParser {
    private fun parseFile(path: String): List<StoryEvent> = jacksonObjectMapper().readValue(InputStreamReader(assets.open(path)))

    override fun parseQuests(): NameSearchableList<Quest> {
        val events = JsonDirectoryParser.parseDirectory("/data/generated/content/story-events", ::parseFile)
        val quests = mutableMapOf<String, Quest>()

        events.forEach { event ->
            if (!quests.containsKey(event.questName)) {
                quests[event.questName] = Quest(event.questName)
            }
            quests[event.questName]?.addEvent(event)
        }

        quests.values.forEach {
            it.initialize()
        }

        return NameSearchableList(quests.values.toList())
    }
}