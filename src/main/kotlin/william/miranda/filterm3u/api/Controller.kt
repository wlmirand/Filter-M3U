package william.miranda.filterm3u.api

import jdk.jfr.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.WebApplicationType
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import william.miranda.filterm3u.business.Downloader
import william.miranda.filterm3u.business.Encoder
import william.miranda.filterm3u.business.Parser
import java.util.*

@RestController
@RequestMapping(value = ["/api"])
class Controller @Autowired constructor(
    private val downloader: Downloader,
    private val parser: Parser,
    private val encoder: Encoder
) {

    @GetMapping(
        produces = ["application/m3u"]
    )
    fun getFile(
        @RequestParam("url") url: String?,
        @RequestParam("category") category: List<String>?,
        @RequestParam("name") name: List<String>?,
        @RequestParam("blacklist") blacklist: List<String>?
    ): ResponseEntity<String> {
        if (url == null) {
            return ResponseEntity
                .ok()
                .header("Content-type", "text/plain")
                .body("No URL provided")
        }

        val channels = downloader.download(url)
        val parsed = parser.parse(
            channels = channels,
            channelCategory = category,
            channelName = name,
            blacklist = blacklist
        )
        val response = encoder.encode(parsed)

        return ResponseEntity.ok(response)
    }
}