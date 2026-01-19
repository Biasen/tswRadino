package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Carrello implements Serializable {

    //key = id prodotto
    private final Map<Integer, RigaCarrello> righe = new LinkedHashMap<>();

    public void aggiungi(Orologio o, int qta){
        if (o == null) return;
        if (qta <= 0) qta = 1;

        RigaCarrello r = righe.get(o.getId());
        if (r == null){
            righe.put(o.getId(), new RigaCarrello(o, qta));
        } else{
            r.setQuantita(r.getQuantita() + qta);
        }
    }

    public void rimuovi(int idProdotto){
        righe.remove(idProdotto);
    }

    public void aggiornaQuantita(int idProdotto, int qta){
        if(!righe.containsKey(idProdotto)) return;
        if(qta <= 0){
            righe.remove(idProdotto);
        } else {
            righe.get(idProdotto).setQuantita(qta);
        }
    }

    public Collection<RigaCarrello> getRighe(){
        return righe.values();
    }

    public BigDecimal getTotale(){
        BigDecimal tot = BigDecimal.ZERO;
        for(RigaCarrello r : righe.values()){
            tot = tot.add(r.getSubtotale());
        }
        return tot;
    }

    public boolean isVuoto(){
        return righe.isEmpty();
    }

}
