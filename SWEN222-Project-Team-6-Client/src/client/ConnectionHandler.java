package client;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import client.Packet.*;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class ConnectionHandler extends Listener implements Runnable {

	private Client client;	
	
	public ConnectionHandler() {		
	}

	public void init(Client client){
		this.client = client;
		
	}
	
	@Override
	public void connected(Connection arg0) {
		Log.info("[CLIENT] You have connected");
		client.sendTCP(new Packet0LoginRequest());
	}
	
	@Override
	public void disconnected(Connection arg0) {
		
	}
	
	@Override
	public void received(Connection c, Object o) {
		if(o instanceof Packet1LoginAnswer){
			boolean ans = ((Packet1LoginAnswer) o).accepted;
			if(ans){
				System.out.println("accepted");
				
			}else{
				c.close();
			}
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	
}
