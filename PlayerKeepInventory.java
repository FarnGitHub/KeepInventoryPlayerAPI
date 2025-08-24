package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerKeepInventory extends PlayerBase {

	private NBTTagList savedInventoryNBT = null;

	public PlayerKeepInventory(EntityPlayerSP player) {
		super(player);
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
	public boolean respawn() {
		//copy from Minecraft.class
		Minecraft mc = ModLoader.getMinecraftInstance();
		if(!mc.isMultiplayerWorld() && !mc.theWorld.worldProvider.canRespawnHere()) {
			mc.usePortal();
		}

		ChunkCoordinates chunkCoordinates3 = null;
		ChunkCoordinates chunkCoordinates4 = null;
		boolean z5 = true;
		if(mc.thePlayer != null) {
			chunkCoordinates3 = mc.thePlayer.getPlayerSpawnCoordinate();
			if(chunkCoordinates3 != null) {
				chunkCoordinates4 = EntityPlayer.func_25060_a(mc.theWorld, chunkCoordinates3);
				if(chunkCoordinates4 == null) {
					mc.thePlayer.addChatMessage("tile.bed.notValid");
				}
			}
		}

		if(chunkCoordinates4 == null) {
			chunkCoordinates4 = mc.theWorld.getSpawnPoint();
			z5 = false;
		}

		IChunkProvider iChunkProvider6 = mc.theWorld.getIChunkProvider();
		if(iChunkProvider6 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate7 = (ChunkProviderLoadOrGenerate)iChunkProvider6;
			chunkProviderLoadOrGenerate7.setCurrentChunkOver(chunkCoordinates4.x >> 4, chunkCoordinates4.z >> 4);
		}

		mc.theWorld.setSpawnLocation();
		mc.theWorld.updateEntityList();
		int i8 = 0;
		if(player != null) {
			i8 = player.entityId;
			player.worldObj.setEntityDead(mc.thePlayer);
		}

		mc.renderViewEntity = null;
		mc.thePlayer = (EntityPlayerSP)this.createPlayerCustom(mc.theWorld);
		mc.thePlayer.dimension = 0;
		mc.renderViewEntity = mc.thePlayer;
		mc.thePlayer.preparePlayerToSpawn();
		if(z5) {
			mc.thePlayer.setPlayerSpawnCoordinate(chunkCoordinates3);
			mc.thePlayer.setLocationAndAngles((double)((float)chunkCoordinates4.x + 0.5F), (double)((float)chunkCoordinates4.y + 0.1F), (double)((float)chunkCoordinates4.z + 0.5F), 0.0F, 0.0F);
		}

		mc.playerController.flipPlayer(mc.thePlayer);
		mc.theWorld.spawnPlayerWithLoadedChunks(mc.thePlayer);
		mc.thePlayer.movementInput = new MovementInputFromOptions(mc.gameSettings);
		mc.thePlayer.entityId = i8;
		mc.thePlayer.func_6420_o();
		mc.playerController.func_6473_b(mc.thePlayer);
		//how to invoke func_6255_d because it is private
		invokeRespawnScreen();
		if(mc.currentScreen instanceof GuiGameOver) {
			mc.displayGuiScreen((GuiScreen)null);
		}
		return true; // vanilla respawn continues
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

	public EntityPlayer createPlayerCustom(World world1) {
		EntityPlayerSP newPlayer = new EntityPlayerSP(ModLoader.getMinecraftInstance(), world1, ModLoader.getMinecraftInstance().session, world1.worldProvider.worldType);

		if (ModLoader.getMinecraftInstance().thePlayer != null) {
			// carry over saved NBT
			if (savedInventoryNBT != null) {
				newPlayer.inventory.readFromNBT(savedInventoryNBT);
				savedInventoryNBT = null; // clear so it won’t dupe
			}
		}

		return newPlayer;
	}

	public void invokeRespawnScreen() {
		try {
			Minecraft mc = ModLoader.getMinecraftInstance();

			// Find the private method func_6255_d(String)
			java.lang.reflect.Method respawnMethod = Minecraft.class.getDeclaredMethod(getRespawnScreenMethod(), String.class);
			respawnMethod.setAccessible(true);

			// Call it with "Respawning"
			respawnMethod.invoke(mc, "Respawning");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getRespawnScreenMethod() {
		try {
			boolean playerSP = Class.forName("net.minecraft.src.EntityPlayerSP") != null;
			return playerSP ? "func_6255_d" : "d";
		} catch (Exception e) {
			return "d";
		}
	}
}
