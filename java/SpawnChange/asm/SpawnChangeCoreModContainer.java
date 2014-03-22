package SpawnChange.asm;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

/**
 * Created by A.K. on 14/03/22.
 */
public class SpawnChangeCoreModContainer extends DummyModContainer{
    public SpawnChangeCoreModContainer() {
        super(new ModMetadata());

        ModMetadata meta = getMetadata();
        meta.modId = "SpawnChangeCore";
        meta.name = "SpawnChangeCore";
        meta.version = "1.0.0";
        meta.authorList = Arrays.asList("takanasayo", "A.K.");
        meta.description = "Change some mobs spawn condition";
        meta.url = "";
        meta.credits = "";
        this.setEnabledState(true);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
