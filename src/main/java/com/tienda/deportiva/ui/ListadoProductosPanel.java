package com.tienda.deportiva.ui;

import com.tienda.deportiva.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListadoProductosPanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private ApiProductoClient apiClient;
    private Runnable onActualizar;

    public ListadoProductosPanel(Runnable onActualizar) {
        this.onActualizar = onActualizar;
        this.apiClient = new ApiProductoClient();
        initComponents();
        cargarProductos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton btnRefrescar = new JButton("🔄 Refrescar");
        btnRefrescar.addActionListener(e -> cargarProductos());

        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                onActualizar.run();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para editar", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnEditar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarProductos() {
        try {
            modelo.setRowCount(0);
            List<Producto> productos = apiClient.obtenerTodos();
            for (Producto p : productos) {
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
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Long id = (Long) modelo.getValueAt(fila, 0);
        String nombre = (String) modelo.getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de eliminar a \"" + nombre + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                apiClient.eliminar(id);
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
