import java.io.*;
import java.net.*;

/*
Testisovellus, joka tällä hetkellä tulostaa virtuaalisokettiin saadun paketin,
tai tiedon siitä onko paketti pudotettu.
*/
class TestiSov {
	private static DatagramSocket soketti = null;
	
	public static void main(String[] args) throws IOException{
		soketti = new VirtualSocket(6666);
		boolean listening = true;
		while(listening){
			try {
				byte[] rec = new byte[256];
				DatagramPacket paketti = new DatagramPacket(rec, rec.length);
				soketti.receive(paketti);
				System.out.println(new String(rec, 0, paketti.getLength()-1));
			}
			catch (IOException e) {
				listening = false;
				System.out.println("catch");
				break;
			}
		}
	}
}