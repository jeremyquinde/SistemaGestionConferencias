package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Inscripcion;
import com.conferencias.negocio.servicios.InscripcionServicio;
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

public class InscripcionesCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private InscripcionServicio inscripcionServicio;
    private TableView<Inscripcion> table;
    private ObservableList<Inscripcion> datos;

    // Campos del formulario
    private TextField txtUsuarioId;
    private TextField txtEventoId;
    private TextField txtFechaInscripcion;
    private TextField txtEstado;

    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;
    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Inscripcion inscripcionSeleccionada;

    public InscripcionesCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.inscripcionServicio = new InscripcionServicio();
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

        // Primera fila: Etiquetas
        Label lblUsuarioId = new Label("ID Usuario:");
        Label lblEventoId = new Label("ID Evento:");
        lblUsuarioId.setFont(Font.font(14));
        lblEventoId.setFont(Font.font(14));
        formGrid.add(lblUsuarioId, 0, 0);
        formGrid.add(lblEventoId, 1, 0);

        // Segunda fila: Inputs
        txtUsuarioId = new TextField();
        txtEventoId = new TextField();
        txtUsuarioId.setPrefWidth(300);
        txtEventoId.setPrefWidth(300);
        formGrid.add(txtUsuarioId, 0, 1);
        formGrid.add(txtEventoId, 1, 1);

        // Tercera fila: Etiquetas
        Label lblFechaInscripcion = new Label("Fecha Inscripción (ISO):");
        Label lblEstado = new Label("Estado:");
        lblFechaInscripcion.setFont(Font.font(14));
        lblEstado.setFont(Font.font(14));
        formGrid.add(lblFechaInscripcion, 0, 2);
        formGrid.add(lblEstado, 1, 2);

        // Cuarta fila: Inputs
        txtFechaInscripcion = new TextField();
        txtEstado = new TextField();
        txtFechaInscripcion.setPrefWidth(300);
        txtEstado.setPrefWidth(300);
        formGrid.add(txtFechaInscripcion, 0, 3);
        formGrid.add(txtEstado, 1, 3);

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
        TableColumn<Inscripcion, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        TableColumn<Inscripcion, Integer> colUsuario = new TableColumn<>("ID Usuario");
        colUsuario.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getUsuarioId()).asObject());
        TableColumn<Inscripcion, Integer> colEvento = new TableColumn<>("ID Evento");
        colEvento.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getEventoId()).asObject());
        TableColumn<Inscripcion, String> colFecha = new TableColumn<>("Fecha Inscripción");
        colFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFechaInscripcion().toString()));
        TableColumn<Inscripcion, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEstado()));
        table.getColumns().addAll(colId, colUsuario, colEvento, colFecha, colEstado);

        // Listener para habilitar botones de edición y eliminación
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                inscripcionSeleccionada = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                inscripcionSeleccionada = null;
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
            if (inscripcionSeleccionada != null) {
                modo = "EDIT";
                cargarInputs(inscripcionSeleccionada);
                habilitarInputs(true);
            }
        });
        btnEliminar.setOnAction(e -> {
            if (inscripcionSeleccionada != null) {
                try {
                    inscripcionServicio.eliminarInscripcion(inscripcionSeleccionada.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });
        btnGuardar.setOnAction(e -> {
            try {
                Inscripcion insc = modo.equals("NEW") ? new Inscripcion() : inscripcionSeleccionada;
                insc.setUsuarioId(Integer.parseInt(txtUsuarioId.getText()));
                insc.setEventoId(Integer.parseInt(txtEventoId.getText()));
                insc.setFechaInscripcion(LocalDateTime.parse(txtFechaInscripcion.getText()));
                insc.setEstado(txtEstado.getText());
                if (modo.equals("NEW")) {
                    inscripcionServicio.inscribir(insc);
                } else if (modo.equals("EDIT")) {
                    inscripcionServicio.actualizarInscripcion(insc);
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
        txtUsuarioId.setDisable(!habilitar);
        txtEventoId.setDisable(!habilitar);
        txtFechaInscripcion.setDisable(!habilitar);
        txtEstado.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtUsuarioId.clear();
        txtEventoId.clear();
        txtFechaInscripcion.clear();
        txtEstado.clear();
    }

    private void cargarInputs(Inscripcion insc) {
        txtUsuarioId.setText(String.valueOf(insc.getUsuarioId()));
        txtEventoId.setText(String.valueOf(insc.getEventoId()));
        txtFechaInscripcion.setText(insc.getFechaInscripcion().toString());
        txtEstado.setText(insc.getEstado());
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(inscripcionServicio.listarInscripciones());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar inscripciones: " + ex.getMessage());
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
