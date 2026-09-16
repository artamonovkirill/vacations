import com.tomtom.http.HttpClient
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static com.tomtom.http.response.ResponseCode.OK

abstract class ServerSpec extends Specification {
    static final baseUrl = 'http://localhost:4567'
    private static boolean started

    def http = new HttpClient(baseUrl: baseUrl)

    def setupSpec() {
        if (started) return

        Vacations.main()

        def conditions = new PollingConditions(timeout: 5, initialDelay: 0.1, delay: 0.1)
        conditions.eventually {
            def response = new HttpClient(baseUrl: baseUrl).get(path: '/health')
            assert response.statusCode == OK
        }

        started = true
    }
}
