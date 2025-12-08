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

        def searchResults = data?.niobeClientData?.get(0)?.get(1)?.data?.presentation?.staysSearch?.results?.searchResults

        if (!searchResults) {
            return []
        }

        return searchResults.collect { result ->
            def listing = result.demandStayListing
            if (listing?.id && listing?.location?.coordinate) {
                def decodedId = new String(listing.id.decodeBase64())
                def numericId = decodedId.split(':')[1]

                def url = "https://www.airbnb.com/rooms/${numericId}".toURL()
                def lat = listing.location.coordinate.latitude
                def lng = listing.location.coordinate.longitude
                return new Listing(url, lat, lng)
            }
            return null
        }.findAll { it != null }.unique { it.url }
    }
}
