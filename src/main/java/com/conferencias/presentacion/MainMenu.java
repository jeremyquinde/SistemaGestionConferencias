package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu {

    private Main mainApp;
    private Scene scene;

    public MainMenu(Main mainApp) {
        this.mainApp = mainApp;
        crearInterfaz();
    }

    private void crearInterfaz() {
        // Verifica que exista un usuario actual
        Usuario currentUser = mainApp.getCurrentUser();
        if (currentUser == null) {
            System.err.println("Usuario actual no establecido. Redirigiendo al login.");
            mainApp.mostrarLogin();
            return;  // Evitar seguir con el menú
        }

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // Opciones según rol:
        if (currentUser.getRol().equalsIgnoreCase("ADMIN")) {
            Button btnUsuarios = new Button("Gestión de Usuarios");
            btnUsuarios.setOnAction(e ->
                    mainApp.getPrimaryStage().setScene(new UsuariosCRUDScreen(mainApp).getScene()));
            vbox.getChildren().add(btnUsuarios);
        }
        if (currentUser.getRol().equalsIgnoreCase("ORGANIZADOR") ||
                currentUser.getRol().equalsIgnoreCase("ADMIN")) {
            Button btnEventos = new Button("Gestión de Eventos");
            btnEventos.setOnAction(e ->
                    mainApp.getPrimaryStage().setScene(new EventosCRUDScreen(mainApp).getScene()));
            vbox.getChildren().add(btnEventos);
        }
        if (currentUser.getRol().equalsIgnoreCase("ASISTENTE") ||
                currentUser.getRol().equalsIgnoreCase("ADMIN") ||
                currentUser.getRol().equalsIgnoreCase("ORGANIZADOR")) {
            Button btnInscripciones = new Button("Gestión de Inscripciones");
            btnInscripciones.setOnAction(e ->
                    mainApp.getPrimaryStage().setScene(new InscripcionesCRUDScreen(mainApp).getScene()));
            vbox.getChildren().add(btnInscripciones);
        }

        Button btnPonentes = new Button("Gestión de Ponentes");
        btnPonentes.setOnAction(e ->
                mainApp.getPrimaryStage().setScene(new PonentesCRUDScreen(mainApp).getScene()));
        vbox.getChildren().add(btnPonentes);

        Button btnUbicaciones = new Button("Gestión de Ubicaciones");
        btnUbicaciones.setOnAction(e ->
                mainApp.getPrimaryStage().setScene(new UbicacionesCRUDScreen(mainApp).getScene()));
        vbox.getChildren().add(btnUbicaciones);

        if (currentUser.getRol().equalsIgnoreCase("ADMIN")) {
            Button btnPatrocinadores = new Button("Gestión de Patrocinadores");
            btnPatrocinadores.setOnAction(e ->
                    mainApp.getPrimaryStage().setScene(new PatrocinadoresCRUDScreen(mainApp).getScene()));
            vbox.getChildren().add(btnPatrocinadores);

            Button btnReportes = new Button("Generar Reportes");
            btnReportes.setOnAction(e ->
                    mainApp.getPrimaryStage().setScene(new ReportesCRUDScreen(mainApp).getScene()));
            vbox.getChildren().add(btnReportes);
        }

        Button btnSalir = new Button("Salir");
        btnSalir.setOnAction(e -> mainApp.cerrarSesion());
        vbox.getChildren().add(btnSalir);

        scene = new Scene(vbox, 1280, 720);
    }

    public Scene getScene() {
        return scene;
    }
}
