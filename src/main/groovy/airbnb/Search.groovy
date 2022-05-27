package airbnb

import groovy.json.JsonSlurper
import groovy.xml.XmlSlurper
import org.ccil.cowan.tagsoup.Parser

class Search {
    static final parser = new Parser()
    static final xmlSlurper = new XmlSlurper(parser)
    static final jsonSlurper = new JsonSlurper()

    static List<Listing> coordinates(URL search) {
        def html = xmlSlurper.parse(search as String)
        String json = html.'**'.find {
            it.text().startsWith('{') && it.text().contains('"lat"')
        }.text()
        def data = jsonSlurper.parseText(json)
        return extract(data)
    }

    private static List<Listing> extract(data) {
        switch (data) {
            case Map:
                if (data.containsKey('id')
                        && data.containsKey('lat')
                        && data.containsKey('lng')) {
                    def url = "https://www.airbnb.com/rooms/${data.id}".toURL()
                    return [new Listing(url, data.lat, data.lng)]
                }
                return data.collectMany { _, v -> extract(v) }
            case List:
                return data.collectMany { extract(it) }
            default:
                return []
        }
    }
}
