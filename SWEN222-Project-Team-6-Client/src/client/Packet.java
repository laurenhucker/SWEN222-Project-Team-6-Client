package client;

public class Packet {
	public static class Packet0LoginRequest {
		private String user;
		private String pass;
		
			
		public Packet0LoginRequest(String user, String pass) {
			
		}
		public String getUser(){
			return this.user;
		}		
		public String getPass(){
			return this.pass;
		}
	}
	
	
	public static class Packet1LoginAnswer { 
		boolean accepted = false;
	}
	public static class Packet2ButtonClick {}
	public static class Packet3player {}
}
