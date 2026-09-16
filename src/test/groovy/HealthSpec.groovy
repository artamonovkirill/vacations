import static com.tomtom.http.response.ResponseCode.OK

class HealthSpec extends ServerSpec {
    def 'serves health'() {
        when:
        def response = http.get(path: '/health', expecting: Map)

        then:
        response.statusCode == OK
        response.body == [status: 'ok']
    }
}
