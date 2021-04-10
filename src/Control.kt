import java.io.File
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    val filename = requestInput("Please introduce the file name with extension, e.g: chat_laura.txt: ")
    try {
        val file = File("input/$filename")
        var data = file.readLines()

        var parsedData = ArrayList<Message>()
        for (line in data) {
            if (startByDate(line)) {
                val indexTime = findSplitter('-', line)
                val temp = line.substring(indexTime + 2)
                if (temp.contains(":")) {
                    parsedData.add(createMessage(line, indexTime, temp))
                }
            } else {
                val updatedMessage = parsedData[parsedData.lastIndex].message + " " + line
                parsedData[parsedData.lastIndex].message = updatedMessage
            }
        }
        data = emptyList()
        val organizedData = HashMap<String, ArrayList<Message>>()
        for (message in parsedData) {
            if (organizedData.containsKey(message.date)) {
                val messages = organizedData[message.date]
                messages!!.add(message)
                organizedData[message.date!!] = messages
            } else {
                val messages = arrayListOf<Message>()
                messages.add(message)
                organizedData[message.date!!] = messages
            }
        }
        parsedData = ArrayList()

        val key = requestInput("Introduce a date: ")
        try {
            val messages = organizedData[key]
            for (message in messages!!) {
                println(message)
            }
        } catch (e: Exception) {
            println("No entries for that date.")
        }
    } catch (e: Exception) {
        println("File not found at 'input' folder.")
    }
}

fun requestInput(message: String): String {
    val sc = Scanner(System.`in`)
    print(message)
    return sc.next()
}

fun findSplitter(splitter: Char, line: String): Int {
    val vLine = line.toCharArray()
    var i = 0
    var found = false

    while (i < vLine.size && !found) {
        if (vLine[i] == splitter) {
            found = true
        } else {
            i++
        }
    }
    return i
}

fun createMessage(line: String, indexTime: Int, temp: String): Message {
    val datetime = line.substring(0, indexTime - 1)
    val tmpDT = datetime.split(" ")
    val date = tmpDT[0]
    val time = tmpDT[1]
    val indexMessage = findSplitter(':', temp)
    val sender = temp.substring(0, indexMessage)
    val message = temp.substring(indexMessage + 2)
    return Message(date, time, sender, message)
}

fun startByDate(line: String): Boolean {
    val regexPattern = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}\\s.*"
    val pattern = Pattern.compile(regexPattern)
    val matcher = pattern.matcher(line)
    return matcher.matches()
}