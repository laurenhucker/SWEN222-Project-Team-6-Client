package client.entity.mob.monsters;

import client.entity.mob.Monster;
import client.graphics.Sprite;

public class GhostMonster extends Monster {

	public GhostMonster(int x, int y, Sprite sprite, int fireRate, int maxHealth, boolean moves, boolean shoots) {
		super(x, y, sprite);
		this.fireRate = fireRate;
		this.MAX_HEALTH = maxHealth;
		this.health = maxHealth;
		this.moves = moves;
		this.shoots = shoots;
	}

}
