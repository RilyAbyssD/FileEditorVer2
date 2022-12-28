package abyss.fileeditor.editor;

import abyss.fileeditor.FileEditor;
import abyss.fileeditor.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;

public class Editor {

    private Player p;
    private final String ROOT_DIR = FileEditor.getPlugin().getDataFolder().getParent();
    private final int INV_SIZE = 54;
    // インベントリーの中に表示できるファイル、ディレクトリの最大の数
    private final int INV_MAX_SIZE = 44;
    private String dir;
    private File[] fileAndFolder;
    private int page = 1;
    private Inventory inv;
    private boolean isCreateDir = false;
    private boolean isCreateFile = false;
    private boolean isPageChange = false;

    public final String MAKE_DIR = "SELECT IS DIR";
    public final String MAKE_FILE = "SELECT IS FILE";

    public Editor(Player p) {
        this.p = p;
        this.dir = ROOT_DIR;
        FileEditor.editorManager.setEditorMap(p, this);
    }

    public String getFileDir() {
        return ROOT_DIR;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public boolean isCreateDir() {
        return isCreateDir;
    }

    public boolean isCreateFile() {
        return isCreateFile;
    }

    public void setCreateDir(boolean createDir) {
        isCreateDir = createDir;
    }

    public void setCreateFile(boolean createFile) {
        isCreateFile = createFile;
    }

    public boolean isPageChange() {
        return isPageChange;
    }

    public void setPageChange(boolean pageChange) {
        isPageChange = pageChange;
    }

    public boolean backPage() {
        if (page > 1) {
            page--;
            if (load()) {
                p.openInventory(getScreen());
                setPageChange(false);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean nextPage() {

        // 88 > 54 176 > 54  < 44 54 - 176 -122 44  100 88 22 > 44              88 > 54      176 > 54
//        if ((page + 1) * INV_MAX_SIZE > fileAndFolder.length || fileAndFolder.length - (page + 1) * INV_MAX_SIZE < INV_MAX_SIZE) {
        if (page * INV_MAX_SIZE < fileAndFolder.length) {
            page++;
            if (load()) {
                p.openInventory(getScreen());
                setPageChange(false);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean create(String name, String select) {
        File file = new File(dir + "/" + name);

        if (select.equals(MAKE_DIR)) {
            if (file.mkdir()) {
                p.sendMessage(Message.mf("§aフォルダ§f " + name + " §aを作成しました！！"));
                setCreateDir(false);
                return true;
            } else {
                p.sendMessage(Message.mf("§c存在しているフォルダ名です"));
                setCreateDir(false);
                return false;
            }
        } else if (select.equals(MAKE_FILE)) {
            try {
                if (file.createNewFile()) {
                    p.sendMessage(Message.mf("§aファイル§f " + name + " §aを作成しました！！"));
                    setCreateFile(false);
                    return true;
                } else {
                    p.sendMessage(Message.mf("§c存在しているファイル名です"));
                    setCreateFile(false);
                    return false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean load() {

        if (dir == null || p == null || !FileEditor.editorManager.isCreated(p)) return false;

        File file = new File(dir);

        if (!file.isDirectory()) return false;

        setPageChange(true);
        fileAndFolder = file.listFiles();
        // インベントリの設定(ファイル、ディレクトリ以外)
        invSetUp();

        return true;
    }

    // ディレクトリのinvを返す
    public Inventory getScreen() {

        if (fileAndFolder == null) return inv;

        /*
            paper = ファイル
            chest = フォルダ
         */
        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();

        int invNum = 0;

        for (int i = (page - 1) * 45; i < fileAndFolder.length; i++) {
            if (i > page * INV_MAX_SIZE || invNum > INV_MAX_SIZE) break;

            if (!fileAndFolder[i].isDirectory()) {
                continue;
            }
            chestMeta.setDisplayName("§f" + fileAndFolder[i].getName());
            chest.setItemMeta(chestMeta);
            inv.setItem(invNum, chest);
            invNum++;
        }

        for (int i = (page - 1) * 45; i < fileAndFolder.length; i++) {
            if (i > page * INV_MAX_SIZE || invNum > INV_MAX_SIZE) break;

            if (!fileAndFolder[i].isFile()) {
                continue;
            }
            paperMeta.setDisplayName("§f" + fileAndFolder[i].getName());
            paper.setItemMeta(paperMeta);
            inv.setItem(invNum, paper);
            invNum++;
        }
        return inv;
    }

    public void invSetUp() {
        inv = Bukkit.createInventory(null, INV_SIZE, getDir() + "-" + page);

        ItemStack back = new ItemStack(Material.OAK_BUTTON);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§f前のページ");
        back.setItemMeta(backMeta);

        ItemStack next = new ItemStack(Material.OAK_BUTTON);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("§f次のページ");
        next.setItemMeta(nextMeta);

        ItemStack createFile = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta createFileMeta = createFile.getItemMeta();
        createFileMeta.setDisplayName("§fファイルを作成");
        createFile.setItemMeta(createFileMeta);

        ItemStack createDir = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta createDirMeta = createDir.getItemMeta();
        createDirMeta.setDisplayName("§fフォルダを作成");
        createDir.setItemMeta(createDirMeta);

        inv.setItem(45, createDir);
        inv.setItem(46, createFile);
        inv.setItem(52, back);
        inv.setItem(53, next);
    }

}
