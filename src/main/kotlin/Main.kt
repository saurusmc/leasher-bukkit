package hazae41.leasher

import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityUnleashEvent
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {
    override fun onEnable() {
        super.onEnable()

        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onunleash(e: EntityUnleashEvent) {
        if (e.reason != UnleashReason.DISTANCE) return
        val living = e.entity as? LivingEntity ?: return
        val holder = living.leashHolder

        server.scheduler.runTask(this) { _ ->
            living.getNearbyEntities(15.0, 15.0, 15.0)
                .asSequence()
                .mapNotNull { it as? Item }
                .filter { it.itemStack.type == Material.LEAD }
                .firstOrNull()?.remove() ?: return@runTask

            living.teleport(holder)
            living.setLeashHolder(holder)
        }
    }
}