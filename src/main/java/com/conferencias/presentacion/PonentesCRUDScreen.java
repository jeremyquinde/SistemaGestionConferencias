package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Ponente;
import com.conferencias.negocio.servicios.PonenteServicio;
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

public class PonentesCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private PonenteServicio ponenteServicio;
    private TableView<Ponente> table;
    private ObservableList<Ponente> datos;

    // Campos del formulario
    private TextField txtNombre;
    private TextField txtEspecialidad;
    private TextField txtEmail;
    private TextField txtTarifaHora;
    private CheckBox chkDisponible;


    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;

    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Ponente ponenteSeleccionado;

    public PonentesCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.ponenteServicio = new PonenteServicio();
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

        // Grupo 1: Etiquetas e Inputs (fila 0 y 1)
        Label lblNombre = new Label("Nombre:");
        Label lblEspecialidad = new Label("Especialidad:");
        Label lblEmail = new Label("Email:");
        lblNombre.setFont(Font.font(14));
        lblEspecialidad.setFont(Font.font(14));
        lblEmail.setFont(Font.font(14));
        formGrid.add(lblNombre, 0, 0);
        formGrid.add(lblEspecialidad, 1, 0);
        formGrid.add(lblEmail, 2, 0);

        txtNombre = new TextField();
        txtEspecialidad = new TextField();
        txtEmail = new TextField();
        txtNombre.setPrefWidth(300);
        txtEspecialidad.setPrefWidth(300);
        txtEmail.setPrefWidth(300);
        formGrid.add(txtNombre, 0, 1);
        formGrid.add(txtEspecialidad, 1, 1);
        formGrid.add(txtEmail, 2, 1);

        // Grupo 2: (solo dos campos, dejamos la tercera celda vacía)
        Label lblTarifaHora = new Label("Tarifa Hora:");
        Label lblDisponible = new Label("Disponible:");
        lblTarifaHora.setFont(Font.font(14));
        lblDisponible.setFont(Font.font(14));
        formGrid.add(lblTarifaHora, 0, 2);
        formGrid.add(lblDisponible, 1, 2);

        txtTarifaHora = new TextField();
        chkDisponible = new CheckBox();
        txtTarifaHora.setPrefWidth(300);
        chkDisponible.setPrefWidth(300);
        formGrid.add(txtTarifaHora, 0, 3);
        formGrid.add(chkDisponible, 1, 3);

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
        TableColumn<Ponente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        TableColumn<Ponente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        TableColumn<Ponente, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEspecialidad()));
        TableColumn<Ponente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        TableColumn<Ponente, Double> colTarifa = new TableColumn<>("Tarifa Hora");
        colTarifa.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTarifaHora()).asObject());
        TableColumn<Ponente, Boolean> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isDisponibilidad()).asObject());
        table.getColumns().addAll(colId, colNombre, colEspecialidad, colEmail, colTarifa, colDisponible);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                ponenteSeleccionado = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                ponenteSeleccionado = null;
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
            if (ponenteSeleccionado != null) {
                modo = "EDIT";
                cargarInputs(ponenteSeleccionado);
                habilitarInputs(true);
            }
        });
        btnEliminar.setOnAction(e -> {
            if (ponenteSeleccionado != null) {
                try {
                    ponenteServicio.eliminarPonente(ponenteSeleccionado.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });
        btnGuardar.setOnAction(e -> {
            try {
                Ponente p = modo.equals("NEW") ? new Ponente() : ponenteSeleccionado;
                p.setNombre(txtNombre.getText());
                p.setEspecialidad(txtEspecialidad.getText());
                p.setEmail(txtEmail.getText());
                p.setTarifaHora(Double.parseDouble(txtTarifaHora.getText()));
                p.setDisponibilidad(chkDisponible.isSelected());

                if (modo.equals("NEW")) {
                    ponenteServicio.registrarPonente(p);
                } else if (modo.equals("EDIT")) {
                    ponenteServicio.actualizarPonente(p);
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
        txtEspecialidad.setDisable(!habilitar);
        txtEmail.setDisable(!habilitar);
        txtTarifaHora.setDisable(!habilitar);
        chkDisponible.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtNombre.clear();
        txtEspecialidad.clear();
        txtEmail.clear();
        txtTarifaHora.clear();
        chkDisponible.setText("");
    }

    private void cargarInputs(Ponente ponente) {
        txtNombre.setText(ponente.getNombre());
        txtEspecialidad.setText(ponente.getEspecialidad());
        txtEmail.setText(ponente.getEmail());
        txtTarifaHora.setText(String.valueOf(ponente.getTarifaHora()));
        chkDisponible.setText(String.valueOf(ponente.isDisponibilidad()));
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(ponenteServicio.listarPonentes());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar ponentes: " + ex.getMessage());
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
