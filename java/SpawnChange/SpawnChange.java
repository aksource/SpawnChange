package SpawnChange;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by A.K. on 14/10/18.
 */
@Mod(modid="SpawnChange", name="SpawnChange", version="@VERSION@",dependencies="required-after:FML", useMetadata = true)
public class SpawnChange {
    @Mod.Instance("SpawnChange")
    public static SpawnChange instance;
    public static String[] entitySpawnHeightConfig = new String[] {
            "Slime:16"
    };
    public static String[] entitySpawnLightConfig = new String[] {
            "PigZombie:7",
            "Ghast:7",
            "Blaze:7",
            "LavaSlime:7"
    };
    public static String[] entitySpawnBiomeConfig = new String[]{
            "PigZombie:Hell"
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        config.load();
        entitySpawnHeightConfig = config.get(Configuration.CATEGORY_GENERAL, "entitySpawnHeightConfig", entitySpawnHeightConfig,
                "Mob Spawn Height Configuration. ex:MOB_NAME:SPAWNABLE_HEIGHT").getStringList();
        entitySpawnLightConfig = config.get(Configuration.CATEGORY_GENERAL, "entitySpawnLightConfig", entitySpawnLightConfig,
                "Mob Spawn Light Configuration. ex:MOB_NAME:SPAWNABLE_LIGHT_VALUE").getStringList();
        entitySpawnBiomeConfig = config.get(Configuration.CATEGORY_GENERAL, "entitySpawnBiomeConfig", entitySpawnBiomeConfig,
                "Mob Spawn Biomes Configuration. ex:MOB_NAME:SPAWNABLE_BIOMES. Extra words are available:OVERWORLD, VANILLA").getStringList();
        config.save();
        SpawnCheckHook.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpawnCheckHook());
    }
}
