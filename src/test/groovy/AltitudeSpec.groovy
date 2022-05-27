import com.tomtom.http.HttpClient
import spock.lang.Specification

import static com.tomtom.http.response.ResponseCode.OK
import static spark.Spark.awaitInitialization

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
                body: url,
                expecting: Map)

        then:
        response.statusCode == OK
        response.body["total"]
        response.body["results"]
        !response.body["results"].findAll { !it.lat || !it.lng || !it.url || !it.elevation }

        where:
        url << ['https://www.airbnb.com/s/homes?ne_lat=72.38927453041205&ne_lng=38.463112574493756&sw_lat=29.270725469587948&sw_lng=-29.80311257449376&room_types%5B%5D=Entire%20home%2Fapt&property_type_id%5B%5D=2&search_type=NAVIGATION_CARD&title_type=CURATED_PROPERTY_TYPE',
                'https://www.airbnb.com/s/Wengen--Switzerland/homes?tab_id=home_tab&refinement_paths%5B%5D=%2Fhomes&flexible_trip_lengths%5B%5D=one_week&query=Wengen%2C%203823%20Lauterbrunnen%2C%20Switzerland&place_id=ChIJYdRtHsahj0cRgQMvzvqWqbE&date_picker_type=calendar&checkin=2022-07-16&checkout=2022-07-23&adults=3&children=2&source=structured_search_input_header&search_type=user_map_move&price_max=311&min_bedrooms=3&ne_lat=46.71360253305795&ne_lng=8.220160174618854&sw_lat=46.45404373470356&sw_lng=7.742254901181354&zoom=11&search_by_map=true']
    }

    def 'finds search altitudes with minimum filter'() {
        when:
        def response = http.post(
                path: '/altitudes?min=9000',
                body: 'https://www.airbnb.com/s/Wengen--Switzerland/homes?tab_id=home_tab&refinement_paths%5B%5D=%2Fhomes&flexible_trip_lengths%5B%5D=one_week&query=Wengen%2C%203823%20Lauterbrunnen%2C%20Switzerland&place_id=ChIJYdRtHsahj0cRgQMvzvqWqbE&date_picker_type=calendar&checkin=2022-07-16&checkout=2022-07-23&adults=3&children=2&source=structured_search_input_header&search_type=user_map_move&price_max=311&min_bedrooms=3&ne_lat=46.71360253305795&ne_lng=8.220160174618854&sw_lat=46.45404373470356&sw_lng=7.742254901181354&zoom=11&search_by_map=true',
                expecting: Map)

        then:
        response.statusCode == OK
        response.body["total"] == 0
        !response.body["results"]
    }
}
