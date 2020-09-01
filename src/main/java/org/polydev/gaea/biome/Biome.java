package org.polydev.gaea.biome;

import com.dfsek.betterend.population.structures.EndStructure;
import org.polydev.gaea.tree.Tree;

import java.util.Random;

public interface Biome {
    EndStructure getRandomStructure(Random r);
    Tree getTree(Random r);
    int getTreeDensity();
    boolean overrideStructureChance();
    BiomeTerrain getGenerator();
}