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
			
			super.receive(packet);
			//Jos generoitiin satunnaisesti pienempi tai yhtäsuuri luku, pudotetaan paketti (tehtävä 2.)
			if (randGen.nextDouble() <= p_drop){
				System.out.println("Dropped packet");
			} 
			else {
				// Asetetaan bittivirhe pakettiin (tehtävä 4.), tällä hetkellä virheen mahdollisuus on aina 100%.
				byte[] data = packet.getData();
				int errorIndex = satLuku(0, packet.getLength() - 1);
				data[errorIndex] = (byte) satLuku(0, 255); 

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
	
}