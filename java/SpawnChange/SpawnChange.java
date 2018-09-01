package SpawnChange;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by A.K. on 14/10/18.
 */
@Mod(modid = SpawnChange.MOD_ID,
        name = SpawnChange.MOD_NAME,
        version = SpawnChange.MOD_VERSION,
        dependencies = SpawnChange.MOD_DEPENDENCIES,
        useMetadata = true,
        acceptedMinecraftVersions = SpawnChange.MOD_MC_VERSION)
public class SpawnChange {

    public static final String MOD_ID = "SpawnChange";
    public static final String MOD_NAME = "SpawnChange";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[12.17.0,)";
    public static final String MOD_MC_VERSION = "[1.9,1.10.99]";
    static String[] entitySpawnHeightConfig = new String[] {
            "Slime:16"
    };
    static String[] entitySpawnLightConfig = new String[] {
            "PigZombie:7",
            "Ghast:7",
            "Blaze:7",
            "LavaSlime:7"
    };
    static String[] entitySpawnBiomeConfig = new String[]{
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
