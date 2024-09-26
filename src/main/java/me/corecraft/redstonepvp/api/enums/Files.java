package me.corecraft.redstonepvp.api.enums;

import com.ryderbelserion.vital.paper.api.files.FileManager;
import me.corecraft.redstonepvp.RedstonePvP;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public enum Files {

    player_drops("player-drops.yml");

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final FileManager fileManager = this.plugin.getFileManager();

    private final String fileName;
    private final String filePath;

    private final File file;

    Files(final String fileName, final String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;

        final File dataFolder = this.plugin.getDataFolder();

        this.file = this.filePath.isEmpty() ? new File(new File(dataFolder, this.filePath), this.fileName) : new File(dataFolder, filePath);
    }

    Files(final String fileName) {
        this(fileName, "");
    }

    public final YamlConfiguration getConfig() {
        return this.fileManager.getFile(getFileName()).getConfiguration();
    }

    public void load() {
        this.fileManager.addFile(this.file);
    }

    public void save() {
        this.fileManager.saveFile(getFileName());
    }

    public final String getFilePath() {
        return this.filePath;
    }

    public final String getFileName() {
        return this.fileName;
    }

    public final File getFile() {
        return this.file;
    }
}