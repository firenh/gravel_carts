package fireopal.gravelcarts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GravelCarts implements ModInitializer {
	public static final String MODID = "gravel_carts";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final FOModVersion VERSION = FOModVersion.fromString("0.1.0");

	public static GravelCartsConfig config = GravelCartsConfig.init();

	@Override
	public void onInitialize() {
		config = GravelCartsConfig.init();
	}

	public static Identifier id(String id) {
		return new Identifier(MODID, id);
	}

	public static final TagKey<Block> MINECART_SPEED_INCREASE_UNDER_TWO = registerBlockTag("minecart_speed_increase_under_two");
	public static final TagKey<Block> MINECART_SPEED_2_5X_BLOCKS = registerBlockTag("minecart_speed_2_5x_blocks");
	public static final TagKey<Block> MINECART_SPEED_2X_BLOCKS = registerBlockTag("minecart_speed_2x_blocks");
	public static final TagKey<Block> MINECART_SPEED_1_5X_BLOCKS = registerBlockTag("minecart_speed_1_5x_blocks");

	public static float getVelocityMultiplierMultiplier(double velocity) {
		return (float) ((velocity - 0.4) / 2);
	}

	private static final double VELOCITY_SUBTRACTOR_POW_BASE = 0.995;
	public static final double LAYER_POW_IN_DIVISOR = layerPowInDivisor();

	public static double layerPowInDivisor() {
		// return Math.sqrt(1.5);
		return 1;
	}

	public static float getVelocityMultiplierSubtractor(double velocity) {
		return (float) (-(Math.pow(VELOCITY_SUBTRACTOR_POW_BASE, velocity)) + 1);
	}

	private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.of(Registry.BLOCK_KEY, id(id));
    }
}
