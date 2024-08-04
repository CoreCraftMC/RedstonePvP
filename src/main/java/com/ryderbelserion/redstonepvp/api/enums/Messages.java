package com.ryderbelserion.redstonepvp.api.enums;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.managers.config.types.Locale;
import com.ryderbelserion.vital.core.util.StringUtil;
import com.ryderbelserion.vital.paper.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Messages {

    no_permission(Locale.no_permission),
    unknown_command(Locale.unknown_command),
    correct_usage(Locale.correct_usage),
    reloaded_plugin(Locale.reloaded_plugin),
    no_item_in_hand(Locale.no_item_in_hand),
    must_be_console_sender(Locale.must_be_console_sender),
    not_a_player(Locale.not_a_player),
    player_not_found(Locale.player_not_found),
    not_a_block(Locale.not_a_block),
    item_frame_bypass(Locale.item_frame_bypass),
    anvil_repair_not_enough(Locale.anvil_repair_not_enough),
    anvil_repair_no_damage(Locale.anvil_repair_no_damage),
    anvil_repair_not_valid(Locale.anvil_repair_not_valid),

    menu_not_found(Locale.menu_not_found),

    beacon_drop_party_added(Locale.beacon_drop_party_added),
    beacon_drop_party_removed(Locale.beacon_drop_party_removed),
    beacon_drop_party_time_updated(Locale.beacon_drop_party_time_updated),
    beacon_drop_party_exists(Locale.beacon_drop_party_exists),
    beacon_drop_party_doesnt_exist(Locale.beacon_drop_party_doesnt_exist),

    beacon_drop_party_started(Locale.beacon_drop_party_started),
    beacon_drop_party_stopped(Locale.beacon_drop_party_stopped),
    beacon_drop_party_not_enough_players(Locale.beacon_drop_party_not_enough_players),
    beacon_drop_party_countdown(Locale.beacon_drop_party_countdown),

    beacon_drop_doesnt_exist(Locale.beacon_drop_doesnt_exist),
    beacon_drop_exists(Locale.beacon_drop_exists),
    beacon_drop_added(Locale.beacon_drop_added),
    beacon_drop_removed(Locale.beacon_drop_removed),
    beacon_drop_set(Locale.beacon_drop_set);

    private Property<String> property;

    private Property<List<String>> properties;
    private boolean isList = false;

    Messages(@NotNull final Property<String> property) {
        this.property = property;
    }

    Messages(@NotNull final Property<List<String>> properties, final boolean isList) {
        this.properties = properties;
        this.isList = isList;
    }

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private final SettingsManager config = ConfigManager.getConfig();

    private final SettingsManager messages = ConfigManager.getMessages();

    private boolean isList() {
        return this.isList;
    }

    public String getString() {
        return this.messages.getProperty(this.property);
    }

    public List<String> getList() {
        return this.messages.getProperty(this.properties);
    }

    public String getMessage(@NotNull final CommandSender sender) {
        return getMessage(sender, new HashMap<>());
    }

    public String getMessage(@NotNull final CommandSender sender, @NotNull final String placeholder, @NotNull final String replacement) {
        Map<String, String> placeholders = new HashMap<>() {{
            put(placeholder, replacement);
        }};

        return getMessage(sender, placeholders);
    }

    public String getMessage(@NotNull final CommandSender sender, @NotNull final Map<String, String> placeholders) {
        return parse(sender, placeholders).replaceAll("\\{prefix}", this.config.getProperty(Config.command_prefix));
    }

    public void sendMessage(final CommandSender sender, final String placeholder, final String replacement) {
        sender.sendRichMessage(getMessage(sender, placeholder, replacement));
    }

    public void sendMessage(final CommandSender sender, final Map<String, String> placeholders) {
        sender.sendRichMessage(getMessage(sender, placeholders));
    }

    public void sendMessage(final CommandSender sender) {
        sender.sendRichMessage(getMessage(sender));
    }

    private @NotNull String parse(@NotNull final CommandSender sender, @NotNull final Map<String, String> placeholders) {
        String message;

        if (isList()) {
            message = StringUtils.chomp(StringUtil.convertList(getList()));
        } else {
            message = getString();
        }

        if (sender instanceof Player player) {
            if (Support.placeholder_api.isEnabled()) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }
        }

        if (!placeholders.isEmpty()) {
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                if (placeholder != null) {
                    final String key = placeholder.getKey();
                    final String value = placeholder.getValue();

                    if (key != null && value != null) {
                        message = message.replace(key, value).replace(key.toLowerCase(), value);
                    }
                }
            }
        }

        return message;
    }

    public void broadcast() {
        broadcast(null);
    }

    public void broadcast(final Map<String, String> placeholder) {
        sendMessage(this.plugin.getServer().getConsoleSender(), placeholder);

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (placeholder == null) sendMessage(player); else sendMessage(player, placeholder);
        }
    }
}