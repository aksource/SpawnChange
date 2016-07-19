package SpawnChange;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;

/**
 * モブのスポーンを調整するクラス
 * Created by A.K. on 14/10/18.
 */
public class SpawnCheckHook {
    private static final int BIOME_ID_HELL = 8;
    private static final int BIOME_ID_SKY = 9;
    public static Map<String, Integer> entitySpawnHightMap = Maps.newHashMap();
    public static Map<String, Integer> entitySpawnLightMap = Maps.newHashMap();
    public static Map<String, Set<Biome>> entitySpawnBiomesMap = Maps.newHashMap();
    private static Set<String> biomeExtraWordsSet = Sets.newHashSet();
    private static Set<Biome> vanillaBiomeSet = Sets.newHashSet();
    private static Set<Biome> overworldBiomeSet = Sets.newHashSet();

    public static void init() {
        generateVanillaBiomeSet();
        addMap(entitySpawnHightMap, SpawnChange.entitySpawnHeightConfig);
        addMap(entitySpawnLightMap, SpawnChange.entitySpawnLightConfig);
        addBiomeMap(entitySpawnBiomesMap, SpawnChange.entitySpawnBiomeConfig);
    }

    private static void addMap(Map<String, Integer> map, String[] strs) {
        String[] split;
        for (String str : strs) {
            split = str.split(":");
            if (split.length > 1 && Integer.getInteger(split[1], 0) > 0) {
                map.put(split[0], Integer.getInteger(split[1]));
            }
        }
    }

    private static void addBiomeMap(Map<String, Set<Biome>> map, String[] strs) {
        String[] split;
        for (String str : strs) {
            split = str.split(":");
            if (split.length > 1 && isValidString(split[1])) {
                if (map.containsKey(split[0])) {
                    map.get(split[1]).addAll(getBiomes(split[1]));
                } else {
                    map.put(split[0], getBiomes(split[1]));
                }
            }
        }
    }

    private static void generateVanillaBiomeSet() {
        for (Biome biome : Biome.REGISTRY) {
            if (biome != null) {
                vanillaBiomeSet.add(biome);
            }
        }
        overworldBiomeSet.addAll(vanillaBiomeSet);
        overworldBiomeSet.remove(Biome.getBiome(BIOME_ID_HELL));
        overworldBiomeSet.remove(Biome.getBiome(BIOME_ID_SKY));
    }

    private static boolean isValidString(String string) {
        return biomeExtraWordsSet.contains(string) || isBiomeName(string);
    }

    private static boolean isBiomeName(String str) {

        for (Biome biome : Biome.REGISTRY) {
            if (biome.getBiomeName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static Set<Biome> getBiomes(String str) {
        if (str.equals("OVERWORLD")) {
            return overworldBiomeSet;
        }

        if (str.equals("VANILLA")) {
            return vanillaBiomeSet;
        }
        Set<Biome> biomeSet = Sets.newHashSet();
        for (Biome biome : Biome.REGISTRY) {
            if (biome != null && biome.getBiomeName().equals(str)) {
                biomeSet.add(biome);
            }
        }
        return biomeSet;
    }


    @SubscribeEvent
    public void spawnCheck(LivingSpawnEvent.CheckSpawn event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        String entityName = EntityList.getEntityString(entityLiving);
        BlockPos blockPos = new BlockPos(event.getX(), event.getY(), event.getZ());
        Biome biome = event.getWorld().getBiomeGenForCoords(blockPos);
        if (entitySpawnBiomesMap.containsKey(entityName)) {
            if (!entitySpawnBiomesMap.get(entityName).contains(biome)) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        if (entitySpawnHightMap.containsKey(entityName)) {
            if (entitySpawnHightMap.get(entityName) < event.getY()) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        int lightValue = event.getWorld().getLight(blockPos);
        if (entitySpawnLightMap.containsKey(entityName)) {
            if (entitySpawnLightMap.get(entityName) < lightValue) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
    static{
        biomeExtraWordsSet.add("OVERWORLD");
        biomeExtraWordsSet.add("VANILLA");
    }
}
