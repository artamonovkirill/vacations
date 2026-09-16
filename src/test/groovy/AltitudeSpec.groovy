import static com.tomtom.http.response.ResponseCode.OK

class AltitudeSpec extends ServerSpec {
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
        url << ['https://www.airbnb.co.uk/s/Saas~Fee--Switzerland/homes?refinement_paths%5B%5D=%2Fhomes&date_picker_type=calendar&search_type=user_map_move&flexible_trip_lengths%5B%5D=one_week&monthly_start_date=2026-01-01&monthly_length=3&monthly_end_date=2026-04-01&price_filter_input_type=2&channel=EXPLORE&place_id=ChIJp2CD9qJFj0cRzho9eqWJgOU&acp_id=74e0fe62-22b2-4992-ab87-943645bf07b0&query=Saas-Fee%2C%20Switzerland&search_mode=regular_search&price_filter_num_nights=5&ne_lat=46.115691236231484&ne_lng=7.934395665391833&sw_lat=46.1111932114823&sw_lng=7.928865876837563&zoom=16.82584670940735&zoom_level=16.82584670940735&search_by_map=true&disable_auto_translation=true']
    }

    def 'finds search altitudes with minimum filter'() {
        when:
        def response = http.post(
                path: '/altitudes?min=9000',
                body: 'https://www.airbnb.co.uk/s/Saas~Fee--Switzerland/homes?refinement_paths%5B%5D=%2Fhomes&date_picker_type=calendar&search_type=user_map_move&flexible_trip_lengths%5B%5D=one_week&monthly_start_date=2026-01-01&monthly_length=3&monthly_end_date=2026-04-01&price_filter_input_type=2&channel=EXPLORE&place_id=ChIJp2CD9qJFj0cRzho9eqWJgOU&acp_id=74e0fe62-22b2-4992-ab87-943645bf07b0&query=Saas-Fee%2C%20Switzerland&search_mode=regular_search&price_filter_num_nights=5&ne_lat=46.115691236231484&ne_lng=7.934395665391833&sw_lat=46.1111932114823&sw_lng=7.928865876837563&zoom=16.82584670940735&zoom_level=16.82584670940735&search_by_map=true&disable_auto_translation=true',
                expecting: Map)

        then:
        response.statusCode == OK
        response.body["total"] == 0
        !response.body["results"]
    }
}
