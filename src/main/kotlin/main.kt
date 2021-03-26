import java.io.File
import kotlin.collections.HashSet
import kotlin.random.Random
import kotlinx.coroutines.*

fun main() = runBlocking{
    val wordsFileName = "words.txt"
    val wordsFile = File(wordsFileName)
    val inputWordsFileName = "inputWords.txt"
    val inputWordsFile = File(inputWordsFileName)
    val wordSet = HashSet<String>()
    var word = ""

    // Вытаскиваем слова из файла
    val reader = wordsFile.bufferedReader()
    val iterator = reader.lineSequence().iterator()
    var n = Random.nextInt(0, 1000000)
    var i = 0
    val job = CoroutineScope(Dispatchers.IO).launch {
        while (iterator.hasNext()) {
            val line = iterator.next()
            wordSet.add(line)
            if (i == n) {
                if (line.length > 4)
                    word = line
                else
                    n++
            }
            i++
        }
        reader.close()
    }
    job.join()
    println(word)

    // Считываем введённые слова
    val wordList = ArrayList<String>()
    do {
        val input = readLine().toString()
        val inputSymbols = input.toCharArray()
        val symbols = word.toCharArray()
        var extraSymbol = false
        inputSymbols.forEach { char ->
            if (!symbols.contains(char))
                extraSymbol = true
        }
        if (!extraSymbol)
            wordList.add(input)
        else
            println("There are no such symbols.")
    } while(input.isNotEmpty())
    if (wordList.isNotEmpty())
        wordList.removeAt(wordList.size-1)
    println(wordList.toString())

    // Подсчитываем очки
    val job2 = CoroutineScope(Dispatchers.Default).launch {
        var score = 0
        wordList.forEach { userWord ->
            if (wordSet.contains(userWord)) {
                score++
            }
        }
        println(String.format("Your score is %d.", score))
    }

    // Записываем правильные слова
    val job3 = CoroutineScope(Dispatchers.Default).launch {
        inputWordsFile.writeText(wordList.toString())
    }
    job2.join()
    job3.join()
}