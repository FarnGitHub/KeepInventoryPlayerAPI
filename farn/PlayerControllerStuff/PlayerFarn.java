package farn.PlayerControllerStuff;

import net.minecraft.src.*;

public class PlayerFarn extends PlayerBase {

	public NBTTagList savedInventoryNBT = null;

	public PlayerFarn(EntityPlayerSP plrE) {
		super(plrE);
		if(!(player instanceof EntityClientPlayerMP)) {
			player.getMc().playerController = new PlayerControllerProxy(player.getMc(), this);
		}
	}

	@Override
	public boolean onDeath(Entity killer) {
		if(!(player instanceof EntityClientPlayerMP)) {
			if(mod_PlayerControllerStaff.getPlayerScoreValue(player) >= 0 && killer != null) {
				killer.addToPlayerScore(player, mod_PlayerControllerStaff.getPlayerScoreValue(player));
			}

			if(killer != null) {
				killer.onKillEntity(player);
			}

			mod_PlayerControllerStaff.setUnusedFlag(player);
			if(!player.getMc().theWorld.multiplayerWorld) {
				mod_PlayerControllerStaff.dropFewItems(player);
			}

			player.getMc().theWorld.func_9425_a(player, (byte)3);
			mod_PlayerControllerStaff.setSize(player);
			player.setPosition(player.posX, player.posY, player.posZ);
			player.motionY = (double)0.1F;

			if(ConfigFarn.instance.keepInventory()) {
				savedInventoryNBT = player.inventory.writeToNBT(new NBTTagList());
			} else {
				player.inventory.dropAllItems();
			}
			if(killer != null) {
				player.motionX = (double)(-MathHelper.cos((player.attackedAtYaw + player.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
				player.motionZ = (double)(-MathHelper.sin((player.attackedAtYaw + player.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
			} else {
				player.motionX = player.motionZ = 0.0D;
			}

			player.yOffset = 0.1F;
			player.addStat(StatList.deathsStat, 1);
		}
		return !(player instanceof EntityClientPlayerMP); // vanilla death logic continues
	}

	@Override
	public boolean writeEntityToNBT(NBTTagCompound tag) {
		// Optionally store our saved inventory inside player NBT (for world save)
		if(!(player instanceof EntityClientPlayerMP)) {
			if (savedInventoryNBT != null) {
				tag.setTag("KeepInventory", savedInventoryNBT);
			}
		}
		return false;
	}

	@Override
	public boolean readEntityFromNBT(NBTTagCompound tag) {
		if(!(player instanceof EntityClientPlayerMP)) {
			if (tag.hasKey("KeepInventory")) {
				savedInventoryNBT = (NBTTagList) tag.getTagList("KeepInventory");
			}
		}
		return false;
	}
}
