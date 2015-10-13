package client.entity;

import client.graphics.Sprite;

public class Item {

	private final String itemName;
	private Sprite sprite;
	
	public Item(String itemName){
		this.itemName = itemName;
		switch(itemName){
		case "SWORD":
			sprite = Sprite.sword;
			break;
		}
	}
	
	public Sprite getSprite(){
		return this.sprite;
	}
	
	public String getItemName(){
		return this.itemName;
	}
	
}
