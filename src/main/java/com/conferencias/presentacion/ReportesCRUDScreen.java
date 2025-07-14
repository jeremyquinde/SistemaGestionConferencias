package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Reporte;
import com.conferencias.negocio.servicios.ReporteServicio;
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
import java.time.LocalDateTime;

public class ReportesCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private ReporteServicio reporteServicio;
    private TableView<Reporte> table;
    private ObservableList<Reporte> datos;

    // Campos del formulario
    private TextField txtTipo;
    private TextField txtFechaGeneracion;
    private TextField txtDatosBrutos;
    private TextField txtUsuarioId;

    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;
    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Reporte reporteSeleccionado;

    public ReportesCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.reporteServicio = new ReporteServicio();
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

        // Una fila de etiquetas
        Label lblTipo = new Label("Tipo:");
        Label lblFechaGen = new Label("Fecha Generación (ISO):");
        Label lblDatos = new Label("Datos Brutos:");
        Label lblUsuarioId = new Label("ID Usuario:");
        lblTipo.setFont(Font.font(14));
        lblFechaGen.setFont(Font.font(14));
        lblDatos.setFont(Font.font(14));
        lblUsuarioId.setFont(Font.font(14));
        formGrid.add(lblTipo, 0, 0);
        formGrid.add(lblFechaGen, 1, 0);
        formGrid.add(lblDatos, 2, 0);
        formGrid.add(lblUsuarioId, 3, 0);

        // Una fila de inputs
        txtTipo = new TextField();
        txtFechaGeneracion = new TextField();
        txtDatosBrutos = new TextField();
        txtUsuarioId = new TextField();
        txtTipo.setPrefWidth(300);
        txtFechaGeneracion.setPrefWidth(300);
        txtDatosBrutos.setPrefWidth(300);
        txtUsuarioId.setPrefWidth(300);
        formGrid.add(txtTipo, 0, 1);
        formGrid.add(txtFechaGeneracion, 1, 1);
        formGrid.add(txtDatosBrutos, 2, 1);
        formGrid.add(txtUsuarioId, 3, 1);

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
        TableColumn<Reporte, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        TableColumn<Reporte, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTipo()));
        TableColumn<Reporte, String> colFecha = new TableColumn<>("Fecha Generación");
        colFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFechaGeneracion().toString()));
        TableColumn<Reporte, String> colDatos = new TableColumn<>("Datos Brutos");
        colDatos.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDatosBrutos()));
        TableColumn<Reporte, Integer> colUsuario = new TableColumn<>("ID Usuario");
        colUsuario.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getUsuarioId()).asObject());
        table.getColumns().addAll(colId, colTipo, colFecha, colDatos, colUsuario);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                reporteSeleccionado = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                reporteSeleccionado = null;
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
            if (reporteSeleccionado != null) {
                modo = "EDIT";
                cargarInputs(reporteSeleccionado);
                habilitarInputs(true);
            }
        });
        btnEliminar.setOnAction(e -> {
            if (reporteSeleccionado != null) {
                try {
                    reporteServicio.eliminarReporte(reporteSeleccionado.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });
        btnGuardar.setOnAction(e -> {
            try {
                Reporte rep = modo.equals("NEW") ? new Reporte() : reporteSeleccionado;
                rep.setTipo(txtTipo.getText());
                rep.setFechaGeneracion(LocalDateTime.parse(txtFechaGeneracion.getText()));
                rep.setDatosBrutos(txtDatosBrutos.getText());
                rep.setUsuarioId(Integer.parseInt(txtUsuarioId.getText()));
                if (modo.equals("NEW")) {
                    // Usamos la generación de reporte para setear los datos brutos
                    reporteServicio.generarReporte(rep);
                } else if (modo.equals("EDIT")) {
                    reporteServicio.actualizarReporte(rep);
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
        txtTipo.setDisable(!habilitar);
        txtFechaGeneracion.setDisable(!habilitar);
        txtDatosBrutos.setDisable(!habilitar);
        txtUsuarioId.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtTipo.clear();
        txtFechaGeneracion.clear();
        txtDatosBrutos.clear();
        txtUsuarioId.clear();
    }

    private void cargarInputs(Reporte rep) {
        txtTipo.setText(rep.getTipo());
        txtFechaGeneracion.setText(rep.getFechaGeneracion().toString());
        txtDatosBrutos.setText(rep.getDatosBrutos());
        txtUsuarioId.setText(String.valueOf(rep.getUsuarioId()));
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(reporteServicio.listarReportes());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar reportes: " + ex.getMessage());
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
