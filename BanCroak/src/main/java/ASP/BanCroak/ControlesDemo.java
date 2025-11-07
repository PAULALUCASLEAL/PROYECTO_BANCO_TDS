package ASP.BanCroak;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ControlesDemo extends Application {

    @Override
    public void start(Stage stage) {
        // --- ListView ---
        Label labelLista = new Label("Lenguajes preferidos:");
        ListView<String> listaLenguajes = new ListView<>();
        listaLenguajes.getItems().addAll("Java", "Python", "Kotlin", "C#", "C++");
        listaLenguajes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaLenguajes.setPrefHeight(100);
        listaLenguajes.setStyle("-fx-background-insets: 0; -fx-selection-bar: #4CAF50 ; -fx-selection-bar-non-focused: red;");

        // --- ComboBox ---
        Label labelCombo = new Label("País de residencia:");
        ComboBox<String> comboPais = new ComboBox<>();
        comboPais.getItems().addAll("España", "Francia", "Italia", "Alemania");
        comboPais.setPromptText("Selecciona un país");

        // --- CheckBox ---
        CheckBox checkSuscripcion = new CheckBox("Suscribirme a las noticias");

        // --- RadioButtons ---
        Label labelSexo = new Label("Sexo:");
        RadioButton hombre = new RadioButton("Hombre");
        RadioButton mujer = new RadioButton("Mujer");
        RadioButton otro = new RadioButton("Prefiero no decirlo");
        otro.setSelected(true); //opción por defecto

        ToggleGroup grupoGenero = new ToggleGroup();
        hombre.setToggleGroup(grupoGenero);
        mujer.setToggleGroup(grupoGenero);
        otro.setToggleGroup(grupoGenero);

        HBox boxSexo = new HBox(10, hombre, mujer, otro);
        boxSexo.setStyle("-fx-border-color: orange; -fx-border-width: 5; -fx-padding: 10;");

        // --- Botón de acción ---
        Button botonMostrar = new Button("Mostrar selección");
        TextArea resultado = new TextArea();
        resultado.setEditable(false);
        resultado.setWrapText(true);

        botonMostrar.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();

            sb.append("Lenguajes seleccionados: ")
              .append(listaLenguajes.getSelectionModel().getSelectedItems())
              .append("\n");

            sb.append("País: ")
              .append(comboPais.getValue() != null ? comboPais.getValue() : "Ninguno")
              .append("\n");

            sb.append("Suscripción: ")
              .append(checkSuscripcion.isSelected() ? "Sí" : "No")
              .append("\n");

            Toggle seleccionado = grupoGenero.getSelectedToggle();
            sb.append("Sexo: ")
              .append(seleccionado != null ? ((RadioButton) seleccionado).getText() : "Sin especificar");

            resultado.setText(sb.toString());
        });
        // --- Notificaciones ---
        TextField valor = new TextField();
        valor.setPromptText("Mete algo");
        valor.textProperty().addListener((objetoText,anteriorV, nuevoV)->{
        	int val=0;
        	try { val=Integer.parseInt(nuevoV);
        	}catch(Exception e) {};
        	if(val<0) {
        		valor.setStyle("-fx-text-fill:red;");
        	} else valor.setStyle("-fx-text-fill:black;");
        });
        //--- Binding ---
        TextField campo1=new TextField();
        TextField campo2=new TextField();
        HBox boxCampos=new HBox(10);
        boxCampos.getChildren().addAll(new Label("campo1:"), campo1,
        								new Label("campo2:"), campo2);
        //campo1.textProperty().bind(campo2.textProperty());
        //campo1.textProperty().bindBidirectional(campo2.textProperty());
        

        // --- Layout principal ---
        VBox root = new VBox(10,
                labelLista, listaLenguajes,
                labelCombo, comboPais,
                boxCampos,
                valor,
                labelSexo, boxSexo,
                checkSuscripcion,
                botonMostrar, resultado
        );
        root.setPadding(new Insets(15));
        root.setStyle("-fx-font-family: 'Segoe UI';");
        //"-fx-font-size: 18px;"
        valor.textProperty().bind(root.widthProperty().asString("%.2f"));
        // --- Escena ---
        Scene scene = new Scene(root, 400, 500);
        //Scene scene = new Scene(root);
        stage.setTitle("Ejemplo de controles de selección en JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

