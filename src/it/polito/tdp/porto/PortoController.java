package it.polito.tdp.porto;

import java.net.URL;
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
		boxPrimo.getItems().addAll(model.getAutori().values());
		boxSecondo.getItems().addAll(model.getAutori().values());
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
    	Author autore = boxPrimo.getValue();
    	txtResult.appendText(model.printNeighbors(autore));
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
}
