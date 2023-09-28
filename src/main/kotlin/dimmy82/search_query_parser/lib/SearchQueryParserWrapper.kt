@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package dimmy82.search_query_parser.lib

import dimmy82.search_query_parser.domain.ICondition
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.logging.Logger
import kotlin.io.path.absolutePathString
import parseQueryToCondition as parseQueryToConditionJson

/**
 * search-query-parser is made to parse complex search query into layered search conditions,
 * so it will be easy to construct Elasticsearch query DSL or something else.
 * this is a wrapper of the Rust library (https://crates.io/crates/search-query-parser).
 * Requirement:
 * - OS: Linux or Mac(arm64)
 * - Java 17
 */
class SearchQueryParserWrapper private constructor() {
    companion object {
        private const val VERSION = "1.1.42"
        private const val NATIVE_VERSION = "0.1.4"
        private const val NATIVE_LIBRARY_NAME = "search_query_parser_${NATIVE_VERSION}"
        private const val NATIVE_LIBRARY_FILE_IN_MAC = "lib${NATIVE_LIBRARY_NAME}.dylib"
        private const val NATIVE_LIBRARY_FILE_IN_LINUX = "lib${NATIVE_LIBRARY_NAME}.so"
        private val nativeLibraryFilePath: String
        private val logger = Logger.getLogger("SearchQueryParser")

        init {
            nativeLibraryFilePath = exportNativeLibrary()
        }

        private fun exportNativeLibrary(): String {
            val nativeLibraryFile = nativeLibraryFile()
            val nativeLibraryFileInput = "${arch()}${File.separator}$nativeLibraryFile"
            val nativeLibraryFileOutput = Paths.get(nativeLibraryExportPath(), nativeLibraryFile)
            logger.info("initialize => export native library file [$nativeLibraryFileOutput] out of jar")
            SearchQueryParserWrapper::class.java.getResourceAsStream(nativeLibraryFileInput)
                ?.let { inputStream ->
                    Files.copy(
                        inputStream,
                        nativeLibraryFileOutput,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            return nativeLibraryFileOutput.absolutePathString()
        }

        private fun arch() =
            System.getProperty("os.arch")?.let {
                if (setOf("aarch64", "arm64").contains(it)) {
                    "arm64"
                } else {
                    "x86_64"
                }
            } ?: "x86_64"

        private fun nativeLibraryFile() =
            System.getProperty("os.name").lowercase().let { os ->
                if (os.contains("mac")) {
                    NATIVE_LIBRARY_FILE_IN_MAC
                } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                    NATIVE_LIBRARY_FILE_IN_LINUX
                } else {
                    throw RuntimeException("Unsupported OS:[${os}]")
                }
            }

        private fun nativeLibraryExportPath(): String {
            val m2Repository = Paths.get(
                System.getProperty("user.home"),
                ".m2",
                "repository",
                "io",
                "github",
                "dimmy82",
                "search-query-parser",
                VERSION
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

        val SEARCH_QUERY_PARSER = SearchQueryParserWrapper()
    }

    init {
        val start = System.currentTimeMillis()
        System.load(nativeLibraryFilePath)
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
