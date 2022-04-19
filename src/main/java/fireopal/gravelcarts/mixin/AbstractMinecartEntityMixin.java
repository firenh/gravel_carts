package fireopal.gravelcarts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fireopal.gravelcarts.GravelCarts;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {
	private Vec3d lastPos;
	private double lastMaxSpeedMultiplier;

	private double getMaxSpeedMultiplier() {
		if (((AbstractMinecartEntity)(Object)this).getPos().equals(lastPos)) {
			return lastMaxSpeedMultiplier;
		}

		BlockState underState = ((AbstractMinecartEntity)(Object)this).world.getBlockState(((AbstractMinecartEntity)(Object)this).getBlockPos().down());
		double multiplier = 1; 

        if (underState.isIn(GravelCarts.MINECART_SPEED_2_5X_BLOCKS)) {
			multiplier = 2.5;
		} else if (underState.isIn(GravelCarts.MINECART_SPEED_2X_BLOCKS)) {
			multiplier = 2;
		} else if (underState.isIn(GravelCarts.MINECART_SPEED_1_5X_BLOCKS)) {
			multiplier = 1.5;
		}  

		int down = 2;
		boolean cappedLayers = GravelCarts.config.maxGravelLayers >= 0;

		while (
			((AbstractMinecartEntity)(Object)this).world.getBlockState(
				((AbstractMinecartEntity)(Object)this).getBlockPos().down(down)
			).isIn(GravelCarts.MINECART_SPEED_INCREASE_UNDER_TWO)
		) {
			multiplier *= 1 + (.25 / Math.pow(down - 1, GravelCarts.layerPowInDivisor()));
			down += 1;

			if (cappedLayers && down > GravelCarts.config.maxGravelLayers) break;
		}

		lastMaxSpeedMultiplier = multiplier;
		return multiplier;
	}

	@Inject(method = "getMaxSpeed", at = @At("RETURN"), cancellable = true)
	private void getMaxSpeed(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(cir.getReturnValue() * getMaxSpeedMultiplier());
    }

	@Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
	private void getVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
		Vec3d velocityVec = ((AbstractMinecartEntity)(Object)this).getVelocity();
		double velocity = Math.sqrt(Math.pow(velocityVec.x, 2) + Math.pow(velocityVec.y, 2) + Math.pow(velocityVec.z, 2));

		if (velocity > 0.4) {
			float friction = 1 - cir.getReturnValue();
			// System.out.println("Friction: " + friction);

			float mul = GravelCarts.getVelocityMultiplierMultiplier(velocity);
			float sub = GravelCarts.getVelocityMultiplierSubtractor(velocity);

			float newVelocityMultiplier = 1 + (friction * mul) - sub;
			// System.out.println(newVelocityMultiplier);
			cir.setReturnValue(newVelocityMultiplier);
		} 
	}
}
