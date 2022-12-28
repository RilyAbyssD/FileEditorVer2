package abyss.fileeditor.editor;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class EditorManager {

    // エディタを表示しているプレイヤー
    private Map<Player, Editor> editorMap = new HashMap<>();

    public Editor getEditorMap(Player p) {
        return editorMap.get(p);
    }

    public void setEditorMap(Player p, Editor editor) {
        editorMap.put(p, editor);
    }

    // 全てのプレイヤーを削除する
    public void clearEditorMap() {
        editorMap.clear();
    }

    // 特定のプレイヤーを削除する
    public void removeEditorMap(Player p) {
        editorMap.remove(p);
    }

    public boolean isCreated(Player p) {
        return editorMap.containsKey(p);
    }

    // ファイル フォルダのどちらかがTrueの場合Trueを返す
    public boolean isCreateFileAndFolder(Player p) {
        if (editorMap.get(p).isCreateDir() || editorMap.get(p).isCreateFile()) {
            return true;
        }
        return false;
    }

    public boolean isPageChange(Player p) {
        return editorMap.get(p).isPageChange();
//        if (editorMap.get(p).isPageChange()) {
//            return true;
//        }
//        return false;
    }

}
