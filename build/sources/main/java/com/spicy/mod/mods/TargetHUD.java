package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.Render2DEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.utils.ColorUtils;
import com.spicy.utils.RenderUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.MathHelper;
import net.optifine.util.MathUtils;
import org.lwjgl.opengl.GL11;
import pisi.unitedmeows.eventapi.event.listener.Listener;

import java.awt.*;


public final class TargetHUD extends Mod {
	private double healthBarWidth;


	public TargetHUD() {
		super("TargetHUD", "shows target info", 0, Category.COMBAT);
	}
   


	public Listener<Render2DEvent> renderListener = new Listener<>(event -> {
		float scaledWidth = (float)event.getScaledResolution().getScaledWidth();
		float scaledHeight = (float)event.getScaledResolution().getScaledHeight();
		KillAura aura = (KillAura) Spicy.getINSTANCE().modManager.getMod(KillAura.class);
		if (aura.targets != null && aura.targets.size() > aura.index && aura.isState() && getMinecraft().theWorld != null) {
			if (aura.targets.get(aura.index) instanceof EntityOtherPlayerMP) {
				EntityOtherPlayerMP target = (EntityOtherPlayerMP) aura.targets.get(aura.index);
				float x = scaledWidth / 2.0F - 70.0F;
				float y = scaledHeight / 2.0F + 80.0F;

				int healthColor = getHealthColor(target.getHealth() / 2, target.getMaxHealth() / 2).getRGB();
				String targetHealth = String.valueOf((float)((int) target.getHealth()) / 2.0F);
				String myHealth = String.valueOf((float)((int)getMinecraft().thePlayer.getHealth()) / 2.0F);

				double hpPercentage = (target.getHealth() / 2) / (target.getMaxHealth() / 2);
				hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0D, 1.0D);
				double hpWidth = 92.0D * hpPercentage;
				int armorValue = aura.targets.get(aura.index).getTotalArmorValue();
				int armorWidth = (int) (armorValue / 20.0);
				this.healthBarWidth = RenderUtils.animate(hpWidth, this.healthBarWidth, 0.3529999852180481D);

				RenderUtils.drawRoundedRect(x, y, (x + 140.0F), (y + 40.0F), 10, Integer.MIN_VALUE);
				RenderUtils.drawRect((x + 40.0F), (y + 36), (float) ((x + 40.0F) + this.healthBarWidth), (y + 37), healthColor);

				if (aura.targets.get(aura.index).getTotalArmorValue() != 0) {
					RenderUtils.drawRect((x + 40.0F), (y + 32), (float) ((x + 40.0F) + 92.0 * armorWidth), (y + 33), Color.cyan.getRGB());
				}


				Spicy.getINSTANCE().fontManager.arrayFont.drawString(target.getNameClear(), x + 40.0F, y + 2.0F, -1);
				Spicy.getINSTANCE().fontManager.arrayFont.drawString("HP: " + targetHealth, x + 40.0F, y + 12.0F, -1);
				float df = Float.parseFloat(myHealth) - Float.parseFloat(targetHealth);
				if(myHealth.equals(targetHealth)) {
					Spicy.getINSTANCE().fontManager.arrayFont.drawString("DF: " + MathUtils.roundToFloat(df), x + 40, y + 22, -1);
				} else if(Float.parseFloat(myHealth) > Float.parseFloat(targetHealth)) {
					Spicy.getINSTANCE().fontManager.arrayFont.drawString("DF: §e" + MathUtils.roundToFloat(df), x + 40, y + 22, -1);
				} else {
					Spicy.getINSTANCE().fontManager.arrayFont.drawString("DF: §c-" + MathUtils.roundToFloat(df), x + 40, y + 22, -1);
				}


				GL11.glPushMatrix();
				getMinecraft().getTextureManager().bindTexture(target.getLocationSkin());
				GL11.glColor4d(255, 255, 255, 255);
				RenderUtils.drawCircleImage((int) x + 5, (int) y + 5, 8f, 8f, 8, 8, 30, 30, 64f, 64f);
				RenderUtils.drawCircleImage((int) x + 5, (int) y + 5, 40f, 8f, 8, 8, 30, 30, 64f, 64f);
				GL11.glPopMatrix();
			}
		}
	});


   public Color getHealthColor(float health, float maxHealth) {
	      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
	      Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
	      float progress = health / maxHealth;
	      return ColorUtils.blendColors(fractions, colors, progress).brighter();
	   }
}