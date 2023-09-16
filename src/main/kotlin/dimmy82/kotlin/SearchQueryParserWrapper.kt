@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package dimmy82.kotlin

import parseQueryToCondition
import java.io.File
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.lang.reflect.Modifier

// 下記のオプションを付けて実行できる
// --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED

class SearchQueryParserWrapper {
    companion object {
        init {
            val libraryPath = "src/main/resources/dimmy82/lib"
            // val libraryPath = "${Paths.get("").toAbsolutePath()}${File.separator}src/main/resources/dimmy82/lib"
            val lookup = MethodHandles.privateLookupIn(Field::class.java, MethodHandles.lookup())
            val modifiersField = lookup.findVarHandle(Field::class.java, "modifiers", Int::class.javaPrimitiveType)
            val libraryPathsClass = Class.forName("jdk.internal.loader.NativeLibraries\$LibraryPaths")
            val libraryUserPaths = libraryPathsClass.getDeclaredField("USER_PATHS").also { it.setAccessible(true) }
            modifiersField.set(
                libraryUserPaths,
                libraryUserPaths.modifiers and Modifier.FINAL.inv()
            )
            val libraryCurrentUserPaths = libraryUserPaths.get(null) as Array<String>
            val libraryNewUserPaths = arrayOf(libraryPath) + libraryCurrentUserPaths
            println("java.library.path: ${libraryNewUserPaths.joinToString(separator = File.pathSeparator)}")
            libraryUserPaths.set(null, libraryNewUserPaths)
            modifiersField.set(
                libraryUserPaths,
                libraryUserPaths.modifiers and Modifier.FINAL.inv()
            )
        }
    }

    init {
        val start = System.currentTimeMillis()
        System.loadLibrary("search_query_parser_0.1.4")
        println("===> load library time: ${System.currentTimeMillis() - start}")
    }

    fun parse(query: String) = parseQueryToCondition(query)
}

val searchQueryParserWrapper: SearchQueryParserWrapper = SearchQueryParserWrapper()

fun main(args: Array<String>) {
    var start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${searchQueryParserWrapper.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
}


