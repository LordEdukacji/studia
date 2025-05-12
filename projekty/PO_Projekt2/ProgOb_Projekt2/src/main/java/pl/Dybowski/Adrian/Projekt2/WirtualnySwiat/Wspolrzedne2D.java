package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat;

public class Wspolrzedne2D {
	  private int x;
	  private int y;

	  public Wspolrzedne2D() {
	    this.x = 0;
	    this.y = 0;
	  }

	  public Wspolrzedne2D(int x, int y) {
	    this.x = x;
	    this.y = y;
	  }

	  public Wspolrzedne2D(Wspolrzedne2D inne) {
	    this.x = inne.x;
	    this.y = inne.y;
	  }

	  public int getX() {
	    return x;
	  }

	  public int getY() {
	    return y;
	  }

	  @Override
	  public String toString() {
	    return "("+x+", "+y+")";
	  }

	  @Override
	  public int hashCode() {
		  final int liczbaPierwsza = 65537;
		  return x * liczbaPierwsza + y;
	  }
	  
	  @Override
	  public boolean equals(Object obiekt) {
		  if (!(obiekt instanceof Wspolrzedne2D)) return false;
		  
		  Wspolrzedne2D inne = (Wspolrzedne2D) obiekt;
		  
		  return this.x == inne.x && this.y == inne.y;
	  }
}

