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

import de.datenente.cyberente.utils.Message;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.noise.PerlinOctaveGenerator;

// TODO: Delete on release
public class ADevelopmentListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        World world = player.getWorld();

        PerlinOctaveGenerator elevationNoiseGenerator = new PerlinOctaveGenerator(world.getSeed(), 4);
        elevationNoiseGenerator.setScale(1 / 200.0);

        PerlinOctaveGenerator detailNoiseGenerator = new PerlinOctaveGenerator(world.getSeed(), 4);
        detailNoiseGenerator.setScale(1 / 30.0);

        PerlinOctaveGenerator roughNoiseGenerator = new PerlinOctaveGenerator(world.getSeed(), 1);
        roughNoiseGenerator.setScale(1 / 100.0);

        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        double elevationNoise = elevationNoiseGenerator.noise(x, z, 0.5, 2.0);
        double detailNoise = detailNoiseGenerator.noise(x, z, 0.5, 2.0);
        double roughNoise = roughNoiseGenerator.noise(x, z, 0.5, 2.0);
        double height = (elevationNoise + detailNoise + roughNoise) * 20;
        player.sendActionBar(Message.text("H {0} E {1} D {2} R {3}", height, elevationNoise, detailNoise, roughNoise));
    }
}
