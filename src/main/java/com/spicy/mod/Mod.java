package com.spicy.mod;

import com.spicy.Spicy;
import com.spicy.setting.$;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mod {
    @Getter @Setter public String alias;
    @Getter public String suffix;
    @Getter @Setter public String desc;

    @Getter @Setter public int key;

    @Getter @Setter public Category cat;

    @Getter public boolean state;
    @Getter @Setter public boolean visible;

    @Getter protected List<$<?>> settings = new ArrayList<>();
    @Getter protected Minecraft minecraft = Minecraft.getMinecraft();
    public int color = -1 * new Random().nextInt();

    public Mod(String _alias, String _desc, int _key, Category _cat) {
        this.alias = _alias;
        this.desc = _desc;
        this.key = _key;
        this.cat = _cat;
        this.suffix = "";
        this.visible = true;
    }


    public void setState(boolean _state) {
        if(_state) {
            onEnable();
            state = true;
        } else {
            onDisable();
            state = false;
        }
    }

    public void setSuffix(String _suffix) {
        this.suffix = _suffix;
    }

    public void onEnable() {
        Spicy.getINSTANCE().eventSystem.subscribeAll(this);

    }

    public void onDisable() {
        Spicy.getINSTANCE().eventSystem.unsubscribeAll(this);
    }

    public void toggle() {
        setState(!state);
    }

}
