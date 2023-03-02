package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.listener.block;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.adapter.PatchPlaceBreakBukkitAdapterApi;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

/**
 * This class represents a {@link org.bukkit.event.block.BlockPistonEvent} listener. More
 * specifically, this is a listener of both {@link BlockPistonExtendEvent} and {@link
 * BlockPistonRetractEvent}.
 *
 * <p>The purpose of these listeners are to prevent the piston exploit which can be a way to remove
 * a place-and-break patch tag and this isn't what we want. So, the idea is simply to move tags in
 * the same direction as the moved blocks.
 */
@Singleton
public class BlockPistonListener implements Listener {

  private final PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi;

  @Inject
  public BlockPistonListener(PatchPlaceBreakBukkitAdapterApi patchPlaceBreakBukkitAdapterApi) {
    this.patchPlaceBreakBukkitAdapterApi = patchPlaceBreakBukkitAdapterApi;
  }

  /**
   * This method is called when a {@link BlockPistonExtendEvent} is dispatched to move
   * place-and-break patch tag to the new destination of blocks.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block piston extend event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPistonExtend(@NonNull BlockPistonExtendEvent event) {
    BlockFace blockFace = event.getDirection();
    Collection<Block> blocks = event.getBlocks();
    patchPlaceBreakBukkitAdapterApi.moveTags(blocks, blockFace);
  }

  /**
   * This method is called when a {@link BlockPistonRetractEvent} is dispatched to move
   * place-and-break patch tag to the new destination of blocks.
   *
   * <p>The EventPriority is set to {@link EventPriority#MONITOR} because we just want to react when
   * we have the confirmation that the event will occur without modifying its result.
   *
   * @param event The block piston retract event.
   */
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPistonRetract(@NonNull BlockPistonRetractEvent event) {
    BlockFace blockFace = event.getDirection();
    Collection<Block> blocks = event.getBlocks();
    patchPlaceBreakBukkitAdapterApi.moveTags(blocks, blockFace);
  }
}
