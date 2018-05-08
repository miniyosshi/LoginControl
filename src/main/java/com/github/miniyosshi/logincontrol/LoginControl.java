package com.github.miniyosshi.logincontrol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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
	
	
	//許可場所
	int[][] a = new int[30][6];
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Plugin LoginControl has been enabled.");
		
		//許可場所（外部から読み込み）
		/*
		Properties properties = new Properties();
		String strpass = "point.properties";
		try {
			
			InputStream istream = new FileInputStream(strpass);
			properties.load(istream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		String line;
		
		try {
			File f = new File("permitted_area.csv");
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				
				for (int j=0; j<6; j++) {
					a[i][j] = Integer.parseInt(data[j]);
				}
				i++;
			}
			System.out.println(Arrays.deepToString(a));
			br.close();
		}catch(IOException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin LoginControl has been disabled.");
	}
	
	
	//座標判定メソッド
	
	public static boolean hantei(int p, int[] sixset, int x) {
		if (sixset[x] <= p && p <= sixset[x+3])
			return true;
		else
			return false;
	}
	
	
	//ログイン時
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.sendMessage("こんにちは、"+ p.getPlayer().getName() + "さん。"+"リストは"+ Bukkit.getWorlds().toString());
		
		Location loc = p.getLocation();
		
		int i =0;
		while (i<a.length) {
			
			if (hantei((int)p.getLocation().getX(), a[i], 0)&&hantei((int)p.getLocation().getY(), a[i], 1)&&hantei((int)p.getLocation().getZ(), a[i], 2)) {
				//もし入ってたらそのまま
				p.sendMessage("そのままの場所です");
				break;
			}
			else
				i++;
		}
		if(i ==a.length-1) {
			//外だったらどっかに飛ばす
			p.sendMessage("あなたは"+loc.toString()+"にいたので"+"に移動しました(仮)");
			p.
			
		}
		
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
		s.setCustomName(e.getPlayer().getName() + "成れの果て");
		s.getEquipment().setHelmet(skull);
		s.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD,1));
		
		
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setapermittedpoint")){
			Player player = null;
			if(sender instanceof Player) {
				sender.sendMessage("ここを許可場所に追加します");
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
