/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import domein.Cursus;
import domein.DomeinController;
import domein.Leerling;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ZoekLlnSchermController implements Initializable {
    
    private DomeinController dc = new DomeinController();

    public DomeinController getDc() {
        return dc;
    }

    public void setDc(DomeinController dc) {
        this.dc = dc;
    }
    
    private Cursus cursus;

    @FXML
    private ListView listView;
    @FXML
    private Button ok;

    @FXML
    private Button zoekenButton;

    @FXML
    private ListView<Leerling> lijst;

    @FXML
    private Text naamLbl;

    @FXML
    private Text nummerLbl;

    @FXML
    private Text emailLbl;

    @FXML
    private ImageView imgView;
    
    @FXML
    private TextField nummerField;
    
    @FXML
    private Label zoekNaamLbl;
    
    @FXML
    private TextField zoekNaamTxtField;
    
    //RijtechniekScherm
    private Map<String, List<String>> rijtechniekOpmerkingenMap;
    private Map<String, String> rijtechniekKleurenMap;
    private Map<String, Map<String, List<String>>> evaRijtechniekMap;
    private Map<String, Map<String, List<String>>> VorigeEvaRijtechniekMap;
    private Map<String, Map<String, List<String>>> evaRijtechniekMap1;
    private Map<String, Map<String, List<String>>> evaRijtechniekMap2;
    private Map<String, Map<String, List<String>>> evaRijtechniekMap3;
    
    //StuurtechniekScherm
    private Map<String, List<String>> stuurtechniekOpmerkingenMap;
    private Map<String, String> stuurtechniekKleurenMap;
    private Map<String, Map<String, List<String>>> evaStuurtechniekMap;
    private Map<String, Map<String, List<String>>> VorigeEvaStuurtechniekMap;
    private Map<String, Map<String, List<String>>> evaStuurtechniekMap1;
    private Map<String, Map<String, List<String>>> evaStuurtechniekMap2;
    private Map<String, Map<String, List<String>>> evaStuurtechniekMap3;
    
    private OverzichtSchermController ozc = new OverzichtSchermController();
    
    private Leerling geselecteerdeLeerling = new Leerling(); 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rijtechniekOpmerkingenMap.put("ambreage", null);
        rijtechniekOpmerkingenMap.put("rem", null);
        rijtechniekOpmerkingenMap.put("stuur", null);
        rijtechniekOpmerkingenMap.put("schakelen", null);
        rijtechniekOpmerkingenMap.put("opmerkzaamheid", null);
        rijtechniekOpmerkingenMap.put("parkeren", null);
        rijtechniekOpmerkingenMap.put("keren in een straat", null);
        rijtechniekOpmerkingenMap.put("garage", null);
        rijtechniekOpmerkingenMap.put("achteruitrijden", null);
        rijtechniekOpmerkingenMap.put("bochten", null);
        rijtechniekOpmerkingenMap.put("helling", null);
        rijtechniekOpmerkingenMap.put("zithouding", null);
        
        rijtechniekKleurenMap.put("ambreage", null);
        rijtechniekKleurenMap.put("rem", null);
        rijtechniekKleurenMap.put("stuur", null);
        rijtechniekKleurenMap.put("schakelen", null);
        rijtechniekKleurenMap.put("opmerkzaamheid", null);
        rijtechniekKleurenMap.put("parkeren", null);
        rijtechniekKleurenMap.put("keren in een straat", null);
        rijtechniekKleurenMap.put("garage", null);
        rijtechniekKleurenMap.put("achteruitrijden", null);
        rijtechniekKleurenMap.put("bochten", null);
        rijtechniekKleurenMap.put("helling", null);
        rijtechniekKleurenMap.put("zithouding", null);
        
        
        zoekNaamLbl.setVisible(false);
        zoekNaamTxtField.setVisible(false);
        ok.setDisable(true);
//        List<Leerling> test = dc.getLeerlingen();
        File startupimg = new File("src/images/boom.png");
        imgView.setImage(new Image(startupimg.toURI().toString()));
        imgView.scaleXProperty();
        imgView.scaleYProperty();
//        test.add(new Leerling("0001", "Joske Vermeulen", new File("src/images/GastonPng.png"), "joske.vermeulen@gmail.com"));
//        test.add(new Leerling("0002", "Ewout Ghysbrecht", new File("src/images/EwoutPng.png"), "ewout.g@hotmail.com"));
//        System.out.println(test.toString());
////        lijst.setItems(FXCollections.observableList(test));//leerling.getAlleLeerlingen() ipv test ofzo
//        System.out.println(lijst.toString());

        lijst.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {

                Leerling geselecteerd = lijst.getSelectionModel().getSelectedItem();
                System.out.println(geselecteerd.getFotoPath().toURI().toString());
                naamLbl.setText(geselecteerd.getNaam());
                nummerLbl.setText("" + geselecteerd.getInschrijvingsNummer());
                emailLbl.setText(geselecteerd.getEmail());
                imgView.setImage(new Image(geselecteerd.getFotoPath().toURI().toString()));
                
            }
        });

    }

    public void zoekOpNummer(ActionEvent event) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/projecten/api/leerlingen/nummer/"+ nummerField.getText());
        System.out.println(target.request(MediaType.APPLICATION_JSON).get(String.class));
        
        Gson gson = new Gson();
        JsonObject jsono = gson.fromJson(target.request(MediaType.APPLICATION_JSON).get(String.class), JsonElement.class).getAsJsonObject();
        System.out.println(jsono.get("naam").getAsString());
        
        naamLbl.setText(jsono.get("naam").toString());
        geselecteerdeLeerling.setInschrijvingsNummer(jsono.get("inschrijvingsNummer").toString());
        nummerLbl.setText(jsono.get("inschrijvingsNummer").toString());
        geselecteerdeLeerling.setNaam(jsono.get("naam").toString());
        emailLbl.setText(jsono.get("email").toString());
        geselecteerdeLeerling.setEmail(jsono.get("email").toString());
        imgView.setImage(new Image(new File("src/images/" + jsono.get("inschrijvingsNummer").getAsString() + ".png").toURI().toString()));
        geselecteerdeLeerling.setFotoPath(new File("src/images/" + jsono.get("inschrijvingsNummer").getAsString() + ".png"));
        dc.setGeselecteerd(geselecteerdeLeerling);
        
        cursus = new Cursus("1", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "rood", "rood", "rood", 0.0, "", null, null, null, null, null, null);
        if(naamLbl.getText() != "naam"){
            ok.setDisable(false);
        }
    }
    
    public void zoekAlle(ActionEvent event){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/projecten/api/leerlingen");
    }

    public void veranderScherm(ActionEvent event) throws IOException {
        Stage stage = (Stage) zoekenButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void naarOverzichtScherm(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        dc.setOzc(ozc);
        dc.setCursus(cursus);
        ozc.setDc(dc);
        loader.setLocation(getClass().getResource("OverzichtScherm.fxml"));
        Stage stage = (Stage) zoekenButton.getScene().getWindow();
        loader.setController(dc.getOzc());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    
}
