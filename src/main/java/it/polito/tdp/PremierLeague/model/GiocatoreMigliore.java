package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {
	Player p;
	double eff;
	
	public GiocatoreMigliore(Player p, double eff) {
		super();
		this.p = p;
		this.eff = eff;
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public double getEff() {
		return eff;
	}

	public void setEff(double eff) {
		this.eff = eff;
	}

	@Override
	public String toString() {
		return "Miglior Giocatore: " + p.getPlayerID() + " - " + p.getName() + ", delta efficienza=" + eff;
	}
	
	
}
