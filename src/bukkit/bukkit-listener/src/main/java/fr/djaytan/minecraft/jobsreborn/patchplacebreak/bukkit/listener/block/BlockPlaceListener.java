package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * This class represents a {@link BlockPlaceEvent} listener.
 *
 * <p>The purpose of this listener is to put a place-and-break tag to newly placed blocks by a
 * player. This permits to prevent place-and-break exploit with diamond ores for example.
 */
@Singleton
public class BlockPlaceListener implements Listener {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @Inject
  public BlockPlaceListener(PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
  }

  /**
   * This method is called when a {@link BlockPlaceEvent} is dispatched to put the place-and-break
   * patch tag.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result. Furthermore,
   * because this plugin will always be enabled before the JobsReborn one (enhance the "depend"
   * plugin.yml line), we have the guarantee that this listener will always be called before the one
   * registered by JobsReborn at the same priority level.
   *
   * @param event The block place event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlace(@NonNull BlockPlaceEvent event) {
    Block block = event.getBlockPlaced();
    patchPlaceBreakBukkitAdapterApi.putTag(block, false);
  }
}
