import java.io.*;
import java.net.*;
import java.util.Random;

public class VirtualSocket extends DatagramSocket
{
	//Paketin pudottmisen todennäköisyys
	private static double p_drop = 0.0;
	
	public VirtualSocket () throws SocketException{
		super();
	}
	
	public VirtualSocket(int portti) throws SocketException {
		super(portti);
	}
	
	public void receive(DatagramPacket packet) throws IOException {
		while(true){	
			Random randGen = new Random();
			/*Ehkä uusi paketti:*/
			
			byte[] copy = packet.getData();
			packet.setData(bittiVirhe(copy));
			
			super.receive(packet);
			//Jos generoitiin satunnaisesti pienempi tai yhtäsuuri luku, pudotetaan paketti
			if (randGen.nextDouble() <= p_drop){
				System.out.println("Dropped packet");
			}
			else {
				try {
					// Asetetaan viivästys ennen paketin palautusta, aikaväli 0-3 sekunttia
                    Thread.sleep(satLuku(0, 3000)); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
				return;
			}
		}
	}
	
	//Apufunktio satunnaisen luvun saamiseen annetulta väliltä
	public int satLuku(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	
	/*
	Apufunktio, jolla muutetaan yksi satunnainen bitti käänteiseksi,
	eli bitti 0 bitiksi 1 ja toisinpäin.
	*/
	public byte[] bittiVirhe(byte[] bitit){
		
		//Satunnainen bitti 
		int maski = 1 << satLuku(0, 7); 
		int muutettava = bitit[0];
		
		//Ehkä looppi jolla katsotaan että varmasti muuttui.
		
		bitit[0] = (byte) (muutettava ^ maski);
		
		return bitit;
	}
}