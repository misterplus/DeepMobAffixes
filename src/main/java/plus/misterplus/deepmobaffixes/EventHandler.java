package plus.misterplus.deepmobaffixes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xt9.deepmoblearning.common.items.ItemTrialKey;
import xt9.deepmoblearning.common.util.ItemStackNBTHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class EventHandler {
    public static List<String> available;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();

            NonNullList<ItemStack> inventory = NonNullList.create();
            inventory.addAll(player.inventory.mainInventory);
            inventory.addAll(player.inventory.offHandInventory);

            NonNullList<ItemStack> trialKeys = getTrialKeys(inventory);
            trialKeys.forEach(EventHandler::modifyTrialKey);
        }
    }

    private static NonNullList<ItemStack> getTrialKeys(NonNullList<ItemStack> inventory) {
        NonNullList<ItemStack> result = NonNullList.create();
        inventory.forEach(stack -> {
            if(stack.getItem() instanceof ItemTrialKey) {
                result.add(stack);
            }
        });

        return result;
    }

    private static void modifyTrialKey(ItemStack trialKey) {
        if (trialKey.hasTagCompound() && trialKey.getTagCompound().hasKey("affixes")) { //has affixes
            List<String> list = ItemStackNBTHelper.getStringList(trialKey, "affixes"); //get existing affixes
            List<String> selectable = new ArrayList<>(available); //affixes whitelist
            List<String> newList = new ArrayList<>();
            for (String key : list) { //remove existing ones from whitelist
                if (selectable.contains(key)) //if it's blacklisted, don't add it into the new list
                    newList.add(key);
                selectable.remove(key); //blacklist existing affixes
            }
            if (list.size() == newList.size()) //no blacklisted affix
                return;
            //now selectable contains all available affixes, and newList contains affixes to be kept
            int total = list.size() - newList.size(); //total number of affixes to be readded
            for (int i = 0; i < total; i++) {
                if (selectable.size() == 0) //no more selectable affixes, skip
                    break;
                String key = selectable.get(new Random().nextInt(selectable.size()));
                newList.add(key);
                selectable.remove(key);
            }
            trialKey.getTagCompound().removeTag("affixes");
            if (newList.size() == 0) { //no more affixes
                return;
            }
            //add new nbt tag
            NBTTagList stringList = new NBTTagList();
            int index = 0;
            for (String s : newList) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString(index+"", s);
                stringList.appendTag(tag);
                index++;
            }
            trialKey.getTagCompound().setTag("affixes", stringList);
        }
    }
}
