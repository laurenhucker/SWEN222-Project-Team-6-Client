package client.entity;

import client.graphics.Sprite;

public class Item {

	private final String itemName;
	private Sprite sprite;
	
	public Item(String itemName){
		this.itemName = itemName;
		switch(itemName){
		case "SWORD_WOOD": sprite = Sprite.sword_wood; break;
		case "SWORD_METAL": sprite = Sprite.sword_metal; break;
		case "SWORD_CRYSTAL": sprite = Sprite.sword_crystal; break;
		
		case "AXE_WOOD": sprite = Sprite.axe_wood; break;
		case "AXE_METAL": sprite = Sprite.axe_metal; break;
		case "AXE_CRYSTAL": sprite = Sprite.axe_crystal; break;
		
		case "STAFF_WOOD": sprite = Sprite.staff_wood; break;
		case "STAFF_METAL": sprite = Sprite.staff_metal; break;
		case "STAFF_CRYSTAL": sprite = Sprite.staff_crystal; break;
		
		case "BOW_WOOD": sprite = Sprite.bow_wood; break;
		case "BOW_METAL": sprite = Sprite.bow_metal; break;
		case "BOW_CRYSTAL": sprite = Sprite.bow_crystal; break;
		}
	}
	
	public Sprite getSprite(){
		return this.sprite;
	}
	
	public String getItemName(){
		return this.itemName;
	}
	
}
