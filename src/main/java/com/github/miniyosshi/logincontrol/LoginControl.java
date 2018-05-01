package com.github.miniyosshi.logincontrol;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginControl extends JavaPlugin implements Listener{
	

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Plugin LoginControl has been enabled.");
		
		//許可場所（外部から読み込み）
		Properties properties = new Properties();
		String strpass = "point.properties";
		try {
			
			InputStream istream = new FileInputStream(strpass);
			properties.load(istream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin LoginControl has been disabled.");
	}
	
	
	//ログイン時
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.sendMessage("こんにちは、"+ p.getPlayer().getName() + "さん。"+"リストは"+ Bukkit.getWorlds().toString());
		
		Location loc = p.getLocation();
		if (loc.getX() != 0  || loc.getY() != 0 || loc.getZ() != 0 ) {
			
			p.sendMessage("あなたは"+loc.toString()+"にいたので"+"に移動しました(仮)");
		}
		else
			p.sendMessage("そのままの場所です");
	}
	
	//ログアウト時
	@EventHandler
	public void onPlayerLogout (PlayerQuitEvent e) {
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM,1,(byte)3);
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		sm.setOwningPlayer(e.getPlayer());
		skull.setItemMeta(sm);
		
		Location loc =e.getPlayer().getLocation();
		
		Skeleton s = (Skeleton) e.getPlayer().getWorld().spawnEntity(loc, EntityType.SKELETON);
		s.setCustomNameVisible(true);
		s.setCustomName("何かの成れの果て");
		s.getEquipment().setHelmet(skull);
		
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setapermittedpoint")){
			Player player = null;
			if(sender instanceof Player) {
				sender.sendMessage("ここを許可場所に設定しなおします");
				player = (Player) sender;
				
				return true;
			}
			else {
				sender.sendMessage("playerからしか受け付けません");
				return false;
			}
		}
		else 
			return false;
	}

}
