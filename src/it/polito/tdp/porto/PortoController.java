package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model;
	
	public void setModel(Model model) {
		this.model = model;
		model.creaGrafo();
		List<Author> tutti = new ArrayList<>(model.getAutori().values());
		boxPrimo.getItems().addAll(tutti);
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	boxSecondo.getItems().clear();
    	Author autore = boxPrimo.getValue();
    	txtResult.appendText(model.printNeighbors(autore));
    	List<Author> tutti = new ArrayList<>(model.getAutori().values());
    	List<Author> togli = new ArrayList<>(model.getVicini(autore));
    	tutti.removeAll(togli);
    	tutti.remove(autore);
    	boxSecondo.getItems().addAll(tutti);
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	Author primo = boxPrimo.getValue();
    	Author secondo = boxSecondo.getValue();
    	List<String> res = new ArrayList<>(model.getPapers(primo, secondo));
    	for(String s : res) {
    		txtResult.appendText(s+"\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
    }
}
