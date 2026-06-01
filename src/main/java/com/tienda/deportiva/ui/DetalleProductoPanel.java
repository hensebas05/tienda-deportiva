package com.tienda.deportiva.ui;

import com.tienda.deportiva.model.Producto;

import javax.swing.*;
import java.awt.*;

public class DetalleProductoPanel extends JPanel {
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblDescripcion;
    private JLabel lblPrecio;
    private JLabel lblStock;
    private JLabel lblCategoria;
    private JLabel lblSku;
    private JLabel lblEstado;
    private ApiProductoClient apiClient;

    public DetalleProductoPanel() {
        this.apiClient = new ApiProductoClient();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelDetalle = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDetalle.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        lblId = new JLabel();
        lblId.setFont(new Font("Arial", Font.BOLD, 12));
        panelDetalle.add(lblId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelDetalle.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        lblNombre = new JLabel();
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        panelDetalle.add(lblNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelDetalle.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        lblDescripcion = new JLabel();
        panelDetalle.add(lblDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelDetalle.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        lblPrecio = new JLabel();
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrecio.setForeground(new Color(0, 128, 0));
        panelDetalle.add(lblPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelDetalle.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        lblStock = new JLabel();
        lblStock.setFont(new Font("Arial", Font.BOLD, 12));
        panelDetalle.add(lblStock, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panelDetalle.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        lblCategoria = new JLabel();
        panelDetalle.add(lblCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panelDetalle.add(new JLabel("SKU:"), gbc);
        gbc.gridx = 1;
        lblSku = new JLabel();
        panelDetalle.add(lblSku, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panelDetalle.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        lblEstado = new JLabel();
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        panelDetalle.add(lblEstado, gbc);

        add(new JScrollPane(panelDetalle), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.addActionListener(e -> {
            if (lblId.getText() != null && !lblId.getText().isEmpty()) {
                try {
                    Long id = Long.parseLong(lblId.getText());
                    cargarProducto(id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarProducto(Long id) {
        try {
            Producto p = apiClient.obtenerPorId(id);
            lblId.setText(String.valueOf(p.getId()));
            lblNombre.setText(p.getNombre());
            lblDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "Sin descripción");
            lblPrecio.setText(String.format("$%.2f", p.getPrecio()));
            lblStock.setText(String.valueOf(p.getStock()) + " unidades");
            lblCategoria.setText(p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría");
            lblSku.setText(p.getSku() != null ? p.getSku() : "N/A");
            
            if (p.getStock() == 0) {
                lblEstado.setText("❌ Agotado");
                lblEstado.setForeground(new Color(255, 0, 0));
            } else if (p.getStock() < 10) {
                lblEstado.setText("⚠️ Stock bajo");
                lblEstado.setForeground(new Color(255, 165, 0));
            } else {
                lblEstado.setText("✅ Disponible");
                lblEstado.setForeground(new Color(0, 128, 0));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiar() {
        lblId.setText("");
        lblNombre.setText("");
        lblDescripcion.setText("");
        lblPrecio.setText("");
        lblStock.setText("");
        lblCategoria.setText("");
        lblSku.setText("");
        lblEstado.setText("");
    }
}
