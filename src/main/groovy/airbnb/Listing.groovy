package airbnb

import groovy.transform.TupleConstructor

@TupleConstructor
class Listing {
    final URL url
    final double lat
    final double lng
}
