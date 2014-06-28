package SpawnChange.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by A.K. on 14/03/22.
 */
public class SpawnChangeCorePlugin implements IFMLLoadingPlugin{
    public static boolean portalSpawn;
    public static int netherSpawnLightValue;
    public static int SlimeSpawnHeight;
    public static Logger logger = Logger.getLogger("SpawnChange");
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                "SpawnChange.asm.EntitySlimeTransformer",
                "SpawnChange.asm.EntityNetherMobTransformer",
                "SpawnChange.asm.BlockObsidianTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return "SpawnChange.asm.SpawnChangeCoreModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.containsKey("mcLocation"))
        {
            File mcLocation = (File) data.get("mcLocation");
            File configLocation = new File(mcLocation, "config");
            File configFile = new File(configLocation, "SpawnChangeCore.cfg");
            initConfig(configFile);
        }
    }

    private void initConfig(File configFile) {
        Configuration config = new Configuration(configFile);
        config.load();
        portalSpawn = config.get(Configuration.CATEGORY_GENERAL, "portalSpawn", false, "ネザーゲートに敵スポーンを許可する。バニラは true").getBoolean(false);
        netherSpawnLightValue = config.get(Configuration.CATEGORY_GENERAL, "netherSpawnLightValue", 7, "ネザーの敵がスポーンする明るさ。バニラは 15, min=0, max=15").getInt();
        netherSpawnLightValue = (netherSpawnLightValue < 0)?0:(netherSpawnLightValue > 15)?15:netherSpawnLightValue;
        SlimeSpawnHeight = config.get(Configuration.CATEGORY_GENERAL, "SlimeSpawnHeight", 16, "スライムがスポーンする高さ。バニラは 40, min = 0, max = 255").getInt();
        SlimeSpawnHeight = (SlimeSpawnHeight <0)?0:(SlimeSpawnHeight>255)?255:SlimeSpawnHeight;
        config.save();
    }
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
