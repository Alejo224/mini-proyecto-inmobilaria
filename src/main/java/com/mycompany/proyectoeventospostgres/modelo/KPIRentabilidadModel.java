package com.mycompany.proyectoeventospostgres.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class KPIRentabilidadModel {

    private final ConexionBD conexionBD = new ConexionBD();

    public KPIRentabilidadModel(){}

    //COMISION DEL AGENTE DE SU PRIMER ARRIENDO
    public boolean comisionAgentes(JTable jTable) {
        String sql = "SELECT \n" +
                "    ac.nombre_completo,\n" +
                "    ar.fecha_registro,\n" +
                "    ar.comision_agente,\n" +
                "    ar.comision_inmobiliaria\n" +
                "FROM agente_comercial ac\n" +
                "JOIN (\n" +
                "    SELECT fk_agente, MIN(fecha_registro) AS primera_fecha\n" +
                "    FROM arriendo\n" +
                "    GROUP BY fk_agente\n" +
                ") primera ON ac.cedula = primera.fk_agente\n" +
                "JOIN arriendo ar \n" +
                "    ON ar.fk_agente = primera.fk_agente \n" +
                "   AND ar.fecha_registro = primera.primera_fecha;";

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nombre agente");
        model.addColumn("Fecha registro");
        model.addColumn("Comisión agente");
        model.addColumn("Comisión inmobiliaria");

        if (jTable == null) {
            jTable = new JTable();
        }

        boolean hayResultados = false;

        try (PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hayResultados = true;
                model.addRow(new Object[]{
                        rs.getString("nombre_completo"),
                        rs.getTimestamp("fecha_registro"),
                        rs.getBigDecimal("comision_agente"),
                        rs.getBigDecimal("comision_inmobiliaria")
                });
            }

            jTable.setModel(model);
            return hayResultados;

        } catch (SQLException e) {
            System.out.println("Error al obtener comisiones: " + e.getMessage());
            return false;
        }
    }

    //comision de la inmobilaria
    public void kpiInmobilaria(JLabel lblTotalGanancia, JLabel lblCantidadArriendos, JLabel lblPromedio,
    JLabel lblPrimerArriendo, JLabel lblUltimoArriendo){

        String sql = "SELECT " +
                "SUM(comision_inmobiliaria) AS total_ganancia_inmobiliaria, " +
                "COUNT(id_arriendo) AS cantidad_arriendos, " +
                "ROUND(AVG(comision_inmobiliaria), 2) AS promedio_comision_arriendo, " +
                "MIN(fecha_registro) AS fecha_primer_arriendo, " +
                "MAX(fecha_registro) AS fecha_ultimo_arriendo " +
                "FROM arriendo WHERE activo = 1";

        try (PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lblTotalGanancia.setText("Ganancia total: $" + rs.getBigDecimal("total_ganancia_inmobiliaria"));
                lblCantidadArriendos.setText("Cantidad de arriendos: " + rs.getInt("cantidad_arriendos"));
                lblPromedio.setText("Promedio por arriendo: $" + rs.getBigDecimal("promedio_comision_arriendo"));
                lblPrimerArriendo.setText("Primer arriendo: " + rs.getDate("fecha_primer_arriendo"));
                lblUltimoArriendo.setText("Último arriendo: " + rs.getDate("fecha_ultimo_arriendo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //INMUEBLES MÁS RENTADOS
    public boolean inmueblesMasRentados(JTable jTable) {
        String sql = "SELECT \n" +
                "    i.codigo_inmueble,\n" +
                "    i.descripcion,\n" +
                "    COUNT(a.id_arriendo) AS veces_arrendado,\n" +
                "    SUM(a.monto_mensual) AS renta_total,\n" +
                "    ROUND(AVG(a.monto_mensual), 2) AS renta_promedio\n" +
                "FROM arriendo a \n" +
                "JOIN inmueble i ON a.codigo_inmueble = i.codigo_inmueble\n" +
                "WHERE a.activo = 1\n" +
                "GROUP BY i.codigo_inmueble, i.descripcion\n" +
                "ORDER BY renta_total DESC\n" +
                "LIMIT 5;";

        DefaultTableModel model = new DefaultTableModel();

        // Columnas correctas según la consulta
        model.addColumn("Código Inmueble");
        model.addColumn("Descripción");
        model.addColumn("Veces Arrendado");
        model.addColumn("Renta Total");
        model.addColumn("Renta Promedio");

        if (jTable == null) {
            jTable = new JTable();
        }

        boolean hayResultados = false;

        try (PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hayResultados = true;
                model.addRow(new Object[]{
                        rs.getInt("codigo_inmueble"),
                        rs.getString("descripcion"),
                        rs.getInt("veces_arrendado"),
                        rs.getBigDecimal("renta_total"),
                        rs.getBigDecimal("renta_promedio")
                });
            }

            jTable.setModel(model);
            return hayResultados;

        } catch (SQLException e) {
            System.out.println("Error al obtener los inmuebles más rentados: " + e.getMessage());
            return false;
        }
    }
}
