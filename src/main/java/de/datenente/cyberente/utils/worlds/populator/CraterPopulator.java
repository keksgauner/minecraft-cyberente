package de.datenente.cyberente.utils.worlds.populator;

import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
public class CraterPopulator  extends BlockPopulator {
    int maxCraterCount = 5;
    int minRadius = 3;
    int maxRadius = 10;
    int minDepth = 1;
    int maxDepth = 5;

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        int craterCount = random.nextInt(this.getMaxCraterCount()) + 1;

        for (int i = 0; i < craterCount; i++) {
            int centerX = worldX + random.nextInt(16);
            int centerZ = worldZ + random.nextInt(16);
            int centerY = limitedRegion.getHighestBlockYAt(centerX, centerZ);

            int radius = random.nextInt(this.getMaxRadius() - this.getMinRadius() + 1) + this.getMinRadius();
            int depth = random.nextInt(this.getMaxDepth() - this.getMinDepth() + 1) + this.getMinDepth();

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    int distanceSquared = x * x + z * z;
                    if (distanceSquared <= radius * radius) {
                        int craterDepth = depth - (int) Math.sqrt(distanceSquared);
                        for (int y = 0; y < craterDepth; y++) {
                            limitedRegion.setType(centerX + x, centerY - y, centerZ + z, Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
