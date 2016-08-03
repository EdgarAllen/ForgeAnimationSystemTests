package anim.blocks.lever;

import anim.AnimMod;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityLever extends TileEntity {

    /**
     *  Our Animation state controller. We can use this to, for example, query the current state, or transition
     *  to a different state.
     *
     *  This is assigned through the proxy and is null server side.
     */
    private final IAnimationStateMachine asm;

    /**
     * Here we define our variables we wish to act as parameters in the state machine. These get mapped to parameters
     * that we can access in the asm json files. Use these to trigger events or transition to a different state. The
     * values are set through code. See {@link #click()} for an example.
     */
    private final VariableValue clickTime = new VariableValue(Float.NEGATIVE_INFINITY);

    public TileEntityLever() {

        /**
         * This is loaded through the proxy. Server side returns null. Here we also map {@link VariableValue} from above
         * that we wish to access from the asm json file. See {@link assets.anim.asms.block#lever.json}
         */
        asm = AnimMod.proxy.load(new ResourceLocation("anim", "asms/block/lever.json"), ImmutableMap.<String, ITimeValue>of(
            "click_time", clickTime
        ));
    }

    /**
     * Expose the Animation Capability.
     * @param capability
     * @param side
     * @return
     */
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side) {
        return capability == CapabilityAnimation.ANIMATION_CAPABILITY || super.hasCapability(capability, side);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side) {
        if(capability == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return super.getCapability(capability, side);
    }

    /**
     * Event handler to process events triggered from animations. It gets called from your {@link AnimationTESR}.
     * This is only called client side, so if your events trigger something more than special effects, you'll have
     * to send a message back to the server. This could potentially be called each frame tick, so anything computationally
     * intensive should probably be scheduled for a later update tick.
     * @param time The global world time for the current tick + partial tick progress, in seconds.
     * @param pastEvents List of events that were triggered since last tick.
     */
    public void handleEvents(float time, Iterable<Event> pastEvents) { }

    /**
     * Example method showing how to change states from code.
     */
    public void click() {
        if(asm != null) {
            if(asm.currentState().equals("open")) {
                float time = Animation.getWorldTime(getWorld(), Animation.getPartialTickTime());
                clickTime.setValue(time);
                asm.transition("closing");
            } else if(asm.currentState().equals("closed")) {
                float time = Animation.getWorldTime(getWorld(), Animation.getPartialTickTime());
                clickTime.setValue(time);
                asm.transition("opening");
            }
        }
    }
}
