package dimmy82.search_query_parser.example

import dimmy82.search_query_parser.lib.SearchQueryParserWrapper.Companion.SEARCH_QUERY_PARSER

fun main(args: Array<String>) {
    var start = System.currentTimeMillis()
    println(
        "===> result: ${
            SEARCH_QUERY_PARSER.parseQueryToCondition("A and B").toString(0)
        }, parse time: ${System.currentTimeMillis() - start} ms"
    )
    start = System.currentTimeMillis()
    println(
        "===> result: ${
            SEARCH_QUERY_PARSER.parseQueryToCondition("A or B").toString(0)
        }, parse time: ${System.currentTimeMillis() - start} ms"
    )
}
