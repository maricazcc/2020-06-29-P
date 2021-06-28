/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<String> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Crea prima il grafo!");
    		return;
    	}
    	
    	String msg = this.model.connessioneMax();
    	this.txtResult.appendText(msg);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	try {
    	     String m = cmbMese.getValue();
    	     if(m == null) {
    	    	 this.txtResult.appendText("Scegli un mese!");
    	    	 return;
    	     }
    	     
    	     int min = Integer.parseInt(txtMinuti.getText());
    	     if(min <0 || min >90) {
    	    	 this.txtResult.appendText("Inserisci un numero tra 0 e 90!");
    	    	 return;
    	     }
    	     
    	     String msg = this.model.creaGrafo(m, min);
    	     this.txtResult.appendText(msg);
    	     
    	     cmbM1.getItems().addAll(this.model.getVertici());
    	     cmbM2.getItems().addAll(this.model.getVertici());
    	     
    	
    	} catch(NumberFormatException n) {
    		this.txtResult.appendText("Inserire un intero!");
    		return;
    	}

    	
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	txtResult.clear();
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Crea prima il grafo!");
    		return;
    	}
    	
    	Match m1 = this.cmbM1.getValue();
    	Match m2 = this.cmbM2.getValue();
    	if(m1==null || m2==null) {
    		this.txtResult.appendText("Scegliere le due partite!");
    		return;
    	}    	
    	List<Match> risultato = new ArrayList<Match> (this.model.trovaPercorso(m1, m2));
    	
    	for(Match ris : risultato)
    		this.txtResult.appendText(ris.toString() + "\n");    
    		
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbMese.getItems().addAll(this.model.getMesi());
  
    }
    
    
}
