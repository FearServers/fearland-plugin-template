package br.fearland.example;

import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Bootstrap order: configs -> Services.require -> migrate -> domain -> Services.register -> listeners -> commands.
        getLogger().info("Example ready");
    }
}
