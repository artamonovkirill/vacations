package maps

import airbnb.Listing
import groovy.transform.TupleConstructor

@TupleConstructor
class ElevatedListing {
    final URL url
    final double lat
    final double lng
    final double elevation

    static elevatedListing(Listing listing, double elevation) {
        new ElevatedListing(listing.url, listing.lat, listing.lng, elevation)
    }
}
