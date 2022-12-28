package abyss.fileeditor;

import abyss.fileeditor.command.Command;
import abyss.fileeditor.editor.ChatListener;
import abyss.fileeditor.editor.EditorListener;
import abyss.fileeditor.editor.EditorManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FileEditor extends JavaPlugin {

    private static FileEditor plugin;

    public static EditorManager editorManager = new EditorManager();

    @Override
    public void onEnable() {

        plugin = this;

        getCommand("fe").setExecutor(new Command());

        Bukkit.getPluginManager().registerEvents(new EditorListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

        getServer().getLogger().info("プラグインが有効になりました");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static FileEditor getPlugin() {
        return plugin;
    }

}
