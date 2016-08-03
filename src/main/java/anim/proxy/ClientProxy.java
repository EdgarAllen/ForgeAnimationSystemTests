package anim.proxy;

import anim.blocks.lever.TileEntityLever;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModelLoader.setCustomModelResourceLocation(leverItem, 0, new ModelResourceLocation(leverBlock.getRegistryName().toString(), "inventory"));

        /**
         * Register a TileEntitySpecialRenderer. We use the AnimationTESR for animated blocks.
         */
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLever.class, new AnimationTESR<TileEntityLever>() {

            /**
             * Event Handler for events triggered from animations. Normally we just hand this off to the tileEntity for processing.
             * This gets called mid {@link AnimationTESR#renderTileEntityFast(TileEntity, double, double, double, float, int, VertexBuffer)}
             * before rendering occurs for the frame.
             * @param tileEntity our tileEntity
             * @param time The global world time for the current tick + partial tick progress, in seconds.
             * @param pastEvents List of events that were triggered since last tick.
             */
            @Override
            public void handleEvents(TileEntityLever tileEntity, float time, Iterable<Event> pastEvents) {
                super.handleEvents(tileEntity, time, pastEvents);
                tileEntity.handleEvents(time, pastEvents);
            }
        });
    }

    /**
     * loads the animation state machine definition file. Only needed to be loaded on the client side.
     * @param location The Resource location of the definition file. Usually located in "modid:asms/block/*.json"
     * @param parameters A mapping between animated variables in java and their names in the definition file.
     * @return The loaded state machine.
     */
    @Override
    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {
        return ModelLoaderRegistry.loadASM(location, parameters);
    }
}
