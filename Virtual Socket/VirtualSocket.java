import java.io.*;
import java.net.*;
import java.util.Random;

public class VirtualSocket extends DatagramSocket
{
	//Paketin pudottmisen todennäköisyys (tehtävä 2.)
	private static double p_drop = 0.2;
	
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
			//Jos generoitiin satunnaisesti pienempi tai yhtäsuuri luku, pudotetaan paketti (tehtävä 2.)
			if (randGen.nextDouble() <= p_drop){
				System.out.println("Dropped packet");
			}
			else {
				try {
					// Asetetaan viivästys ennen paketin palautusta, aikaväli 0-3 sekunttia (tehtävä 3.)
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
	eli bitti 0 bitiksi 1 ja toisinpäin. (tehtävä 4.)
	*/
	public byte[] bittiVirhe(byte[] bitit){
		
		//Satunnainen bitti 
		int maski = 1 << satLuku(0, 7); 
		//Muutetaan kaikista saaduista byte-biteistä yksi satunnainen bitti.
		//Ei kosketa viimeiseen byteen, koska se on CRC8, tai ensimmäiseen, 
		//koska se on seq.
		for (int i = 1; i < bitit.length-1; i++){
			int muutettava = bitit[i];
			bitit[i] = (byte) (muutettava ^ maski);
		}
		
		return bitit;
	}
}