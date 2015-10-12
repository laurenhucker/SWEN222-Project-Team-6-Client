package client;

public class Packet {
	public static class Packet0LoginRequest {
		private String user;
		private String pass;
		
		public Packet0LoginRequest(String user, String pass){
			this.user = user;
			this.pass = pass;
		}
		
		public String getUser(){
			return this.user;
		}
		
		public String getPass(){
			return this.pass;
		}
	}
	
	//public Packet0LoginRequest getLoginPacket(String user, String pass){
	//	return new Packet0LoginRequest(user, pass);
	//}
	
	public static class Packet1LoginAnswer { 		
		boolean accepted = false;
	}
	public static class Packet2ButtonClick {
	}
}
