package li.barlog.app.rest

import li.barlog.app.config.AppConfig
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.ResourceAccessException

@RunWith(SpringRunner::class)
@SpringBootTest(
	classes = arrayOf(AppConfig::class),
	webEnvironment = WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class ErrorsControllerIT {
	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Value("\${api.prefix}")
	private val apiPrefix = ""

	@Test
	fun bad() {
		val request = HttpEntity<Void>(headers())

		val response = restTemplate.exchange("$apiPrefix/errors/bad", HttpMethod.GET,
			request, String::class.java)
		assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
	}

	@Test(expected = ResourceAccessException::class)
	fun timeout() {
		val request = HttpEntity<Void>(headers())

		restTemplate.exchange("$apiPrefix/errors/timeout", HttpMethod.GET,
			request, String::class.java)
	}

	private fun headers() = run {
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON_UTF8
		headers.accept = listOf(MediaType.APPLICATION_JSON_UTF8)
		headers
	}
}
