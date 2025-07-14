package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.negocio.servicios.UsuarioServicio;
import com.conferencias.datos.entidades.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LoginScreen {

    private Main mainApp;
    private Scene scene;
    private UsuarioServicio usuarioServicio;

    public LoginScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.usuarioServicio = new UsuarioServicio();
        crearInterfaz();
    }

    private void crearInterfaz() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Contraseña:");
        TextField passwordField = new TextField();

        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText(); // En producción, hashea la contraseña
            try {
                // Dentro del método que maneja el evento de login, después de la validación:
                Usuario usuario = usuarioServicio.obtenerUsuarioPorEmail(email);
                if (usuario != null && usuario.getContrasenaHash().equals(password)) {
                    System.out.println("Inicio de sesión exitoso para: " + usuario.getRol());
                    // Asigna el usuario actual en Main:
                    mainApp.setCurrentUser(usuario);
                    mainApp.mostrarMenuPrincipal();
                } else {
                    System.out.println("Credenciales inválidas.");
                }

            } catch (Exception ex) {
                System.out.println("Error al iniciar sesión: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(btnLogin, 1, 2);

        scene = new Scene(grid, 400, 250);
    }

    public Scene getScene() {
        return scene;
    }
}
