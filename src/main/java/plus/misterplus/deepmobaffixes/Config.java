package plus.misterplus.deepmobaffixes;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod.EventBusSubscriber
public class Config {
    private static Configuration config = new Configuration(new File("config/" + DeepMobAffixes.MODID + ".cfg"));
    public static ConfigCategory trialAffixes = new ConfigCategory("trial affixes");

    public static void load() {
        config.load();
    }

    public static void initConfigValues() {
        initTrialAffixes();
        config.save();
    }

    private static void initTrialAffixes() {
        trialAffixes.setComment("Affixes for all trials.\nSet to false to disable a certain affix from appearing.");
        config.setCategoryComment(trialAffixes.getName(), trialAffixes.getComment());

        trialAffixes.put("speed", config.get(trialAffixes.getName(), "speed", true));
        trialAffixes.put("regen_party", config.get(trialAffixes.getName(), "regen_party", true));
        trialAffixes.put("empowered_glitches", config.get(trialAffixes.getName(), "empowered_glitches", true));
        trialAffixes.put("knockback_immunity", config.get(trialAffixes.getName(), "knockback_immunity", true));
        trialAffixes.put("blaze_invaders", config.get(trialAffixes.getName(), "blaze_invaders", true));
        trialAffixes.put("loot_hoarders", config.get(trialAffixes.getName(), "loot_hoarders", true));
        trialAffixes.put("thunderdome", config.get(trialAffixes.getName(), "thunderdome", true));
    }
}
