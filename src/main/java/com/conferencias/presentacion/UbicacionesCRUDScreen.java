package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Ubicacion;
import com.conferencias.negocio.servicios.UbicacionServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.sql.SQLException;

public class UbicacionesCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private UbicacionServicio ubicacionServicio;
    private TableView<Ubicacion> table;
    private ObservableList<Ubicacion> datos;

    // Campos del formulario
    private TextField txtNombre;
    private TextField txtDireccion;
    private TextField txtCapacidad;
    private TextField txtTipo;

    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;

    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Ubicacion ubicacionSeleccionada;

    public UbicacionesCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.ubicacionServicio = new UbicacionServicio();
        crearInterfaz();
        cargarDatos();
        configurarEstadoInicial();
    }

    private void crearInterfaz() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // ---------- FORMULARIO ----------
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        // Una sola fila de etiquetas
        Label lblNombre = new Label("Nombre:");
        Label lblDireccion = new Label("Dirección:");
        Label lblCapacidad = new Label("Capacidad:");
        Label lblTipo = new Label("Tipo:");
        lblNombre.setFont(Font.font(14));
        lblDireccion.setFont(Font.font(14));
        lblCapacidad.setFont(Font.font(14));
        lblTipo.setFont(Font.font(14));
        formGrid.add(lblNombre, 0, 0);
        formGrid.add(lblDireccion, 1, 0);
        formGrid.add(lblCapacidad, 2, 0);
        formGrid.add(lblTipo, 3, 0);

        // Una sola fila de inputs
        txtNombre = new TextField();
        txtDireccion = new TextField();
        txtCapacidad = new TextField();
        txtTipo = new TextField();
        txtNombre.setPrefWidth(300);
        txtDireccion.setPrefWidth(300);
        txtCapacidad.setPrefWidth(300);
        txtTipo.setPrefWidth(300);
        formGrid.add(txtNombre, 0, 1);
        formGrid.add(txtDireccion, 1, 1);
        formGrid.add(txtCapacidad, 2, 1);
        formGrid.add(txtTipo, 3, 1);

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
        TableColumn<Ubicacion, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        TableColumn<Ubicacion, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        TableColumn<Ubicacion, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDireccion()));
        TableColumn<Ubicacion, Integer> colCapacidad = new TableColumn<>("Capacidad");
        colCapacidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacidad()).asObject());
        TableColumn<Ubicacion, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo()));
        table.getColumns().addAll(colId, colNombre, colDireccion, colCapacidad, colTipo);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                ubicacionSeleccionada = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                ubicacionSeleccionada = null;
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
            if (ubicacionSeleccionada != null) {
                modo = "EDIT";
                cargarInputs(ubicacionSeleccionada);
                habilitarInputs(true);
            }
        });
        btnEliminar.setOnAction(e -> {
            if (ubicacionSeleccionada != null) {
                try {
                    ubicacionServicio.eliminarUbicacion(ubicacionSeleccionada.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });
        btnGuardar.setOnAction(e -> {
            try {
                Ubicacion u = modo.equals("NEW") ? new Ubicacion() : ubicacionSeleccionada;
                u.setNombre(txtNombre.getText());
                u.setDireccion(txtDireccion.getText());
                u.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
                u.setTipo(txtTipo.getText());

                if (modo.equals("NEW")) {
                    ubicacionServicio.crearUbicacion(u);
                } else if (modo.equals("EDIT")) {
                    ubicacionServicio.actualizarUbicacion(u);
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
        txtDireccion.setDisable(!habilitar);
        txtCapacidad.setDisable(!habilitar);
        txtTipo.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtNombre.clear();
        txtDireccion.clear();
        txtCapacidad.clear();
        txtTipo.clear();
    }

    private void cargarInputs(Ubicacion ubicacion) {
        txtNombre.setText(ubicacion.getNombre());
        txtDireccion.setText(ubicacion.getDireccion());
        txtCapacidad.setText(String.valueOf(ubicacion.getCapacidad()));
        txtTipo.setText(ubicacion.getTipo());
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(ubicacionServicio.listarUbicaciones());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar ubicaciones: " + ex.getMessage());
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
