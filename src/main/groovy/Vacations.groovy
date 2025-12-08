import airbnb.Search
import io.javalin.Javalin

import static maps.Altitude.altitudes

class Vacations {
    static void main(String... args) {
        def app = Javalin.create().start(4567)

        app.get('/health') { ctx ->
            ctx.json(['status': 'ok'])
        }

        app.post('/altitudes') { ctx ->
            def url = ctx.body().toURL()
            int min = ctx.queryParam('min')?.toInteger() ?: 0

            try {
                def listings = Search.coordinates(url)
                def elevatedListings = altitudes(listings)
                def matchingListings = elevatedListings.findAll {
                    it.elevation >= min
                }
                ctx.json(['results': matchingListings, 'total': matchingListings.size()])
            } catch (Exception e) {
                println(e.message)
                throw e
            }
        }
    }
}
