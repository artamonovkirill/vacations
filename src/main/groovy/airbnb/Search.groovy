package airbnb

import groovy.json.JsonSlurper
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import org.ccil.cowan.tagsoup.Parser

class Search {
    static final parser = new Parser()
    static final xmlSlurper = new XmlSlurper(parser)
    static final jsonSlurper = new JsonSlurper()

    static List<Listing> coordinates(URL searchURL) {
        def html = xmlSlurper.parse(searchURL as String)
        def embedded = html.'**'.find {
            it.name() == 'script' && it.text().startsWith('{') && it.text().contains('"lat"')
        } as GPathResult
        if (!embedded) throw new IllegalStateException("No embedded JSON in $searchURL")

        def data = jsonSlurper.parseText(embedded.text())

        def staysSearches = valuesOf(data, 'staysSearch')
        if (staysSearches.size() != 1)
            throw new IllegalStateException(
                    "Expected 1 staysSearch in $searchURL, found ${staysSearches.size()}")

        def mapSearchResults = staysSearches[0]['mapResults']['mapSearchResults'] as List
        return mapSearchResults.findResults { searchResult ->
            def listing = searchResult['demandStayListing']
            def coordinate = listing['location']['coordinate']

            def id = new String(listing['id'].toString().decodeBase64()).split(':')[1]
            return new Listing("https://www.airbnb.com/rooms/$id".toURL(),
                    coordinate['latitude'] as double,
                    coordinate['longitude'] as double)
        }
    }

    private static List valuesOf(node, String key) {
        if (node instanceof Map) {
            def here = node[key] != null ? [node[key]] : []
            return here + node.values().collectMany { valuesOf(it, key) }
        }
        if (node instanceof List)
            return node.collectMany { valuesOf(it, key) }
        return []
    }
}
