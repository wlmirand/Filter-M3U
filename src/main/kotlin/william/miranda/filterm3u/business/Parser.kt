package william.miranda.filterm3u.business

import net.bjoernpetersen.m3u.model.M3uEntry
import org.springframework.stereotype.Component

@Component
class Parser {

    private companion object {
        private const val GROUP_TITLE = "group-title"
    }

    fun parse(
        channels: List<M3uEntry>,
        channelCategory: List<String>? = null,
        channelName: List<String>? = null,
        blacklist: List<String>? = null
    ): List<M3uEntry> {
        //Nothing set, so we just skip the filters
        if (channelCategory == null && channelName == null && blacklist == null) {
            return channels
        }

        val result = mutableListOf<M3uEntry>()

        for (channel in channels) {

            val channelGroup = channel.metadata[GROUP_TITLE]

            val meetNameConstraints = channelName != null && channel.title?.containsAny(channelName) != false
            val meetGroupConstraints = channelCategory != null && channelGroup?.containsAny(channelCategory) != false
            val meetNameBlacklist = blacklist != null && channel.title?.containsAny(blacklist) != false
            val meetGroupBlacklist = blacklist != null && channelGroup?.containsAny(blacklist) != false

            if (!meetNameBlacklist && !meetGroupBlacklist && (meetNameConstraints || meetGroupConstraints)) {
                result.add(channel)
            }
        }

        return result
    }

    private fun String.containsAny(collection: Collection<String>, ignoreCase: Boolean = true): Boolean {
        for (item in collection) {
            if (this.contains(item, ignoreCase)) {
                return true
            }
        }

        return false
    }
}