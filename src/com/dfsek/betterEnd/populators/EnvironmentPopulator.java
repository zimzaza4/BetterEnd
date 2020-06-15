package com.dfsek.betterend.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.Main;
import com.dfsek.betterend.structures.ShatteredTree;
import com.dfsek.betterend.structures.Tree;

public class EnvironmentPopulator extends BlockPopulator {
	private Main main = Main.getInstance();
	private int min = main.getConfig().getInt("trees.min-per-chunk");
	private int max = main.getConfig().getInt("trees.max-per-chunk");
	private int herdChance = main.getConfig().getInt("aether.animals.herd-chance-per-chunk", 15);
	private int herdMin = main.getConfig().getInt("aether.animals.herd-min-size", 2);
	private int herdMax = main.getConfig().getInt("aether.animals.herd-max-size", 5);
	private int obMax = main.getConfig().getInt("trees.obsidian-pillars.max-height");
	private int obMin = main.getConfig().getInt("trees.obsidian-pillars.min-height");
	private int heatNoise = main.getConfig().getInt("outer-islands.heat-noise");
	private int biomeSize = main.getConfig().getInt("outer-islands.biome-size");
	private boolean allAether = main.getConfig().getBoolean("all-aether", false);
	private int baseH = main.getConfig().getInt("outer-islands.island-height", 64);
	
