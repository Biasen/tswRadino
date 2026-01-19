package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class RigaCarrello implements Serializable {
    private Orologio prodotto;
    private int quantita;

    public RigaCarrello() {}

    public RigaCarrello(Orologio prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public Orologio getProdotto() {return prodotto;}
    public void setProdotto(Orologio prodotto) {this.prodotto = prodotto;}

    public int getQuantita() {return quantita;}
    public void setQuantita(int quantita){this.quantita = quantita;}

    public BigDecimal getSubtotale(){
        return prodotto.getPrezzoAttuale().multiply(new BigDecimal(quantita));
    }
}
