package anim.blocks.lever;

import anim.AnimMod;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockLever extends Block implements ITileEntityProvider {
    public static final String NAME = "lever";
    private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockLever() {
        super(Material.WOOD);

        setRegistryName(NAME);
        setUnlocalizedName(NAME);
        setCreativeTab(AnimMod.creativeTab);
    }

    /**
     *  We need an ExtendedBlockState to hold extra properties specifically for animation.
     *
     *  {@link Properties#AnimationProperty} is an unlisted property and holds the state for the animation. Unlisted
     *  properties do not need to be declared in your blockstate file.
     *
     *  {@link Properties#StaticProperty} is a boolean property that holds a special state for models. Models that are
     *  defined as static don't move and can be rendered faster. We split our model into separate parts for the static
     *  model in one json and the parts that will be animated in a separate json so we can render them in separate passes.
     *  See the 'static' section in {@link assets.anim.blockstates.lever.json} for an example.
     *  {@link assets.anim.models.block.lever.json} and {@link assets.anim.models.block.lever_slider.json} for an example
     *  on the splitting of the model files.
     *
     */
    @Nonnull
    @Override
    public ExtendedBlockState createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{ FACING, Properties.StaticProperty }, new IUnlistedProperty[]{ Properties.AnimationProperty });
    }

    /**
     * getActualState needs to add the property for {@link Properties#StaticProperty} so the animation system has
     * access to it.
     */
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(Properties.StaticProperty, true);
    }

    /**
     * Simple method to handle block clicks. {@link TileEntityLever#click()} Shows an example of how to transition
     * from one state to another from code.
     */
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        // We want to run on the client since the asm controller is client side only.
        if(world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TileEntityLever) {
                ((TileEntityLever)te).click();
            }
        }
        return true;
    }

    //region Boilerplate Code
    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityLever();
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_AABB;
    }

    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }

    @Nonnull
    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        super.getStateFromMeta(meta);
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
    //endregion
}
