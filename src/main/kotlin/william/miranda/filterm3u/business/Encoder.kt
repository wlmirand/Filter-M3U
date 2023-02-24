package william.miranda.filterm3u.business

import net.bjoernpetersen.m3u.model.M3uEntry
import net.bjoernpetersen.m3u.model.M3uMetadata
import org.springframework.stereotype.Component

@Component
class Encoder {

    private companion object {
        private const val EXTM3U = "#EXTM3U"
        private const val EXT_X_SESSION_DATA = "#EXT-X-SESSION-DATA:DATA-ID=\"com.xui.1_5_5r2\""
        private const val EXTINF = "#EXTINF:-1"
    }

    fun encode(channels: List<M3uEntry>): String {
        val stringBuilder = StringBuilder()

        stringBuilder
            .appendLine(EXTM3U)
            .appendLine(EXT_X_SESSION_DATA)

        channels.forEach { entry ->
            stringBuilder.appendLine("$EXTINF ${encodeMetadata(entry.metadata)}, ${entry.title}\n${entry.location}")
        }

        return stringBuilder.toString()
    }

    private fun encodeMetadata(metadata: M3uMetadata): String {
        var result = ""
        metadata.forEach { entry ->
            result += "${entry.key}=\"${entry.value}\" "
        }
        return result.trim()
    }
}