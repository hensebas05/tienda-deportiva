package com.tienda.deportiva.ui;

import com.tienda.deportiva.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BusquedaProductoPanel extends JPanel {
    private JTextField txtBusqueda;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ApiProductoClient apiClient;

    public BusquedaProductoPanel() {
        this.apiClient = new ApiProductoClient();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.add(new JLabel("Buscar por nombre:"));
        txtBusqueda = new JTextField(20);
        panelBusqueda.add(txtBusqueda);

        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> realizarBusqueda());
        panelBusqueda.add(btnBuscar);

        JButton btnLimpiar = new JButton("🗑️ Limpiar");
        btnLimpiar.addActionListener(e -> {
            txtBusqueda.setText("");
            modelo.setRowCount(0);
        });
        panelBusqueda.add(btnLimpiar);

        add(panelBusqueda, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInfo = new JPanel();
        panelInfo.add(new JLabel("Ingresa un nombre y haz clic en Buscar"));
        add(panelInfo, BorderLayout.SOUTH);
    }

    private void realizarBusqueda() {
        String termino = txtBusqueda.getText().trim();
        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un término de búsqueda", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            modelo.setRowCount(0);
            List<Producto> resultados = apiClient.buscar(termino);

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron productos", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Producto p : resultados) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        String.format("$%.2f", p.getPrecio()),
                        p.getStock(),
                        p.getCategoria() != null ? p.getCategoria().getNombre() : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Long getProductoSeleccionadoId() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            return (Long) modelo.getValueAt(fila, 0);
        }
        return null;
    }
}
