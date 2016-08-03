package anim.proxy;

import anim.blocks.lever.BlockLever;
import anim.blocks.lever.TileEntityLever;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    BlockLever leverBlock;
    ItemBlock leverItem;

    public void preInit(FMLPreInitializationEvent event) {
        leverBlock = new BlockLever();
        leverItem = new ItemBlock(leverBlock);
        leverItem.setRegistryName(leverBlock.getRegistryName());

        GameRegistry.register(leverBlock);
        GameRegistry.registerTileEntity(TileEntityLever.class, BlockLever.NAME);
        GameRegistry.register(leverItem);
    }

    /**
     * loads the animation state machine definition file. Only needs to be loaded client side. Server side we just return null.
     * @param location The {@link ResourceLocation} of the definition file. Usually located in "modid:asms/block/*.json"
     * @param parameters A mapping between animated variables in java and their names in the definition file.
     * @return The loaded state machine.
     */
    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {
        return null;
    }
}
