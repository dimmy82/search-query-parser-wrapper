@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package dimmy82.search_query_parser.lib

import dimmy82.search_query_parser.domain.ICondition
import java.io.File
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.logging.Level
import java.util.logging.Logger
import parseQueryToCondition as parseQueryToConditionJson

// append these jvm options for execution
// --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED

/**
 * search-query-parser is made to parse complex search query into layered search conditions,
 * so it will be easy to construct Elasticsearch query DSL or something else.
 * this is a wrapper of the Rust library (https://crates.io/crates/search-query-parser).
 * Requirement:
 * - Java 17
 * - Linux or Mac(arm64)
 */
class SearchQueryParserWrapper private constructor() {
    companion object {
        private val logger = Logger.getLogger("SearchQueryParser")
        private const val NATIVE_LIBRARY_NAME = "search_query_parser_0.1.4"
        private val nativeLibraryFiles = setOf(
            "lib${NATIVE_LIBRARY_NAME}.so",
            "lib${NATIVE_LIBRARY_NAME}.dylib"
        )

        init {
            val nativeLibraryPath = exportNativeLibrary()
            prepareForLoadNativeLibrary(nativeLibraryPath)
        }

        private fun exportNativeLibrary(): String {
            val osArch = osArch()
            val nativeLibraryPath = nativeLibraryExportPath()
            nativeLibraryFiles.forEach { nativeLibraryFile ->
                val nativeLibraryFilePath = "$osArch${File.separator}$nativeLibraryFile"
                logger.info("initialize => export native library file [$nativeLibraryFilePath] out of jar")
                SearchQueryParserWrapper::class.java.getResourceAsStream(nativeLibraryFilePath)
                    ?.let { inputStream ->
                        Files.copy(
                            inputStream,
                            Paths.get(nativeLibraryPath, nativeLibraryFile),
                            StandardCopyOption.REPLACE_EXISTING
                        )
                    }
            }
            return nativeLibraryPath
        }

        private fun osArch() =
            System.getProperty("os.arch")?.let {
                if (setOf("aarch64", "arm64").contains(it)) {
                    "arm64"
                } else {
                    "x86_64"
                }
            } ?: "x86_64"

        private fun nativeLibraryExportPath(): String {
            val m2Repository = Paths.get(
                System.getProperty("user.home"),
                ".m2",
                "repository",
                "io",
                "github",
                "dimmy82",
                "search-query-parser",
                "0.1.4"
            ).toFile()
            val nativeLibrary = if (m2Repository.isDirectory) {
                Paths.get(m2Repository.absolutePath, "native-library")
            } else {
                Paths.get("", "native-library")
            }
            if (!nativeLibrary.toFile().isDirectory) {
                Files.createDirectory(nativeLibrary)
            }
            return nativeLibrary.toFile().absolutePath
        }

        private fun prepareForLoadNativeLibrary(nativeLibraryPath: String) {
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
            logger.log(
                Level.CONFIG,
                "initialize => java.library.path: ${libraryNewUserPaths.joinToString(separator = File.pathSeparator)}"
            )
            libraryUserPaths.set(null, libraryNewUserPaths)

            // set final back to NativeLibraries$LibraryPaths.USER_PATHS
            modifiersField.set(
                libraryUserPaths,
                libraryUserPaths.modifiers and Modifier.FINAL.inv()
            )
        }

        val SEARCH_QUERY_PARSER = SearchQueryParserWrapper()
    }

    init {
        val start = System.currentTimeMillis()
        System.loadLibrary(NATIVE_LIBRARY_NAME)
        logger.info("initialize => native library loaded [time: ${System.currentTimeMillis() - start} ms]")
    }

    fun parseQueryToCondition(query: String): ICondition {
        val start = System.currentTimeMillis()
        val condition = parseQueryToConditionJsonString(query).let { ICondition.parseConditionFromJsonString(it) }
        logger.info(
            "parse 「$query」 => \n${
                condition.toString(
                    "> ",
                    0
                )
            }\n> [time: ${System.currentTimeMillis() - start} ms]"
        )
        return condition
    }

    internal fun parseQueryToConditionJsonString(query: String) = parseQueryToConditionJson(query)
}
