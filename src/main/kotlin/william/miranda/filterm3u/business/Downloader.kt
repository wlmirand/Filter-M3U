package william.miranda.filterm3u.business

import net.bjoernpetersen.m3u.M3uParser
import net.bjoernpetersen.m3u.model.M3uEntry
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.net.URL

@Component
class Downloader {

    fun download(fileUrl: String): List<M3uEntry> {
        URL(fileUrl).openStream().use {
            InputStreamReader(it).use { reader ->
                return M3uParser.parse(reader).sortedBy { it.title }
            }
        }
    }
}