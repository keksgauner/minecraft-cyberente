package de.datenente.cyberente.special.VehicleTypes;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public abstract class CustomVehicle {
    public abstract String getName();
    public abstract int getCustomModelData();

    public void applyTo(ArmorStand stand) {
        ItemStack helmet = new ItemStack(Material.IRON_HORSE_ARMOR);
        helmet.editMeta(meta -> meta.setCustomModelData(getCustomModelData()));
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setBasePlate(false);
        stand.setCustomName(getName());
        stand.setCustomNameVisible(false);
        stand.getEquipment().setHelmet(helmet);
    }
}
