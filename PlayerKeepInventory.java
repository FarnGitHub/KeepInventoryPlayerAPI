package net.minecraft.src;

public class PlayerKeepInventory extends PlayerBase {

	public NBTTagList savedInventoryNBT = null;

	public PlayerKeepInventory(EntityPlayerSP player) {
		super(player);
		player.getMc().playerController = new InventoryKeepPlayerControllerSP(player.getMc(), this);
	}

	@Override
	public boolean onDeath(Entity killer) {
		if(player.scoreValue >= 0 && killer != null) {
			killer.addToPlayerScore(player, player.scoreValue);
		}

		if(killer != null) {
			killer.onKillEntity(player);
		}

		player.unused_flag = true;
		if(!player.getMc().theWorld.multiplayerWorld) {
			player.dropFewItems();
		}

		player.getMc().theWorld.func_9425_a(player, (byte)3);
		player.setSize(0.2F, 0.2F);
		player.setPosition(player.posX, player.posY, player.posZ);
		player.motionY = (double)0.1F;

		savedInventoryNBT = player.inventory.writeToNBT(new NBTTagList());
		if(killer != null) {
			player.motionX = (double)(-MathHelper.cos((player.attackedAtYaw + player.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
			player.motionZ = (double)(-MathHelper.sin((player.attackedAtYaw + player.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
		} else {
			player.motionX = player.motionZ = 0.0D;
		}

		player.yOffset = 0.1F;
		player.addStat(StatList.deathsStat, 1);
		return true; // vanilla death logic continues
	}

	@Override
	public boolean writeEntityToNBT(NBTTagCompound tag) {
		// Optionally store our saved inventory inside player NBT (for world save)
		if (savedInventoryNBT != null) {
			tag.setTag("KeepInventory", savedInventoryNBT);
		}
		return false;
	}

	@Override
	public boolean readEntityFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("KeepInventory")) {
			savedInventoryNBT = (NBTTagList) tag.getTagList("KeepInventory");
		}
		return false;
	}
}
