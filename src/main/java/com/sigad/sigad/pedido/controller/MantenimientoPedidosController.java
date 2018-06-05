/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.helpers.PedidoHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */

public class MantenimientoPedidosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    JFXPopup popup;
    @FXML
    JFXButton moreBtn;
    @FXML
    StackPane hiddenSp;
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, Integer> id = new JFXTreeTableColumn<>("ID");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> cliente = new JFXTreeTableColumn<>("Cliente");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> destino = new JFXTreeTableColumn<>("Destinatario");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> fecha = new JFXTreeTableColumn<>("Fecha");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> estado = new JFXTreeTableColumn<>("Estado");
    @FXML
    private JFXTreeTableView<PedidoOrdenLista> tablaPedidos;
    Pedido pedido;

    private final ObservableList<PedidoOrdenLista> pedidos = FXCollections.observableArrayList();
    public static final String viewPath = "/com/sigad/sigad/pedido/view/mantenimientoPedidos.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        columnas();
        agregarColumnas();
        cargarDatos();
    }

    public void columnas() {
        id.setPrefWidth(70);
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, Integer> param) -> param.getValue().getValue().id.asObject());

        cliente.setPrefWidth(300);
        cliente.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().cliente);

        destino.setPrefWidth(350);
        destino.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().direccion);
        fecha.setPrefWidth(100);
        fecha.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().fecha);

        estado.setPrefWidth(70);
        estado.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().estado);
    }

    public void cargarDatos() {
        pedidos.clear();
        PedidoHelper helper = new PedidoHelper();
        ArrayList<Pedido> peds = helper.getPedidos();
        peds.forEach((t) -> {
            pedidos.add(new PedidoOrdenLista(t));
        });
    }

    public void agregarColumnas() {
        final TreeItem<PedidoOrdenLista> rootPedido = new RecursiveTreeItem<>(pedidos, RecursiveTreeObject::getChildren);
        tablaPedidos.setEditable(true);
        tablaPedidos.getColumns().setAll(id, cliente, destino, fecha, estado);
        tablaPedidos.setRoot(rootPedido);
        tablaPedidos.setShowRoot(false);
//        tablaPedidos.setRowFactory(new Callback<TreeTableView<ProductoLista>, TreeTableRow<ProductoLista>>() {
//            @Override
//            public TreeTableRow<ProductoLista> call(TreeTableView<ProductoLista> param) {
//                TreeTableRow<ProductoLista> row = new TreeTableRow<>();
//                row.setOnMouseClicked((event) -> {
//                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                        ProductoLista rowData = row.getItem();
//                        mostrarInfoProducto(rowData.getCodigo());
//
//                    }
//                });
//                return row; //To change body of generated lambdas, choose Tools | Templates.
//            }
//        });

    }

    public void initPopup() {
        JFXButton edit = new JFXButton("Editar");

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {

                } catch (Exception ex) {

                }
            }
        });

        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);

        VBox vBox = new VBox(edit);

        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }

    @FXML
    void crearPedido(MouseEvent event) {
        try {
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(SeleccionarProductosController.viewPath));
            node = (Node) loader.load();
            SeleccionarProductosController sel = loader.getController();
            sel.initModel(hiddenSp);
            hiddenSp.getChildren().setAll(node);
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void handleAction(Event event) {

        if (event.getSource() == moreBtn) {
            int count = tablaPedidos.getSelectionModel().getSelectedCells().size();
            if (count > 1) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "Ok", hiddenSp);
            } else if (count <= 0) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", hiddenSp);
            } else {
                int selected = tablaPedidos.getSelectionModel().getSelectedIndex();
                pedido = tablaPedidos.getSelectionModel().getModelItem(selected).getValue().pedido;
                initPopup();
                popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }

        }
    }

    class PedidoOrdenLista extends RecursiveTreeObject<PedidoOrdenLista> {

        IntegerProperty id;
        StringProperty cliente;
        StringProperty direccion;
        StringProperty fecha;
        StringProperty estado;
        Pedido pedido;

        public PedidoOrdenLista(Pedido pedido) {
            this.pedido = pedido;
            this.id = new SimpleIntegerProperty(pedido.getId().intValue());
            this.cliente = new SimpleStringProperty(pedido.getCliente().toString());
            this.direccion = new SimpleStringProperty(pedido.getDireccionDeEnvio());
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            this.fecha = new SimpleStringProperty(f.format(pedido.getFechaVenta()));
            this.estado = new SimpleStringProperty(pedido.getEstado().getNombre());

        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PedidoOrdenLista) {
                PedidoOrdenLista pl = (PedidoOrdenLista) o;
                return pl.pedido.getId().equals(this.pedido.getId());
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + Objects.hashCode(this.pedido.getId());
            return hash;
        }

    }

}