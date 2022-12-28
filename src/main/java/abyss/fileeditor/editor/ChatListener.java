package abyss.fileeditor.editor;

import abyss.fileeditor.FileEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void sendChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        if (FileEditor.editorManager.isCreated(p) && FileEditor.editorManager.isCreateFileAndFolder(p)) {

            String name = e.getMessage();

            Editor editor = FileEditor.editorManager.getEditorMap(p);

            if (editor.isCreateFile()) {
                editor.create(name, editor.MAKE_FILE);
            } else if(editor.isCreateDir()) {
                editor.create(name, editor.MAKE_DIR);
            }
            e.setCancelled(true);
        }

    }

}
