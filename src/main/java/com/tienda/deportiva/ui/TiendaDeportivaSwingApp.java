package com.tienda.deportiva.ui;

import javax.swing.*;
import java.awt.*;

public class TiendaDeportivaSwingApp extends JFrame {
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private ListadoProductosPanel listadoPanel;
    private FormularioProductoPanel formularioPanel;
    private BusquedaProductoPanel busquedaPanel;
    private DetalleProductoPanel detallePanel;

    public TiendaDeportivaSwingApp() {
        setTitle("🛍️ Tienda Deportiva - Gestión de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        listadoPanel = new ListadoProductosPanel(() -> mostrarPantalla("listado"));
        formularioPanel = new FormularioProductoPanel(() -> mostrarPantalla("listado"));
        busquedaPanel = new BusquedaProductoPanel();
        detallePanel = new DetalleProductoPanel();

        panelPrincipal.add(listadoPanel, "listado");
        panelPrincipal.add(formularioPanel, "formulario");
        panelPrincipal.add(busquedaPanel, "busqueda");
        panelPrincipal.add(detallePanel, "detalle");

        add(panelPrincipal, BorderLayout.CENTER);
        add(crearBarraNavegacion(), BorderLayout.NORTH);
    }

    private JPanel crearBarraNavegacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JButton btnListado = new JButton("📋 Listado");
        btnListado.addActionListener(e -> {
            mostrarPantalla("listado");
            listadoPanel.cargarProductos();
        });

        JButton btnNuevo = new JButton("➕ Nuevo Producto");
        btnNuevo.addActionListener(e -> {
            mostrarPantalla("formulario");
            formularioPanel.limpiar();
        });

        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> mostrarPantalla("busqueda"));

        JButton btnDetalle = new JButton("👁️ Detalle");
        btnDetalle.addActionListener(e -> {
            Long id = listadoPanel.getProductoSeleccionadoId();
            if (id != null) {
                detallePanel.cargarProducto(id);
                mostrarPantalla("detalle");
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto del listado", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnSalir = new JButton("🚪 Salir");
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas salir de la aplicación?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        panel.add(btnListado);
        panel.add(btnNuevo);
        panel.add(btnBuscar);
        panel.add(btnDetalle);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(btnSalir);

        return panel;
    }

    private void mostrarPantalla(String nombre) {
        cardLayout.show(panelPrincipal, nombre);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaDeportivaSwingApp());
    }
}
