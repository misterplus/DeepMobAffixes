package plus.misterplus.deepmobaffixes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod(modid = DeepMobAffixes.MODID, name = DeepMobAffixes.NAME, version = DeepMobAffixes.VERSION, dependencies = "required-after:deepmoblearning@[1.12.2-2.5.2,);")
public class DeepMobAffixes
{
    public static final String MODID = "deepmobaffixes";
    public static final String NAME = "Deep Mob Affixes";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load();
        Config.initConfigValues();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        List<String> available = new ArrayList<String>(){{
            addAll(Arrays.asList("speed", "regen_party", "empowered_glitches", "knockback_immunity", "blaze_invaders", "loot_hoarders", "thunderdome"));
        }};
        for (String key : Config.trialAffixes.keySet()) {
            if (!Config.trialAffixes.get(key).getBoolean()) {
                available.remove(key);
            }
        }
        EventHandler.available = available;
    }
}
