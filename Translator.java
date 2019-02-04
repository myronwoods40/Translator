//Myron Woods
//CS 4712
//June 17, 2018
//Assignment 2 Translation GUI

//Imports URL functionality 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//Imports JSON webcrawl package
import org.json.JSONArray;

//Imports JavaFX 
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;


public class Translator extends Application {
 
/*Makes a call to the Google translation API with the user requested phrase to be
translated. This call also requests a language for the phrase to be translated into. */
   public static String callUrlAndParseResult(String langFrom, String langTo,
   		String word) throws Exception {
   
      String url = "http://translate.googleapis.com/translate_a/single?"+  
         	"client=gtx&" + 
         	"sl=" +  langFrom +  
         	"&tl=" +  langTo + 
         	"&dt=t&q=" +  URLEncoder.encode(word, "UTF-8");
   
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
   
      BufferedReader in = new BufferedReader(
         	new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
   
      while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
      }
      in.close();
   /*the return information from the Google API is then run through a JSON package
   that will turn the information into a readable state.*/
      return parseResult(response.toString());
   }
  
   private static String parseResult(String inputJson) throws Exception {
   
      JSONArray jsonArray = new JSONArray(inputJson);
      JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
      JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
      return jsonArray3.get(0).toString();
   }
   
   @Override
    public void start(Stage primaryStage)
   {
   VBox vbox = new VBox(30);
   BorderPane pane = new BorderPane();
   pane.setStyle(" -fx-background-color: lightblue;");
   primaryStage.setTitle("Translator");
   
   //Text field for user input
      TextField screen = new TextField();
      screen.setAlignment(Pos.CENTER_LEFT);
      screen.setPrefWidth(80);
      screen.setPrefHeight(150);
      screen.setStyle("background-color: white; -fx-font-size: 20;");
      pane.setTop(screen);
   
   //Drop down language selection and buttons
   ChoiceBox<String> choicebox = new ChoiceBox<>();
   choicebox.setStyle("-fx-font-size: 16;");
   choicebox.setValue("Spanish");
   choicebox.getItems().add("Spanish");
   choicebox.getItems().add("French");
   choicebox.getItems().add("German");
   Button trans = new Button("Translate");
   trans.setStyle("-fx-font-size: 16;");
   Button clear = new Button("Clear");
   clear.setStyle("-fx-font-size: 16;");
   
   //adjusts position of drop down and button.
   vbox.getChildren().addAll(choicebox,trans,clear);
   pane.setCenter(vbox);
   vbox.setAlignment(Pos.CENTER);
   
   //event handle that assigns actions to the buttons.
   trans.setOnAction(e->transButton(choicebox,screen));
   clear.setOnAction(e->{
	   screen.setEditable(true);
	   screen.setText("");
	   screen.setAlignment(Pos.CENTER_LEFT);
   });
   
   
   //Make it show and sets dimensions
   Scene stScene = new Scene(pane, 400, 400, Color.BLUE);
      primaryStage.setScene(stScene);
      primaryStage.setResizable(false);
      primaryStage.show();
   
   }  
   
   /*Method sets the action for the translate button*/
   public void transButton(ChoiceBox<String> choicebox,TextField screen){
   /*Takes the information that the user inputed and houses it in these
    variables. These variables are then used to retrieve information from
    the server. */
   String userInput = screen.getText();
   String language = choicebox.getValue();
   String langCode = "en";
   		/*The server uses codes for languages. However this is not user
   		 friendly. So the drop down box displays the languages in full.
   		 Then these are converted to their language code and sent to the server*/
   if(language == "Spanish"){langCode="es";}
   if(language == "French"){langCode="fr";}
   if(language == "German"){langCode="de";}
   
   try {
	     screen.setAlignment(Pos.CENTER);	
         screen.setText(callUrlAndParseResult("en",langCode,userInput));
      } 
      catch(Exception e) {
         System.out.println("sorry error");
      }
   screen.setEditable(false); 
   }
   
   /*Main method launches program*/
   public static void main(String[] args) {
   launch(args);
   }
}
