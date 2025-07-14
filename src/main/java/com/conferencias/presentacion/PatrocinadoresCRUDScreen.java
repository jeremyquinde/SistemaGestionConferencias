package com.conferencias.presentacion;

import com.conferencias.Main;
import com.conferencias.datos.entidades.Patrocinador;
import com.conferencias.negocio.servicios.PatrocinadorServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.sql.SQLException;

public class PatrocinadoresCRUDScreen {

    private Main mainApp;
    private Scene scene;
    private PatrocinadorServicio patrocinadorServicio;
    private TableView<Patrocinador> table;
    private ObservableList<Patrocinador> datos;

    // Campos del formulario
    private TextField txtNombre;
    private TextField txtNivel;
    private TextField txtAporteEconomico;
    private TextField txtEventoId;

    // Botones del formulario
    private Button btnGuardar;
    private Button btnCancelar;
    // Botones CRUD generales
    private Button btnNuevo;
    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private String modo; // "NEW" o "EDIT"
    private Patrocinador patrocinadorSeleccionado;

    public PatrocinadoresCRUDScreen(Main mainApp) {
        this.mainApp = mainApp;
        this.patrocinadorServicio = new PatrocinadorServicio();
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

        // Etiquetas en una fila
        Label lblNombre = new Label("Nombre:");
        Label lblNivel = new Label("Nivel:");
        Label lblAporte = new Label("Aporte Económico:");
        Label lblEventoId = new Label("ID Evento:");
        lblNombre.setFont(Font.font(14));
        lblNivel.setFont(Font.font(14));
        lblAporte.setFont(Font.font(14));
        lblEventoId.setFont(Font.font(14));
        formGrid.add(lblNombre, 0, 0);
        formGrid.add(lblNivel, 1, 0);
        formGrid.add(lblAporte, 2, 0);
        formGrid.add(lblEventoId, 3, 0);

        // Inputs en una fila
        txtNombre = new TextField();
        txtNivel = new TextField();
        txtAporteEconomico = new TextField();
        txtEventoId = new TextField();
        txtNombre.setPrefWidth(300);
        txtNivel.setPrefWidth(300);
        txtAporteEconomico.setPrefWidth(300);
        txtEventoId.setPrefWidth(300);
        formGrid.add(txtNombre, 0, 1);
        formGrid.add(txtNivel, 1, 1);
        formGrid.add(txtAporteEconomico, 2, 1);
        formGrid.add(txtEventoId, 3, 1);

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
        TableColumn<Patrocinador, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        TableColumn<Patrocinador, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));
        TableColumn<Patrocinador, String> colNivel = new TableColumn<>("Nivel");
        colNivel.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNivel()));
        TableColumn<Patrocinador, Double> colAporte = new TableColumn<>("Aporte Económico");
        colAporte.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getAporteEconomico()).asObject());
        TableColumn<Patrocinador, Integer> colEvento = new TableColumn<>("ID Evento");
        colEvento.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getEventoId()).asObject());
        table.getColumns().addAll(colId, colNombre, colNivel, colAporte, colEvento);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                patrocinadorSeleccionado = newSel;
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                patrocinadorSeleccionado = null;
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
            if (patrocinadorSeleccionado != null) {
                modo = "EDIT";
                cargarInputs(patrocinadorSeleccionado);
                habilitarInputs(true);
            }
        });
        btnEliminar.setOnAction(e -> {
            if (patrocinadorSeleccionado != null) {
                try {
                    patrocinadorServicio.eliminarPatrocinador(patrocinadorSeleccionado.getId());
                    cargarDatos();
                    limpiarInputs();
                } catch (SQLException ex) {
                    mostrarAlerta("Error al eliminar: " + ex.getMessage());
                }
            }
        });
        btnGuardar.setOnAction(e -> {
            try {
                Patrocinador p = (modo.equals("NEW")) ? new Patrocinador() : patrocinadorSeleccionado;
                p.setNombre(txtNombre.getText());
                p.setNivel(txtNivel.getText());
                p.setAporteEconomico(Double.parseDouble(txtAporteEconomico.getText()));
                p.setEventoId(Integer.parseInt(txtEventoId.getText()));
                if (modo.equals("NEW")) {
                    patrocinadorServicio.registrarPatrocinador(p);
                } else if (modo.equals("EDIT")) {
                    patrocinadorServicio.actualizarPatrocinador(p);
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
        txtNivel.setDisable(!habilitar);
        txtAporteEconomico.setDisable(!habilitar);
        txtEventoId.setDisable(!habilitar);
        btnGuardar.setDisable(!habilitar);
        btnCancelar.setDisable(!habilitar);
    }

    private void limpiarInputs() {
        txtNombre.clear();
        txtNivel.clear();
        txtAporteEconomico.clear();
        txtEventoId.clear();
    }

    private void cargarInputs(Patrocinador p) {
        txtNombre.setText(p.getNombre());
        txtNivel.setText(p.getNivel());
        txtAporteEconomico.setText(String.valueOf(p.getAporteEconomico()));
        txtEventoId.setText(String.valueOf(p.getEventoId()));
    }

    private void cargarDatos() {
        try {
            datos = FXCollections.observableArrayList(patrocinadorServicio.listarPatrocinadores());
            table.setItems(datos);
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar patrocinadores: " + ex.getMessage());
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
