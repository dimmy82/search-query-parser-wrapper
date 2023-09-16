@file:JvmName("SearchQueryParser")

/**
 * Download binary from [https://github.com/dimmy82/search-query-parser-cdylib/blob/master/bin/libsearch_query_parser_0.1.3.dylib]
 * */

/** This file is used as a namespace for all the exported Rust functions. */
internal external fun parseQueryToCondition(str: String): String
