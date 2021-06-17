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
        def json = html.'**'.find { it.'@id' == 'data-state' }.text()
        def data = jsonSlurper.parseText(json)
        return data.niobeMinimalClientData[1][1].data.dora.exploreV3.sections*.items*.listing
                .flatten()
                .findAll { it }
                .collect { new Listing("https://www.airbnb.com/rooms/${it.id}".toURL(), it.lat, it.lng) }
    }
}
