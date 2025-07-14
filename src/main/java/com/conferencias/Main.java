package com.conferencias;

import com.conferencias.datos.entidades.Usuario;
import com.conferencias.presentacion.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private Usuario currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mostrarMenuPrincipal();
    }

    public void setCurrentUser(Usuario user) {
        this.currentUser = user;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    // Método opcional para cerrar sesión:
    public void cerrarSesion() {
        this.currentUser = null;
        mostrarLogin();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void mostrarLogin() {
        LoginScreen loginScreen = new LoginScreen(this);
        Scene scene = loginScreen.getScene();
        primaryStage.setTitle("Sistema de Gestión de Conferencias - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarMenuPrincipal() {
        // Llama al menú principal una vez autenticado
        com.conferencias.presentacion.MainMenu mainMenu = new com.conferencias.presentacion.MainMenu(this);
        Scene scene = mainMenu.getScene();
        primaryStage.setTitle("Sistema de Gestión de Conferencias - Menú Principal");
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
