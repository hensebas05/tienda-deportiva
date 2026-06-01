# 📊 Modelo de Datos - DER

## Entidades

### CATEGORIAS
| Campo | Tipo | Restricción |
|-------|------|------------|
| id | BIGINT | PRIMARY KEY |
| nombre | VARCHAR(100) | UNIQUE, NOT NULL |
| descripcion | TEXT | NULL |

### PRODUCTOS
| Campo | Tipo | Restricción |
|-------|------|------------|
| id | BIGINT | PRIMARY KEY |
| nombre | VARCHAR(100) | NOT NULL |
| descripcion | TEXT | NULL |
| precio | DECIMAL(10,2) | NOT NULL |
| stock | INT | NOT NULL |
| categoria_id | BIGINT | FOREIGN KEY |
| sku | VARCHAR(50) | NULL |

### CLIENTES
| Campo | Tipo | Restricción |
|-------|------|------------|
| id | BIGINT | PRIMARY KEY |
| nombre | VARCHAR(100) | NOT NULL |
| email | VARCHAR(100) | UNIQUE, NOT NULL |
| telefono | VARCHAR(10) | NOT NULL |
| direccion | VARCHAR(255) | NOT NULL |

### VENTAS
| Campo | Tipo | Restricción |
|-------|------|------------|
| id | BIGINT | PRIMARY KEY |
| cliente_id | BIGINT | FOREIGN KEY |
| fecha | DATETIME | NOT NULL |
| total | DECIMAL(10,2) | NOT NULL |
| estado | VARCHAR(20) | NOT NULL |

### DETALLE_VENTAS
| Campo | Tipo | Restricción |
|-------|------|------------|
| id | BIGINT | PRIMARY KEY |
| venta_id | BIGINT | FOREIGN KEY |
| producto_id | BIGINT | FOREIGN KEY |
| cantidad | INT | NOT NULL |
| precio_unitario | DECIMAL(10,2) | NOT NULL |
| subtotal | DECIMAL(10,2) | NOT NULL |

## Relaciones

- CATEGORIAS (1) --- (N) PRODUCTOS
- PRODUCTOS (1) --- (N) DETALLE_VENTAS
- CLIENTES (1) --- (N) VENTAS
- VENTAS (1) --- (N) DETALLE_VENTAS

---

**Última actualización**: Junio 2026
