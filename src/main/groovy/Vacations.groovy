import airbnb.Search
import groovy.json.JsonOutput
import maps.Altitude

import static maps.Altitude.altitudes
import static spark.Spark.get
import static spark.Spark.post

class Vacations {
    static void main(String... args) {
        post("/altitudes", (request, response) -> {
            def url = request.body().toURL()
            int min = request.queryParams("min")?.toInteger() ?: 0

            try {
                def listings = Search.coordinates(url)
                def elevatedListings = altitudes(listings)

                def matchingListings = elevatedListings.findAll { it.elevation >= min }
                JsonOutput.toJson(["results": matchingListings, "total": matchingListings.size()])
            } catch (Exception e) {
                println(e.message)
                throw e
            }
         })
    }
}
