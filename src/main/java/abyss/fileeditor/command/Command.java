package abyss.fileeditor.command;

import abyss.fileeditor.editor.Editor;
import abyss.fileeditor.util.Message;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.mf("このコマンドはプレイヤーのみ実行可能です"));
        }

        Player p = (Player) sender;

        Editor e = new Editor(p);

        if (e.load()) {
            p.openInventory(e.getScreen());
            e.setPageChange(false);
        } else {
            p.sendMessage(Message.mf("§cエラーが発生しました"));
        }

        return true;
    }
}
