package abyss.fileeditor.editor;

import abyss.fileeditor.FileEditor;
import abyss.fileeditor.util.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class EditorListener implements Listener {

    @EventHandler
    public void clickInventory(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (FileEditor.editorManager.isCreated(p)) {
            Editor editor = FileEditor.editorManager.getEditorMap(p);

            ItemStack item = e.getCurrentItem();

            if (item != null && item.getItemMeta() != null) {

                if (item.getItemMeta().getDisplayName().equals("§f前のページ")) {
                    if (!editor.backPage()) {
                        p.sendMessage(Message.mf("§cこれ以上前に戻ることはできません"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals("§f次のページ")) {
                    if (!editor.nextPage()) {
                        p.sendMessage(Message.mf("§cこれ以上次に進むことはできません"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals("§fファイルを作成")) {
                    editor.setCreateFile(true);
                    p.closeInventory();
                    p.sendMessage(Message.mf("§a作成するファイル名を入力してください"));

                } else if (item.getItemMeta().getDisplayName().equals("§fフォルダを作成")) {
                    editor.setCreateDir(true);
                    p.closeInventory();
                    p.sendMessage(Message.mf("§a作成するフォルダ名を入力してください"));
                }

            }

            if (item.getType() == Material.CHEST) {
                String dirName = item.getItemMeta().getDisplayName();

                editor.setDir(editor.getDir() + "/" + dirName);

                p.sendMessage(Message.mf(editor.getDir()));

                if (editor.load()) {
                    p.openInventory(editor.getScreen());
                    editor.setPageChange(false);
                }
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {

        Player p = (Player) e.getPlayer();

        // インベントリを閉じた時、プレイヤーがエディタを開いていた場合は、マップから削除
        if (FileEditor.editorManager.isCreated(p) && !(FileEditor.editorManager.isCreateFileAndFolder(p)) && !(FileEditor.editorManager.isPageChange(p))) {
            FileEditor.editorManager.removeEditorMap(p);
        }
    }

}
