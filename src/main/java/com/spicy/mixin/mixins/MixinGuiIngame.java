package com.spicy.mixin.mixins;

import com.spicy.Spicy;
import com.spicy.events.Render2DEvent;
import com.spicy.mod.mods.Scoreboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {

    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        Spicy.getINSTANCE().eventSystem.fire(new Render2DEvent(sr));
        net.minecraft.scoreboard.Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(Minecraft.getMinecraft().thePlayer.getName());
        if (scoreplayerteam != null) {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();
            if (i1 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        Scoreboard sb = (Scoreboard) Spicy.getINSTANCE().modManager.getMod(Scoreboard.class);

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            sb.renderScoreboard(scoreobjective1, sr);
        }

    }



    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    public void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
            ci.cancel();
    }
}