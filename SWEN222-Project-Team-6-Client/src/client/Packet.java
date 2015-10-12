package client;

public class Packet {
	public static class Packet0LoginRequest {}
	public static class Packet1LoginAnswer { 
		String user = "user";
		String pass = "pass";
		boolean accepted = false;}
	public static class Packet2ButtonClick {}
}
