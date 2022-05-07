package com.spicy.mod.mods;

import com.spicy.Spicy;
import com.spicy.events.SprintEvent;
import com.spicy.events.UpdateEvent;
import com.spicy.mod.Category;
import com.spicy.mod.Mod;
import com.spicy.setting.$;
import com.spicy.setting.interfaces.Setting;
import pisi.unitedmeows.eventapi.event.listener.Listener;

public class Sprint extends Mod {

    @Setting(label = "multi-directional")
    public $<Boolean> multi_dir = new $<>(true);
    @Setting(label = "auto-sprint")
    public $<Boolean> auto_sprint = new $<>(true);
    @Setting(label = "legit")
    public $<Boolean> legit = new $<>(false);




    public Sprint() {
        super("Sprint", "Auto sprint", 0, Category.MOVEMENT);
    }

    public Listener<UpdateEvent> updateListener = new Listener<UpdateEvent>(event -> {
        getMinecraft().thePlayer.setSprinting(true);
    }).filter(f -> getMinecraft().thePlayer != null && canSprint());

    public Listener<SprintEvent> sprintListener = new Listener<SprintEvent>(event -> {
        event.setSprinting(true);
    }).filter(f -> canSprint());

    private boolean canSprint() {
        return auto_sprint.val() && (!getMinecraft().thePlayer.isCollidedHorizontally && !getMinecraft().thePlayer.isSneaking() && (!legit.val() || Spicy.getINSTANCE().modManager.getMod(NoSlowdown.class).isState() || (legit.val() && getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 5 && !getMinecraft().thePlayer.isUsingItem()))) && (multi_dir.val() ? (getMinecraft().thePlayer.movementInput.moveForward != 0.0f || getMinecraft().thePlayer.movementInput.moveStrafe != 0.0f) : (getMinecraft().thePlayer.movementInput.moveForward > 0.0f));
    }
}