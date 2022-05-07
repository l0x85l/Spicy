package com.spicy.mixin;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tweaker implements ITweaker {

    private final ArrayList<String> args = new ArrayList<>();

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
        this.args.addAll(args);
        addArg("gameDir", gameDir);
        addArg("assetsDir", assetsDir);
        addArg("version", profile);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.init();
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
        Mixins.addConfiguration("mixins.spicy.json");

        if (environment.getObfuscationContext() == null) {
            environment.setObfuscationContext("notch"); // Switch's to notch mappings
        }
        environment.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[]{});
    }

    private void addArg(String label, Object value) {
        args.add("--" + label);
        args.add(value instanceof String ? (String) value : value instanceof File ? ((File) value).getAbsolutePath() : ".");
    }
}