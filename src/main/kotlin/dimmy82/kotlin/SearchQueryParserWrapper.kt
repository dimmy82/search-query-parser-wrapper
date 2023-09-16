@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package dimmy82.kotlin

import parseQueryToCondition
import java.io.File
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.absolutePathString


// 下記のオプションを付けて実行できる
// --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED

class SearchQueryParserWrapper private constructor() {
    companion object {
        private const val nativeLibraryPathInJar = "dimmy82/lib"
        private val nativeLibraryPath = Paths.get("").absolutePathString()
        private val nativeLibraryExtensions = setOf("so", "dylib")

        init {
            exportNativeLibrary()
            prepareForLoadNativeLibrary()
        }

        private fun exportNativeLibrary() {
            // Step 1: JARファイルからネイティブライブラリを読み込む
            File(nativeLibraryPathInJar).listFiles()?.forEach { nativeLibraryFile ->
                if (nativeLibraryExtensions.contains(nativeLibraryFile.extension)) {
                    SearchQueryParserWrapper::class.java.getResourceAsStream(nativeLibraryFile.canonicalPath)
                        ?.let { inputStream ->
                            Files.copy(
                                inputStream,
                                Paths.get(nativeLibraryPath, nativeLibraryFile.name),
                                StandardCopyOption.REPLACE_EXISTING
                            )
                        }
                }
            }
        }

        private fun prepareForLoadNativeLibrary() {
            // field modifier
            val lookup = MethodHandles.privateLookupIn(Field::class.java, MethodHandles.lookup())
            val modifiersField = lookup.findVarHandle(Field::class.java, "modifiers", Int::class.javaPrimitiveType)

            // jdk.internal.loader.NativeLibraries$LibraryPaths in Java 17
            val libraryPathsClass = Class.forName("jdk.internal.loader.NativeLibraries\$LibraryPaths")
            val libraryUserPaths = libraryPathsClass.getDeclaredField("USER_PATHS").also { it.setAccessible(true) }

            // remove final from NativeLibraries$LibraryPaths.USER_PATHS
            modifiersField.set(
                libraryUserPaths,
                libraryUserPaths.modifiers and Modifier.FINAL.inv()
            )

            // append path to java.library.path
            @Suppress("UNCHECKED_CAST")
            val libraryCurrentUserPaths = libraryUserPaths.get(null) as Array<String>
            val libraryNewUserPaths = arrayOf(nativeLibraryPath) + libraryCurrentUserPaths
            println("java.library.path: ${libraryNewUserPaths.joinToString(separator = File.pathSeparator)}")
            libraryUserPaths.set(null, libraryNewUserPaths)

            // set final back to NativeLibraries$LibraryPaths.USER_PATHS
            modifiersField.set(
                libraryUserPaths,
                libraryUserPaths.modifiers and Modifier.FINAL.inv()
            )
        }

        val SEARCH_QUERY_PARSER_WRAPPER: SearchQueryParserWrapper = SearchQueryParserWrapper()
    }

    init {
        val start = System.currentTimeMillis()
        System.loadLibrary("search_query_parser_0.1.4")
        println("===> load library time: ${System.currentTimeMillis() - start}")
    }

    fun parse(query: String) = parseQueryToCondition(query)
}


