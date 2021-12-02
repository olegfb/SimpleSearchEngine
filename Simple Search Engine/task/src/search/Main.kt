package search

import java.io.File

class DataSearch {
    private val arrDataFIO = mutableListOf<String>()
    private val arrInvertIndex = mutableMapOf<String, MutableList<Int>>()

    fun add(pStr: String) {
        arrDataFIO.add(pStr)
        val arrStr = pStr.lowercase().split("\\s+".toRegex())
        for (a in arrStr) {
            if (a in arrInvertIndex.keys) arrInvertIndex[a]?.add(arrDataFIO.lastIndex)
            else arrInvertIndex[a] = mutableListOf(arrDataFIO.lastIndex)
        }
    }

    private fun getQuery(): List<String> {
        println("\nEnter a name or email to search all suitable people.")
        return readLine()!!.lowercase().split("\\s+".toRegex())
    }

    private fun find(pStr: String): MutableList<Int> {
        var arrRez = mutableListOf<Int>()
        if (pStr in arrInvertIndex.keys) {
            arrRez = arrInvertIndex[pStr]!!
        }
        return arrRez
    }

    fun findANY() {
        val arrRez = mutableListOf<Int>()
        val arrInp = getQuery()
        for (a in arrInp) {
            arrRez.addAll(find(a))
        }
        printRez(arrRez.toSet())
    }


    fun findALL() {
        val arrInp = getQuery()
        var arrRez = find(arrInp[0])
        for (i in 1..arrInp.lastIndex) {
            arrRez = arrRez.intersect(find(arrInp[i]).toSet()).toMutableList()
        }
        printRez(arrRez.toSet())
    }
    fun findNONE(){
        val arrRez = mutableListOf<Int>()
        val arrInp = getQuery()
        for (a in arrInp) {
            arrRez.addAll(find(a))
        }
        val indSet = arrRez.toSet()
        arrRez.clear()
        for (i in arrDataFIO.indices) {
            if (i in indSet) continue
            arrRez.add(i)
        }
        printRez(arrRez.toSet())
    }

    private fun printRez(arrRez: Set<Int>) {
        if (arrRez.isEmpty()) println("No matching people found.")
        else {
            println("\n${arrRez.size} persons found:")
            arrRez.forEach { println(arrDataFIO[it]) }
        }
    }

    fun printAll() {
        println("\n=== List of people ===")
        arrDataFIO.forEach { println(it) }
    }
}

fun mainMenu(): String {
    println("\n=== Menu ===\n" +
            "1. Search information.\n" +
            "2. Print all data.\n" +
            "0. Exit.")
    var str = readLine()!!
    if (str == "1") {
        println("\nSelect a matching strategy: ALL, ANY, NONE")
        str = readLine()!!
    }
    return str
}

fun main(args: Array<String>) {
    if (args.size != 2 || args[0]!="--data") {
        println("Неверные параметры запуска!")
        return
    }
    val pFile = File(args[1])
    val objFIO = DataSearch()
    pFile.forEachLine { objFIO.add(it) }
    do {
        when (mainMenu()) {
            "ALL" -> objFIO.findALL()
            "ANY" -> objFIO.findANY()
            "NONE" -> objFIO.findNONE()
            "2" -> objFIO.printAll()
            "0" -> break
            else -> println("\nIncorrect option! Try again.")
            }
    } while (true)
    println("\nBye!")
}

