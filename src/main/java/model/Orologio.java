package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Orologio implements Serializable {

    private int id;
    private String nomeModello;
    private String marca;
    private Integer idCategoria;      // Integer per gestire NULL dal DB
    private String movimento;
    private String materialeCassa;
    private Integer diametroMM;       // Integer per gestire NULL dal DB
    private String impermeabilita;
    private String descrizione;
    private String immagine;
    private BigDecimal prezzoAttuale;
    private int stock;
    private boolean attivo;

    public Orologio() {
        // costruttore vuoto richiesto per JavaBean
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeModello() {
        return nomeModello;
    }

    public void setNomeModello(String nomeModello) {
        this.nomeModello = nomeModello;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getMovimento() {
        return movimento;
    }

    public void setMovimento(String movimento) {
        this.movimento = movimento;
    }

    public String getMaterialeCassa() {
        return materialeCassa;
    }

    public void setMaterialeCassa(String materialeCassa) {
        this.materialeCassa = materialeCassa;
    }

    public Integer getDiametroMM() {
        return diametroMM;
    }

    public void setDiametroMM(Integer diametroMM) {
        this.diametroMM = diametroMM;
    }

    public String getImpermeabilita() {
        return impermeabilita;
    }

    public void setImpermeabilita(String impermeabilita) {
        this.impermeabilita = impermeabilita;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public BigDecimal getPrezzoAttuale() {
        return prezzoAttuale;
    }

    public void setPrezzoAttuale(BigDecimal prezzoAttuale) {
        this.prezzoAttuale = prezzoAttuale;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }
}