/*
 * MIT License
 *
 * Copyright (c) 2025 KeksGauner, CyberEnte
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.datenente.cyberente.listeners;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ReplantListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void handleReplant(PlayerInteractEvent interactEvent) {
        if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = interactEvent.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() != Material.WHEAT) return;

        ItemStack item = interactEvent.getItem();
        if (item == null) return;
        Material material = item.getType();
        if (material != Material.WHEAT_SEEDS) return;

        if (!(clickedBlock.getBlockData() instanceof Ageable ageable)) return;
        if (ageable.getAge() != ageable.getMaximumAge()) return; // Noch nicht ausgewachsen

        // Drop simulieren
        clickedBlock
                .getWorld()
                .dropItemNaturally(clickedBlock.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.WHEAT));
        int seeds = 1 + random.nextInt(2); // 1-2 Samen
        clickedBlock
                .getWorld()
                .dropItemNaturally(
                        clickedBlock.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.WHEAT_SEEDS, seeds));

        ageable.setAge(0);
        clickedBlock.setBlockData(ageable);
    }
}
