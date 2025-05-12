package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Organizm implements Comparable<Organizm> {
	private final int inicjatywa;
	
	private int wiek;
	
	protected final Swiat swiat;
	protected Wspolrzedne2D polozenie;
	
	protected int sila;
	protected boolean zyje;
	
	public Organizm(Swiat swiat, Wspolrzedne2D polozenie, int inicjatywa, int sila) {
		this.swiat = swiat;
		this.polozenie = polozenie;
		this.inicjatywa = inicjatywa;
		this.wiek = swiat.nadajWiek();
		this.setSila(sila);
		this.setZyje(true);
	}
	//------------------------------GETTERY I SETTERY-------------------------------------------------
	public Wspolrzedne2D getPolozenie() {
		return polozenie;
	}
	
	public void setPolozenie(Wspolrzedne2D polozenie) {
		this.polozenie = polozenie;
	}
	
	public int getInicjatywa() {
		return inicjatywa;
	}

	public int getWiek() {
		return wiek;
	}

	public int getSila() {
		return sila;
	}

	public void setSila(int sila) {
		this.sila = sila;
	}
	
	public void zmienSila(int zmiana) {
		this.sila += zmiana;
	}

	public boolean isZyje() {
		return zyje;
	}

	public void setZyje(boolean zyje) {
		this.zyje = zyje;
	}
	
	public void setWiek(int wiek) {
		this.wiek = wiek;
	}
	//----------------------------------------PODSTAWY------------------------------------------------
	public abstract void akcja();
	
	public abstract void kolizja(Organizm ofiara, boolean atak);
	
	public void rysowanie(JLabel pole) {
		ImageIcon oryginalnaIkona = new ImageIcon(getClass().getResource("/" + this.getClass().getSimpleName() + ".png"));
		Image obrazek = oryginalnaIkona.getImage();
		Image zmienionyRozmiar = obrazek.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		pole.setIcon(new ImageIcon(zmienionyRozmiar));
	}
	
	public void zakonczZycie() {
		zyje = false;
		swiat.usunOrganizmZPola(this);
	}
	
	public void zamienMiejscami(Organizm inny) {
		swiat.usunOrganizmZPola(this);
		swiat.usunOrganizmZPola(inny);
		
		Wspolrzedne2D tmp = this.polozenie;
		this.polozenie = inny.polozenie;
		inny.polozenie = tmp;
		
		swiat.dodajOrganizmDoPola(this);
		swiat.dodajOrganizmDoPola(inny);
	}
	//-----------------------------------EFEKTY SPECJALNE---------------------------------------------
	public boolean odbilAtak(Organizm atakujacy) {
		return false;
	}
	
	public boolean mozeUciec() {
		return false;
	}
	
	public void ucieknij(Organizm przeciwnik, boolean atak) {
		zakonczZycie();
	}
	//----------------------------------------ROZMNAZANIE---------------------------------------------
	public abstract boolean tegoSamegoTypu(Organizm inny);
	
	public abstract Organizm stworzDziecko(Wspolrzedne2D cel);
	//----------------------------------------ZAPISYWANIE---------------------------------------------
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" "+polozenie.getX()+" "+polozenie.getY()+" "+Integer.toString(getWiek())+" "+sila+"\n";
	}
	//----------------------------------------COMPARABLE----------------------------------------------
	// do sortowania
	@Override
	public int compareTo(Organizm inny) {
		int porownanie = Integer.compare(inny.inicjatywa, this.inicjatywa);
		
		if (porownanie != 0) return porownanie;
		
		return Integer.compare(this.wiek, inny.wiek);
	}
}
