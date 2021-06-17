import com.tomtom.http.HttpClient
import com.tomtom.http.response.ResponseCode
import spock.lang.Specification

import static com.tomtom.http.response.ResponseCode.OK
import static spark.Spark.awaitInitialization;

class AltitudeSpec extends Specification {
    def http = new HttpClient(baseUrl: 'http://localhost:4567')

    def setupSpec() {
        Vacations.main()
        awaitInitialization()
    }

    def 'finds all search altitudes'() {
        when:
        def response = http.post(
                path: '/altitudes',
                body: 'https://www.airbnb.com/s/homes?ne_lat=72.38927453041205&ne_lng=38.463112574493756&sw_lat=29.270725469587948&sw_lng=-29.80311257449376&room_types%5B%5D=Entire%20home%2Fapt&property_type_id%5B%5D=2&search_type=NAVIGATION_CARD&title_type=CURATED_PROPERTY_TYPE',
                expecting: Map)

        then:
        response.statusCode == OK
        response.body["total"]
        response.body["results"]
        response.body["results"].every { it.lat && it.lng && it.url && it.elevation }
    }

    def 'finds search altitudes with minimum filter'() {
        when:
        def response = http.post(
                path: '/altitudes?min=9000',
                body: 'https://www.airbnb.com/s/homes?ne_lat=72.38927453041205&ne_lng=38.463112574493756&sw_lat=29.270725469587948&sw_lng=-29.80311257449376&room_types%5B%5D=Entire%20home%2Fapt&property_type_id%5B%5D=2&search_type=NAVIGATION_CARD&title_type=CURATED_PROPERTY_TYPE',
                expecting: Map)

        then:
        response.statusCode == OK
        response.body["total"] == 0
        !response.body["results"]
    }
}
