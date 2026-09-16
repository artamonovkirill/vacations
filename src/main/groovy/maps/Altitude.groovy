package maps

import airbnb.Listing
import groovy.json.JsonSlurper

import static maps.ElevatedListing.elevatedListing

class Altitude {
    static final slurper = new JsonSlurper()
    static final key = System.getenv('GOOGLE_MAPS_API_KEY') ?:
            { throw new IllegalStateException('GOOGLE_MAPS_API_KEY is not set') }()

    private static String redacted(String message) {
        message?.replace(key, '<redacted>') ?: 'no message'
    }

    static altitudes(List<Listing> listings) {
        def coordinates = listings.collect { "${it.lat},${it.lng}" }.join("|")
        def response
        try {
            response = slurper.parseText("https://maps.googleapis.com/maps/api/elevation/json?locations=$coordinates&key=$key".toURL().text)
        } catch (Exception e) {
            throw new RuntimeException(redacted(e.message))
        }
        if (response.error_message)
            throw new RuntimeException(response.error_message)
        def elevations = response.results
        listings.collect { listing ->
            def elevation = elevations.find { e -> e.location.lat == listing.lat && e.location.lng == listing.lng }.elevation
            elevatedListing(listing, elevation)
        }
    }
}
