package com.github.miniyosshi.logincontrol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
	int[][] area = new int[40][6];
	
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
					area[i][j] = Integer.parseInt(data[j]);
				}
				i++;
			}
			System.out.println(Arrays.deepToString(area));
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
		
		int i = 0;
		
		
		while (i<area.length) {
			
			if (hantei((int)loc.getX(), area[i], 0)&&hantei((int)loc.getY(), area[i], 1)&&hantei((int)loc.getZ(), area[i], 2)) {
				//もし入ってたらそのまま
				p.sendMessage("そのままの場所です");
				break;
			}
			else
				i++;
		}
		if(i == area.length) {
			//外だったらどっかに飛ばす
			//World world = Bukkit.getServer().getWorld("world");
			//Location finloc = new Location(world,128,79,156);
			//p.teleport(finloc);
			p.sendMessage("あなたは"+loc.toString()+"にいたので"+"finloc.toString()"+"に移動しました");
		}
		
	}
	
	//ログアウト時
	
	public boolean horrorOn = false;
	
	@EventHandler
	public void onPlayerLogout (PlayerQuitEvent e) {
		
		if (horrorOn == true) {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM,1,(byte)3);
			SkullMeta sm = (SkullMeta) skull.getItemMeta();
			sm.setOwningPlayer(e.getPlayer());
			skull.setItemMeta(sm);
			
			Location loc =e.getPlayer().getLocation();
			
			Skeleton s = (Skeleton) e.getPlayer().getWorld().spawnEntity(loc, EntityType.SKELETON);
			s.setCustomNameVisible(true);
			s.setCustomName(e.getPlayer().getName() + "の哀れな姿");
			s.getEquipment().setHelmet(skull);
			s.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD,1));
		}
		
		
		
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setpoint")){
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
		
		if(cmd.getName().equalsIgnoreCase("horror")) {
			horrorOn = !horrorOn;
			sender.sendMessage("horrorOnOff切り替えました");
			return true;
		}
		
		else 
			return false;
	}

}
