package cz.larkyy.lchangelogs.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cz.larkyy.lchangelogs.LChangeLogs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public void sendMsg(Player p, String msg) {
        p.sendMessage(format(msg));
    }

    public void sendConsoleMsg(String msg) {
        getMain().getServer().getConsoleSender().sendMessage(format(msg));
    }

    public void sendTitleMsg(Player p, String title, String subTitle, int fadein, int stay, int fadeout) {
        p.sendTitle(format(title), format(subTitle), fadein, stay, fadeout);
    }

    public static String format(String msg) {
        Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

        if(msg!=null) {
            if (Bukkit.getVersion().contains("1.16")) {
                Matcher match = pattern.matcher(msg);
                while (match.find()) {
                    String color = msg.substring(match.start(), match.end());
                    msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                    match = pattern.matcher(msg);
                }
            }
            return ChatColor.translateAlternateColorCodes('&', msg);
        }
        return msg;
    }

    public List<String> formatList(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String str : list) {
            result.add(format(str));
        }
        return result;
    }

    public ItemStack mkItem(Material material, String displayName, String localizedName, List<String> lore, String texture) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(format(displayName));
        im.setLocalizedName(localizedName);
        if (lore != null)
            im.setLore(formatList(lore));
        is.setItemMeta(im);

        if (texture != null && material.equals(Material.PLAYER_HEAD))
            setSkullItemSkin(is, texture);

        return is;
    }

    public void setSkullItemSkin(ItemStack is, String texture) {

        ItemMeta meta = is.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x) {
            x.printStackTrace();
        }
        is.setItemMeta(meta);
    }

    public static Location blockLocation(Location loc) {
        loc.setX(Math.floor(loc.getX()));
        loc.setY(Math.floor(loc.getY()));
        loc.setZ(Math.floor(loc.getZ()));
        return loc;
    }

    private static LChangeLogs getMain() {
        return LChangeLogs.getMain();
    }
}
