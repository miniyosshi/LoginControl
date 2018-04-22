package com.github.miniyosshi.logincontrol;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginControl extends JavaPlugin implements Listener{
	

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Plugin LoginControl has been enabled.");
		
		//許可場所（外部から読み込み）
		try {
			Properties properties = new Properties();
			String strpass = "point.properties";
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
	
	public Location  q = new Location(this.getServer().getWorld("World"),128.0,79.0,115.0);
	
	//ログイン時
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.sendMessage("こんにちは、"+ p.getPlayer().getName() + "さん。今の許可場所は"+ q.toString() 
		+"です。リストは"+ Bukkit.getWorlds().toString() +"問題は"+ Bukkit.getWorlds().get(0));
		
		Location loc = p.getLocation();
		if (loc.getX() != q.getX() || loc.getY() != q.getY() || loc.getZ() != q.getZ() ) {
			p.teleport(q);
			
			p.sendMessage("あなたは"+loc.toString()+"にいたので"+q.toString()+"に移動しました");
		}
		else
			p.sendMessage("そのままの場所です");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sethereasapermittedpoint")){
			Player player = null;
			if(sender instanceof Player) {
				sender.sendMessage("ここを許可場所に設定しなおします");
				player = (Player) sender;
				q = player.getLocation();
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
