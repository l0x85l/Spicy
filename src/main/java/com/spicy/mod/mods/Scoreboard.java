package com.spicy.mod.mods;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.spicy.Spicy;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class Scoreboard extends Mod {

    public Scoreboard() {
        super("Scoreboard", "Turn on the scoreboard", 0, Category.RENDER);
    }


    public void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
        if(!isState()) return;
        net.minecraft.scoreboard.Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = Lists.newArrayList(collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList()));
        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int i = (int) Spicy.getINSTANCE().fontManager.arrayFont.getWidth(objective.getDisplayName());

        String s;
        for(Iterator i$ = collection.iterator(); i$.hasNext(); i = (int) Math.max(i, Spicy.getINSTANCE().fontManager.arrayFont.getWidth(s))) {
            Score score = (Score)i$.next();
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
        }

        HUD hud = (HUD) Spicy.getINSTANCE().modManager.getMod(HUD.class);
        int i1 = collection.size() - getMinecraft().fontRendererObj.FONT_HEIGHT + (hud.y / 2) + 50;
        int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;
        Iterator i$ = collection.iterator();

        while(i$.hasNext()) {
            Score score1 = (Score)i$.next();
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            int k = j1 - j * getMinecraft().fontRendererObj.FONT_HEIGHT;
            int l = scaledRes.getScaledWidth() - k1 + 2;
            Gui.drawRect(l1 - 2, k, l, k + getMinecraft().fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            Spicy.getINSTANCE().fontManager.arrayFont.drawString(s1, l1, k, -1);
            String s3;
            if (j == collection.size()) {
                s3 = objective.getDisplayName();
                Gui.drawRect(l1 - 2, k - getMinecraft().fontRendererObj.FONT_HEIGHT - 1, l, k - 1, Integer.MIN_VALUE);
                Gui.drawRect(l1 - 2, k - 1, l, k, Integer.MIN_VALUE);
                Spicy.getINSTANCE().fontManager.defaultFont.drawString(s3, l1 + i / 2 - Spicy.getINSTANCE().fontManager.defaultFont.getWidth(s3) / 2, k - getMinecraft().fontRendererObj.FONT_HEIGHT, -1);
            }
        }

    }
}
