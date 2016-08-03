package anim;

import anim.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "anim", name = "Anim", version = "1.0.0.0")
public class AnimMod {

    @Mod.Instance("anim")
    public static AnimMod instance;

    @SidedProxy(clientSide = "anim.proxy.ClientProxy", serverSide = "anim.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final Logger log = LogManager.getLogger("Anim");

    public static final CreativeTabs creativeTab = new CreativeTabs("tabAnim") {
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.APPLE;
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }
}
