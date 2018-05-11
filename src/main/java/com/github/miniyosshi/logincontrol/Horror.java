package com.github.miniyosshi.logincontrol;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Horror {
	public static void reviver(Player p) {
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM,1,(byte)3);
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		sm.setOwningPlayer(p);
		skull.setItemMeta(sm);
		
		Location loc =p.getLocation();
		
		Skeleton s = (Skeleton) p.getWorld().spawnEntity(loc, EntityType.SKELETON);
		s.setCustomNameVisible(true);
		s.setCustomName(p.getName() + "の哀れな姿");
		s.getEquipment().setHelmet(skull);
		s.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD,1));
	}
	

}
