import com.dfsek.betterend.world.generation.biomes.Biome;
import com.dfsek.betterend.world.generation.biomes.BiomeGrid;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for testing and troubleshooting distribution of BiomeGrid biomes
 */
public class TestBiomeDistribution {
    public static void main(String[] args) {
        System.out.println("Beginning biome distribution test.");
        long t = System.nanoTime();
        System.out.println("*-----------------------------------------*");
        Map<Biome, Integer> map = new HashMap<>();
        for(int x = 0; x < 100000; x++) {
            for(int z = 0; z < 10; z++) {
                Biome b = BiomeGrid.blank().getBiome(x, z);
                map.put(b, map.getOrDefault(b, 0) + 1);
            }
        }
        for(Map.Entry<Biome, Integer> e : map.entrySet()) {
            System.out.println(e.getKey().toString() + ": " + e.getValue());
        }
        System.out.println("*-----------------------------------------*");
        System.out.println("Aether Aggregated: " + (map.get(Biome.AETHER) + map.get(Biome.AETHER_FOREST) + map.get(Biome.AETHER_HIGHLANDS) + map.get(Biome.AETHER_HIGHLANDS_FOREST)));
        System.out.println("Void Aggregated: " + (map.get(Biome.VOID) + map.get(Biome.STARFIELD)));
        System.out.println("End Aggregated: " + (map.get(Biome.END)));
        System.out.println("Shattered End Aggregated: " + (map.get(Biome.SHATTERED_END) + map.get(Biome.SHATTERED_FOREST)));
        System.out.println("*-----------------------------------------*");
        System.out.println("Done. Time elapsed: " + (System.nanoTime() - t)/1000000L + "ms. " + (System.nanoTime() - t)/(100000L*10L) + "ns per calculation.");
    }
}
