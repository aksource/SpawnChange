package SpawnChange;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;

/**
 * Created by A.K. on 14/10/18.
 */
public class SpawnCheckHook {
    public static Map<String, Integer> entitySpawnHightMap = Maps.newHashMap();
    public static Map<String, Integer> entitySpawnLightMap = Maps.newHashMap();
    public static Map<String, Set<BiomeGenBase>> entitySpawnBiomesMap = Maps.newHashMap();
    private static Set<String> biomeExtraWordsSet = Sets.newHashSet();
    private static Set<BiomeGenBase> vanillaBiomeSet = Sets.newHashSet();
    private static Set<BiomeGenBase> overworldBiomeSet = Sets.newHashSet();

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
            if (split.length > 1 && Integer.parseInt(split[1]) > 0) {
                map.put(split[0], Integer.parseInt(split[1]));
            }
        }
    }

    private static void addBiomeMap(Map<String, Set<BiomeGenBase>> map, String[] strs) {
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
        for (BiomeGenBase biomeGenBase : BiomeGenBase.getBiomeGenArray()) {
            if (biomeGenBase != null) {
                vanillaBiomeSet.add(biomeGenBase);
            }
        }
        overworldBiomeSet.addAll(vanillaBiomeSet);
        overworldBiomeSet.remove(BiomeGenBase.hell);
        overworldBiomeSet.remove(BiomeGenBase.sky);
    }

    private static boolean isValidString(String string) {
        return biomeExtraWordsSet.contains(string) || isBiomeName(string);
    }

    private static boolean isBiomeName(String str) {
        for (BiomeGenBase biomeGenBase : BiomeGenBase.getBiomeGenArray()) {
            if (biomeGenBase != null && biomeGenBase.biomeName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static Set<BiomeGenBase> getBiomes(String str) {
        if (str.equals("OVERWORLD")) {
            return overworldBiomeSet;
        }

        if (str.equals("VANILLA")) {
            return vanillaBiomeSet;
        }
        Set<BiomeGenBase> biomeSet = Sets.newHashSet();
        for (BiomeGenBase biomeGenBase : BiomeGenBase.getBiomeGenArray()) {
            if (biomeGenBase != null && biomeGenBase.biomeName.equals(str)) {
                biomeSet.add(biomeGenBase);
            }
        }
        return biomeSet;
    }


    @SubscribeEvent
    public void spawnCheck(LivingSpawnEvent.CheckSpawn event) {
        EntityLivingBase entityLiving = event.entityLiving;
        String entityName = EntityList.getEntityString(entityLiving);
        BlockPos blockPos = new BlockPos(event.x, event.y, event.z);
        BiomeGenBase biome = event.world.getBiomeGenForCoords(blockPos);
        if (entitySpawnBiomesMap.containsKey(entityName)) {
            if (!entitySpawnBiomesMap.get(entityName).contains(biome)) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        if (entitySpawnHightMap.containsKey(entityName)) {
            if (entitySpawnHightMap.get(entityName) < event.y) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        int lightValue = event.world.getLight(blockPos);
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
