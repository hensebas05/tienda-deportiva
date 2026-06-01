package com.tienda.deportiva.ui;

import com.tienda.deportiva.model.Producto;
import com.tienda.deportiva.model.Categoria;

import javax.swing.*;
import java.awt.*;

public class FormularioProductoPanel extends JPanel {
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtCategoria;
    private JTextField txtSku;
    private ApiProductoClient apiClient;
    private Runnable onGuardar;
    private Long productoId;

    public FormularioProductoPanel(Runnable onGuardar) {
        this.onGuardar = onGuardar;
        this.apiClient = new ApiProductoClient();
        this.productoId = null;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextArea(3, 20);
        panelFormulario.add(new JScrollPane(txtDescripcion), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(20);
        panelFormulario.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(20);
        panelFormulario.add(txtStock, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelFormulario.add(new JLabel("Categoría ID:"), gbc);
        gbc.gridx = 1;
        txtCategoria = new JTextField(20);
        panelFormulario.add(txtCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panelFormulario.add(new JLabel("SKU:"), gbc);
        gbc.gridx = 1;
        txtSku = new JTextField(20);
        panelFormulario.add(txtSku, gbc);

        add(new JScrollPane(panelFormulario), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.addActionListener(e -> guardar());
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.addActionListener(e -> limpiar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarProducto(Long id) {
        try {
            Producto p = apiClient.obtenerPorId(id);
            this.productoId = p.getId();
            txtNombre.setText(p.getNombre());
            txtDescripcion.setText(p.getDescripcion());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtStock.setText(String.valueOf(p.getStock()));
            if (p.getCategoria() != null) {
                txtCategoria.setText(String.valueOf(p.getCategoria().getId()));
            }
            txtSku.setText(p.getSku());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El precio es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtStock.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El stock es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto producto = new Producto();
            producto.setNombre(txtNombre.getText());
            producto.setDescripcion(txtDescripcion.getText());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText()));
            producto.setStock(Integer.parseInt(txtStock.getText()));
            producto.setSku(txtSku.getText());

            if (!txtCategoria.getText().isEmpty()) {
                Categoria cat = new Categoria();
                cat.setId(Long.parseLong(txtCategoria.getText()));
                producto.setCategoria(cat);
            }

            if (productoId == null) {
                apiClient.crear(producto);
                JOptionPane.showMessageDialog(this, "Producto creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                apiClient.actualizar(productoId, producto);
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            limpiar();
            onGuardar.run();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifica que precio y stock sean números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiar() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCategoria.setText("");
        txtSku.setText("");
        productoId = null;
    }
}
