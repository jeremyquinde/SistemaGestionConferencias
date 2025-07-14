package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Evento;
import com.conferencias.negocio.servicios.EventoServicio;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventosCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private EventoServicio eventoServicio;
    private TableView<Evento> table;
    private ObservableList<Evento> datos;

    // Campos del formulario
    private TextField txtNombre;
    private DatePicker dpFechaInicio;
    private DatePicker dpFechaFin;
    private TextField txtCapacidad;
    private TextField txtEstado;
    private TextField txtUbicacionId;

    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;

    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Evento eventoSeleccionado;

    public EventosCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.eventoServicio = new EventoServicio();
        crearInterfaz();
        cargarDatos();
        configurarEstadoInicial();
    }

    private void crearInterfaz() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // ---------- FORMULARIO ----------
        // Creamos un GridPane para el formulario con 3 columnas
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        // Grupo 1 (Fila 0 y 1)
        Label lblNombre = new Label("Nombre:");
        Label lblFechaInicio = new Label("Fecha Inicio:");
        Label lblFechaFin = new Label("Fecha Fin:");
        lblNombre.setFont(Font.font(14));
        lblFechaInicio.setFont(Font.font(14));
        lblFechaFin.setFont(Font.font(14));
        formGrid.add(lblNombre, 0, 0);
        formGrid.add(lblFechaInicio, 1, 0);
        formGrid.add(lblFechaFin, 2, 0);

        txtNombre = new TextField();
        dpFechaInicio = new DatePicker();
        dpFechaFin = new DatePicker();
        txtNombre.setPrefWidth(300);
        dpFechaInicio.setPrefWidth(300);
        dpFechaFin.setPrefWidth(300);
        formGrid.add(txtNombre, 0, 1);
        formGrid.add(dpFechaInicio, 1, 1);
        formGrid.add(dpFechaFin, 2, 1);

        // Grupo 2 (Fila 2 y 3)
        Label lblCapacidad = new Label("Capacidad:");
        Label lblEstado = new Label("Estado:");
        Label lblUbicacionId = new Label("ID Ubicación:");
        lblCapacidad.setFont(Font.font(14));
        lblEstado.setFont(Font.font(14));
        lblUbicacionId.setFont(Font.font(14));
        formGrid.add(lblCapacidad, 0, 2);
        formGrid.add(lblEstado, 1, 2);
        formGrid.add(lblUbicacionId, 2, 2);

        txtCapacidad = new TextField();
        txtEstado = new TextField();
        txtUbicacionId = new TextField();
        txtCapacidad.setPrefWidth(300);
        txtEstado.setPrefWidth(300);
        txtUbicacionId.setPrefWidth(300);
        formGrid.add(txtCapacidad, 0, 3);
        formGrid.add(txtEstado, 1, 3);
        formGrid.add(txtUbicacionId, 2, 3);

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

        // Agrupamos botones (formulario y CRUD) en un VBox
        VBox vboxControls = new VBox(10, formGrid, hboxFormButtons, hboxCrud);
        vboxControls.setAlignment(Pos.CENTER);
        vboxControls.setPadding(new Insets(10));

        // ---------- TABLA ----------
        table = new TableView<>();
        table.setPrefHeight(300);
        TableColumn<Evento, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        TableColumn<Evento, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        TableColumn<Evento, String> colInicio = new TableColumn<>("Fecha Inicio");
        colInicio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaInicio().toString()));
        TableColumn<Evento, String> colFin = new TableColumn<>("Fecha Fin");
        colFin.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaFin().toString()));
        TableColumn<Evento, Integer> colCapacidad = new TableColumn<>("Capacidad");
        colCapacidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacidad()).asObject());
        TableColumn<Evento, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstado()));
        TableColumn<Evento, Integer> colUbicacion = new TableColumn<>("ID Ubicación");
        colUbicacion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getUbicacionId()).asObject());
        table.getColumns().addAll(colId, colNombre, colInicio, colFin, colCapacidad, colEstado, colUbicacion);

        // Agregamos el listener a la tabla para habilitar/deshabilitar botones
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                eventoSeleccionado = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                eventoSeleccionado = null;
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
            }
        });

        // ---------- ACCIONES DE BOTONES ----------

        // Nuevo: Limpia el formulario y habilita inputs
        btnNuevo.setOnAction(e -> {
            modo = "NEW";
            limpiarInputs();
            habilitarInputs(true);
        });

        // Editar: Carga los datos de la fila seleccionada
        btnEditar.setOnAction(e -> {
            if (eventoSeleccionado != null) {
                modo = "EDIT";
                cargarInputs(eventoSeleccionado);
                habilitarInputs(true);
            }
        });

        // Eliminar: Elimina el registro seleccionado
        btnEliminar.setOnAction(e -> {
            if (eventoSeleccionado != null) {
                try {
                    eventoServicio.eliminarEvento(eventoSeleccionado.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });

        // Guardar: Guarda el nuevo registro o actualiza el existente
        btnGuardar.setOnAction(e -> {
            try {
                // Recopilamos datos sin parsear:
                String nombre         = txtNombre.getText();
                LocalDate inicio      = dpFechaInicio.getValue();
                LocalDate fin         = dpFechaFin.getValue();
                String capacidadStr   = txtCapacidad.getText();
                String estado         = txtEstado.getText();
                String ubicacionIdStr = txtUbicacionId.getText();

                if (modo.equals("NEW")) {
                    eventoServicio.crearEvento(
                            nombre, inicio, fin,
                            capacidadStr, estado, ubicacionIdStr
                    );
                }
                else {
                    // incluye el id si es edición
                    eventoServicio.actualizarEvento(
                            eventoSeleccionado.getId(),
                            nombre, inicio, fin,
                            capacidadStr, estado, ubicacionIdStr
                    );
                }

                cargarDatos();
                limpiarInputs();
                habilitarInputs(false);

            } catch (Exception ex) {
                mostrarAlerta("Error al guardar el evento: " + ex.getMessage());
            }
        });



        // Cancelar: Limpia y deshabilita el formulario
        btnCancelar.setOnAction(e -> {
            limpiarInputs();
            habilitarInputs(false);
        });

        // Volver: Regresa al menú principal
        btnVolver.setOnAction(e -> mainApp.mostrarMenuPrincipal());

        // ---------- MONTAJE DEL LAYOUT ----------
        VBox vboxPrincipal = new VBox(20, vboxControls, table);
        vboxPrincipal.setAlignment(Pos.CENTER);
        root.setCenter(vboxPrincipal);

        // Tamaño de la ventana: 1280 x 720
        scene = new Scene(root, 1280, 720);
    }

    private void configurarEstadoInicial() {
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        habilitarInputs(false);
    }

    private void habilitarInputs(boolean habilitar) {
        txtNombre.setDisable(!habilitar);
        dpFechaInicio.setDisable(!habilitar);
        dpFechaFin.setDisable(!habilitar);
        txtCapacidad.setDisable(!habilitar);
        txtEstado.setDisable(!habilitar);
        txtUbicacionId.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtNombre.clear();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
        txtCapacidad.clear();
        txtEstado.clear();
        txtUbicacionId.clear();
    }

    private void cargarInputs(Evento evento) {
        txtNombre.setText(evento.getNombre());
        dpFechaInicio.setValue(evento.getFechaInicio().toLocalDate());
        dpFechaFin.setValue(evento.getFechaFin().toLocalDate());
        txtCapacidad.setText(String.valueOf(evento.getCapacidad()));
        txtEstado.setText(evento.getEstado());
        txtUbicacionId.setText(String.valueOf(evento.getUbicacionId()));
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(eventoServicio.listarEventos());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar eventos: " + ex.getMessage());
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