	@SuppressWarnings("deprecation")
	public void populate(World world, Random random, Chunk chunk) {
		//taiga
		
		//trees
		int amount = random.nextInt(max-min)+min;  // Amount of trees
		if((Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20) || allAether)
			for (int i = 0; i < amount; i++) {
				int X = random.nextInt(15);
				int Z = random.nextInt(15);
				int Y;
				for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() == Material.AIR && Y>0; Y--); // Find the highest block of the (X,Z) coordinate chosen.
				if (Y > baseH-1 && Y < 255) {
					Location blockLocation = chunk.getBlock(X, Y, Z).getLocation();
					switch(Main.getBiome(blockLocation.getBlockX(), blockLocation.getBlockZ(), world.getSeed())) {
					case "AETHER":
						for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK) && Y>0; Y--);
						if(Y > 1) {
							if(random.nextInt(100) < 85) {
								world.generateTree(blockLocation, TreeType.TREE);
							} else {
								world.generateTree(blockLocation, TreeType.BIG_TREE);
							}
						}
						break;
					case "AETHER_FOREST":
						if(random.nextInt(20) < 12 && blockLocation.getBlock().getType() == Material.GRASS_BLOCK) {
							//world.generateTree(blockLocation, TreeType.BIG_TREE);
							new Tree(blockLocation, 1.5, random, random.nextInt(4)+10, 3, "OAK");
						}
						break;
					case "AETHER_HIGHLANDS":
						for (int j = 0; j < 16; j++) {
							int X1 = random.nextInt(15);
							int Z1 = random.nextInt(15);
							int Y1;
							for (Y1 = world.getMaxHeight()-1; (chunk.getBlock(X1, Y1, Z1).getType() != Material.GRASS_BLOCK && 
									chunk.getBlock(X1, Y1, Z1).getType() != Material.PODZOL &&
									chunk.getBlock(X1, Y1, Z1).getType() != Material.COARSE_DIRT &&
									chunk.getBlock(X1, Y1, Z1).getType() != Material.SNOW) && Y1>0; Y1--);
							if(Y1 > 1) {
								blockLocation = chunk.getBlock(X, Y1, Z).getLocation();
								if(blockLocation.getBlock().getType() == Material.SNOW) {
									blockLocation.getBlock().setType(Material.AIR);
									blockLocation.subtract(0, 1, 0);
								}
								if(random.nextInt(100) < 85) {
									world.generateTree(blockLocation, TreeType.REDWOOD);
								} else {
									world.generateTree(blockLocation, TreeType.TALL_REDWOOD);
								}

							}
						}
						break;
					case "AETHER_HIGHLANDS_FOREST":
						if(random.nextInt(20) < 10 && (blockLocation.getBlock().getType() == Material.GRASS_BLOCK ||
								blockLocation.getBlock().getType() == Material.PODZOL ||
								blockLocation.getBlock().getType() == Material.COARSE_DIRT ||
								blockLocation.getBlock().getType() == Material.SNOW ||
								blockLocation.getBlock().getType() == Material.GRAVEL)) {
							new Tree(blockLocation, 1.5, random, 3*(random.nextInt(3)+5), 3, "SPRUCE");
						}
						break;
					case "SHATTERED_END":
						if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(10) < 7) {
							if(random.nextInt(100) < 60) {
								int[] upBound = {random.nextInt(obMax-obMin)+obMin, 0, 0, 0};
								int[] lowBound = {random.nextInt(obMax-obMin)+obMin, 0, 0, 0};
								int maxH = 0;
								int maxHVal = upBound[0];
								for(int j = 1; j < upBound.length; j++) {
									upBound[j] = upBound[j-1] + random.nextInt(5)-2;
									if(upBound[j] > maxHVal) {
										maxH = j;
										maxHVal = upBound[j];
									}
								}
								for(int j = 1; j < lowBound.length; j++) {
									lowBound[j] = lowBound[j-1] + random.nextInt(7)-3;
								}
								for(int j = -lowBound[0]; j < upBound[0]; j++) {
									if(world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_ROD ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE_BRICK_SLAB ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE_BRICK_WALL ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).isPassable()) world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).setType(Material.OBSIDIAN);
								}
								for(int j = -lowBound[1]; j < upBound[1]; j++) {
									if(world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_ROD ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE_BRICK_SLAB ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE_BRICK_WALL ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z).isPassable()) world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z).setType(Material.OBSIDIAN);
								}
								for(int j = -lowBound[2]; j < upBound[2]; j++) {
									if(world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z+1).getType() == Material.END_ROD ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z+1).getType() == Material.END_STONE_BRICK_SLAB ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z+1).getType() == Material.END_STONE_BRICK_WALL ||
											world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z+1).isPassable()) world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z+1).setType(Material.OBSIDIAN);
								}
								for(int j = -lowBound[3]; j < upBound[3]; j++) {
									if(world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).getType() == Material.END_STONE ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z+1).getType() == Material.END_ROD ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z+1).getType() == Material.END_STONE_BRICK_SLAB ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z+1).getType() == Material.END_STONE_BRICK_WALL ||
											world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z+1).isPassable()) world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z+1).setType(Material.OBSIDIAN);
								}
								if(random.nextInt(100) < 25) {
									switch(maxH) {
									case 0:
										world.spawn(new Location(world, (chunk.getX()*16)+X+0.5, upBound[0]+Y, (chunk.getZ()*16)+Z+0.5), EnderCrystal.class, (enderCrystal) -> enderCrystal.setShowingBottom(false));
										break;
									case 1:
										world.spawn(new Location(world, (chunk.getX()*16)+X+1.5, upBound[1]+Y, (chunk.getZ()*16)+Z+0.5), EnderCrystal.class, (enderCrystal) -> enderCrystal.setShowingBottom(false));
										break;
									case 2:
										world.spawn(new Location(world, (chunk.getX()*16)+X+0.5, upBound[2]+Y, (chunk.getZ()*16)+Z+1.5), EnderCrystal.class, (enderCrystal) -> enderCrystal.setShowingBottom(false));
										break;
									case 3:
										world.spawn(new Location(world, (chunk.getX()*16)+X+1.5, upBound[3]+Y, (chunk.getZ()*16)+Z+1.5), EnderCrystal.class, (enderCrystal) -> enderCrystal.setShowingBottom(false));
										break;
									}
								}
							} else {
								int upBound = (int) (random.nextInt((int) ((obMax*0.75)-(obMin*0.75)))+(obMin*0.75));
								int lowBound = (int) (random.nextInt((int) ((obMax*0.75)-(obMin*0.75)))+(obMin*0.75));
								for(int j = -lowBound; j < upBound; j++) {
									world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).setType(Material.OBSIDIAN);
								}
							}
						}
						break;
					case "SHATTERED_FOREST":
						if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(20) < 6 && i == 0) {
							new ShatteredTree(blockLocation, 2, random, random.nextInt(10)+20, 3);
						} else if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(20) < 10) {
							new ShatteredTree(blockLocation, 1, random, random.nextInt(5)+5, 3);
						}
						break;
					default:
						if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(10) < 8) {
							world.generateTree(blockLocation.add(0,1,0), TreeType.CHORUS_PLANT);
						}
					}
				}
			}
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 4);
		for(int X = 0; X < 16; X++) {
			for(int Z = 0; Z < 16; Z++) {
				double biomeNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/biomeSize, (double) (chunk.getZ()*16+Z)/biomeSize, 0.5D, 0.5D);
				double heatNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/heatNoise, (double) (chunk.getZ()*16+Z)/heatNoise, 0.5D, 0.5D);
				int Y;
				for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() != Material.SPRUCE_LEAVES) && Y>0; Y--);
				if(heatNoiseLvl < -0.5 && random.nextInt(100) < -50*(heatNoiseLvl+0.5) && chunk.getBlock(X, Y, Z).getType() == Material.SPRUCE_LEAVES) {
					chunk.getBlock(X, Y+1, Z).setType(Material.SNOW);
				}
				if(heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || allAether)) {
					world.setBiome(chunk.getX()*16+X, chunk.getZ()*16+Z, Biome.TAIGA);
					
					if(random.nextInt(1000) < 2) {
						for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK && 
								chunk.getBlock(X, Y, Z).getType() != Material.GRAVEL &&
								chunk.getBlock(X, Y, Z).getType() != Material.PODZOL &&
								chunk.getBlock(X, Y, Z).getType() != Material.COARSE_DIRT) && Y>0; Y--);
						if(Y > 1) world.getBlockAt(chunk.getX()*16+X, Y+1, chunk.getZ()*16+Z).setType((random.nextBoolean()) ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);
					}
				}
			}
		}
		
		//animals
		if(random.nextInt(100) < herdChance) {
			int size = random.nextInt(herdMax-herdMin)+herdMin;
			EntityType type;
			switch(random.nextInt(3)) {
			case 0:
				type = EntityType.CHICKEN;
				break;
			case 1:
				type = EntityType.COW;
				break;
			default:
				type = EntityType.SHEEP;
				break;
			}
			int X = random.nextInt(16);
			int Z = random.nextInt(16);
			if(Main.getBiome(chunk.getX()*16 + X, chunk.getZ()*16 + Z, world.getSeed()).equals("AETHER") || Main.getBiome(chunk.getX()*16 + X, chunk.getZ()*16 + Z, world.getSeed()).equals("AETHER_HIGHLANDS")) {
				for (int i = 0; i < size; i++) {
					int Y;
					for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK && Y>0; Y--);
					if(Y > 1) world.spawnEntity(new Location(world, chunk.getX()*16 + X + random.nextInt(3), Y + 1, chunk.getZ()*16 + Z + random.nextInt(3)), type);
				}
			}
		}
	}


} 