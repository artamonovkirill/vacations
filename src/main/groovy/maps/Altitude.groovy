package maps

import airbnb.Listing
import groovy.json.JsonSlurper

import static maps.ElevatedListing.elevatedListing

class Altitude {
    static final slurper = new JsonSlurper()
    static final key = new File("src/main/resources/api.key").text

    static altitudes(List<Listing> listings) {
        def coordinates = listings.collect { "${it.lat},${it.lng}" }.join("|")
        def url = "https://maps.googleapis.com/maps/api/elevation/json?locations=${coordinates}&key=$key".toURL()
        def elevations = slurper.parse(url).results
        listings.collect { listing ->
            def elevation = elevations.find { e -> e.location.lat == listing.lat && e.location.lng == listing.lng }.elevation
            elevatedListing(listing, elevation)
        }
    }
}
