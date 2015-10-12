package client.entity;

public class Item {

	private final String spriteDir;
	
	public Item(String dir){
		this.spriteDir = dir;
	}
	
	public String getDir(){
		return this.spriteDir;
	}
	
}
