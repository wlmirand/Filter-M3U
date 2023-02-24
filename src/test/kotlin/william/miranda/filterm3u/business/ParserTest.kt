package william.miranda.filterm3u.business

import net.bjoernpetersen.m3u.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ParserTest {

	private companion object {
		private const val GROUP_TITLE = "group-title"
	}

	private lateinit var parser: Parser

	private val channelList = listOf(
		M3uEntry(
			location = MediaLocation.invoke("http://url11.com"),
			title = "Canal 11",
			metadata = M3uMetadata(
				mapOf(GROUP_TITLE to "Filmes | C1")
			)
		),
		M3uEntry(
			location = MediaLocation.invoke("http://url12.com"),
			title = "Canal 12",
			metadata = M3uMetadata(
				mapOf(GROUP_TITLE to "Filmes | C2")
			)
		),
		M3uEntry(
			location = MediaLocation.invoke("http://url21.com"),
			title = "Canal 21",
			metadata = M3uMetadata(
				mapOf(GROUP_TITLE to "Filmes | C3")
			)
		),
		M3uEntry(
			location = MediaLocation.invoke("http://url22.com"),
			title = "Canal 22",
			metadata = M3uMetadata(
				mapOf(GROUP_TITLE to "Series | C1")
			)
		),
		M3uEntry(
			location = MediaLocation.invoke("http://url31.com"),
			title = "Canal 31",
			metadata = M3uMetadata(
				mapOf(GROUP_TITLE to "Series | C2")
			)
		)
	)

	@BeforeEach
	fun setup() {
		parser = Parser()
	}

	@Test
	fun whenNoFiltersSet() {
		val result = parser.parse(
			channels = channelList
		)

		assert(result.size == channelList.size)
		for (i in result.indices) {
			assert(result[i] == channelList[i])
		}
	}

	@Test
	fun whenCategoryFilterSet() {
		val categoryName = "Filmes"

		val result = parser.parse(
			channels = channelList,
			channelCategory = listOf(categoryName)
		)

		val resultCount = channelList.mapNotNull { it.metadata[GROUP_TITLE] }.count { it.contains(categoryName) }

		assert(result.size == resultCount)
		for (i in result.indices) {
			assert(result[i] == channelList[i])
		}
	}

	@Test
	fun whenCategoryFilterAndBlacklistSet() {
		val categoryName = "Filmes"
		val blacklist = "C2"

		val result = parser.parse(
			channels = channelList,
			channelCategory = listOf(categoryName),
			blacklist = listOf(blacklist)
		)

		val resultCount = channelList.mapNotNull { it.metadata[GROUP_TITLE] }.count {
			it.contains(categoryName)
				&& it.contains(blacklist).not()
		}

		assert(result.size == resultCount)
	}

	@Test
	fun whenCategoryAndNameFilterAndBlacklistSet() {
		val categoryName = "Filmes"
		val channelName = "31"
		val blacklist = "C2"

		val result = parser.parse(
			channels = channelList,
			channelCategory = listOf(categoryName),
			channelName = listOf(channelName),
			blacklist = listOf(blacklist)
		)

		val resultCount = channelList.mapNotNull { it.metadata[GROUP_TITLE] }.count {
			(it.contains(categoryName)
				|| it.contains(categoryName))
				&& it.contains(blacklist).not()
		}

		assert(result.size == resultCount)
	}
}
