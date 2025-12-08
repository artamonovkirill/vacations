import airbnb.Search
import spock.lang.Specification

class AirbnbParsingSpec extends Specification {
    def 'extracts coordinates from Airbnb search'() {
        given:
        def url = 'https://www.airbnb.co.uk/s/Saas~Fee--Switzerland/homes?refinement_paths%5B%5D=%2Fhomes&date_picker_type=calendar&search_type=user_map_move&flexible_trip_lengths%5B%5D=one_week&monthly_start_date=2026-01-01&monthly_length=3&monthly_end_date=2026-04-01&price_filter_input_type=2&channel=EXPLORE&place_id=ChIJp2CD9qJFj0cRzho9eqWJgOU&acp_id=74e0fe62-22b2-4992-ab87-943645bf07b0&query=Saas-Fee%2C%20Switzerland&search_mode=regular_search&price_filter_num_nights=5&ne_lat=46.115691236231484&ne_lng=7.934395665391833&sw_lat=46.1111932114823&sw_lng=7.928865876837563&zoom=16.82584670940735&zoom_level=16.82584670940735&search_by_map=true&disable_auto_translation=true'.toURL()

        when:
        def listings = Search.coordinates(url)

        then:
        listings.size() > 0
        listings.every { it.url && it.lat && it.lng }
        listings.every { it.lat > 46.0 && it.lat < 47.0 } // Saas-Fee latitude range
        listings.every { it.lng > 7.0 && it.lng < 8.0 } // Saas-Fee longitude range
    }
}
