package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Usuario;
import com.conferencias.negocio.servicios.UsuarioServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.time.LocalDate;

public class UsuariosCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private UsuarioServicio usuarioServicio;
    private TableView<Usuario> table;
    private ObservableList<Usuario> datos;

    // Campos del formulario
    private TextField txtNombre;
    private TextField txtEmail;
    private PasswordField txtContrasena; // Para la contraseña
    private ChoiceBox<String> cbRol;     // Rol: ADMIN, ORGANIZADOR, ASISTENTE
    private TextField txtFechaRegistro;  // Se muestra solo para información (no editable)

    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;
    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Usuario usuarioSeleccionado;

    public UsuariosCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.usuarioServicio = new UsuarioServicio();
        crearInterfaz();
        cargarDatos();
        configurarEstadoInicial();
    }

    private void crearInterfaz() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // ---------- FORMULARIO ----------
        // Usamos un GridPane de 5 columnas (para 4 campos; el quinto para alinear)
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        // Grupo 1: Etiquetas (Fila 0)
        Label lblNombre = new Label("Nombre:");
        Label lblEmail = new Label("Email:");
        Label lblContrasena = new Label("Contraseña:");
        Label lblRol = new Label("Rol:");
        lblNombre.setFont(Font.font(14));
        lblEmail.setFont(Font.font(14));
        lblContrasena.setFont(Font.font(14));
        lblRol.setFont(Font.font(14));
        formGrid.add(lblNombre, 0, 0);
        formGrid.add(lblEmail, 1, 0);
        formGrid.add(lblContrasena, 2, 0);
        formGrid.add(lblRol, 3, 0);

        // Grupo 1: Inputs (Fila 1)
        txtNombre = new TextField();
        txtEmail = new TextField();
        txtContrasena = new PasswordField();
        cbRol = new ChoiceBox<>();
        cbRol.getItems().addAll("ADMIN", "ORGANIZADOR", "ASISTENTE");
        txtNombre.setPrefWidth(300);
        txtEmail.setPrefWidth(300);
        txtContrasena.setPrefWidth(300);
        cbRol.setPrefWidth(300);
        formGrid.add(txtNombre, 0, 1);
        formGrid.add(txtEmail, 1, 1);
        formGrid.add(txtContrasena, 2, 1);
        formGrid.add(cbRol, 3, 1);

        // Grupo 2: Información adicional (Solo una fila para Fecha de Registro)
        Label lblFechaRegistro = new Label("Fecha Registro:");
        lblFechaRegistro.setFont(Font.font(14));
        txtFechaRegistro = new TextField();
        txtFechaRegistro.setPrefWidth(300);
        txtFechaRegistro.setDisable(true); // Solo informativo
        formGrid.add(lblFechaRegistro, 0, 2);
        formGrid.add(txtFechaRegistro, 1, 2);

        // ---------- BOTONES DEL FORMULARIO ----------
        HBox hboxFormButtons = new HBox(20);
        hboxFormButtons.setAlignment(Pos.CENTER);
        btnGuardar = new Button("Guardar");
        btnCancelar = new Button("Cancelar");
        hboxFormButtons.getChildren().addAll(btnGuardar, btnCancelar);

        // ---------- BOTONES CRUD GENERALES ----------
        HBox hboxCrud = new HBox(20);
        hboxCrud.setAlignment(Pos.CENTER);
        btnNuevo = new Button("Nuevo");
        btnEditar = new Button("Editar");
        btnEliminar = new Button("Eliminar");
        btnVolver = new Button("Volver al Menú");
        hboxCrud.getChildren().addAll(btnNuevo, btnEditar, btnEliminar, btnVolver);

        VBox vboxControls = new VBox(10, formGrid, hboxFormButtons, hboxCrud);
        vboxControls.setAlignment(Pos.CENTER);
        vboxControls.setPadding(new Insets(10));

        // ---------- TABLA ----------
        table = new TableView<>();
        table.setPrefHeight(300);
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));
        TableColumn<Usuario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRol()));
        TableColumn<Usuario, LocalDate> colFechaRegistro = new TableColumn<>("Fecha Registro");
        colFechaRegistro.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaRegistro()));
        table.getColumns().addAll(colId, colNombre, colEmail, colRol, colFechaRegistro);

        // Listener de selección en la tabla
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                usuarioSeleccionado = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                usuarioSeleccionado = null;
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
            }
        });

        // ---------- ACCIONES DE BOTONES ----------
        btnNuevo.setOnAction(e -> {
            modo = "NEW";
            limpiarInputs();
            habilitarInputs(true);
        });
        btnEditar.setOnAction(e -> {
            if (usuarioSeleccionado != null) {
                modo = "EDIT";
                cargarInputs(usuarioSeleccionado);
                habilitarInputs(true);
            }
        });
        btnEliminar.setOnAction(e -> {
            if (usuarioSeleccionado != null) {
                try {
                    usuarioServicio.eliminarUsuario(usuarioSeleccionado.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });
        btnGuardar.setOnAction(e -> {
            try {
                Usuario u = modo.equals("NEW") ? new Usuario() : usuarioSeleccionado;
                u.setNombre(txtNombre.getText());
                u.setEmail(txtEmail.getText());
                u.setContrasenaHash(txtContrasena.getText());
                u.setRol(cbRol.getValue());
                // Para registro, si es nuevo, se setea la fecha actual (en edición la fecha no cambia)
                if (modo.equals("NEW")) {
                    u.setFechaRegistro(LocalDate.now());
                    usuarioServicio.registrarUsuario(u);
                } else if (modo.equals("EDIT")) {
                    // Para edición, se actualiza sin cambiar la fecha de registro
                    usuarioServicio.actualizarRol(u.getId(), u.getRol());
                }
                cargarDatos();
                limpiarInputs();
                habilitarInputs(false);
            } catch (Exception ex) {
                mostrarAlerta("Error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        btnCancelar.setOnAction(e -> {
            limpiarInputs();
            habilitarInputs(false);
        });
        btnVolver.setOnAction(e -> mainApp.mostrarMenuPrincipal());

        VBox vboxPrincipal = new VBox(20, vboxControls, table);
        vboxPrincipal.setAlignment(Pos.CENTER);
        root.setCenter(vboxPrincipal);

        scene = new Scene(root, 1280, 720);
    }

    private void configurarEstadoInicial() {
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        habilitarInputs(false);
    }

    private void habilitarInputs(boolean habilitar) {
        txtNombre.setDisable(!habilitar);
        txtEmail.setDisable(!habilitar);
        txtContrasena.setDisable(!habilitar);
        cbRol.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtNombre.clear();
        txtEmail.clear();
        txtContrasena.clear();
        cbRol.getSelectionModel().clearSelection();
        txtFechaRegistro.clear();
    }

    private void cargarInputs(Usuario u) {
        txtNombre.setText(u.getNombre());
        txtEmail.setText(u.getEmail());
        // Para editar, normalmente no se reingresa la contraseña, se podría dejar en blanco
        txtContrasena.setText("");
        cbRol.setValue(u.getRol());
        txtFechaRegistro.setText(u.getFechaRegistro().toString());
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(usuarioServicio.listarUsuarios());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar usuarios: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
