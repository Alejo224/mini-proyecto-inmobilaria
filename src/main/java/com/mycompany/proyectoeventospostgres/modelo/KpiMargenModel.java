/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.modelo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nico
 */
public class KpiMargenModel {
    private final ConexionBD conexionBD = new ConexionBD();
    private int codigoInmueble, cedulaAgente;
    private BigDecimal precioPropietario, montoMensual, margen;
    
    
    public KpiMargenModel(){}
    
    public KpiMargenModel(int codigoInmueble, int cedulaAgente, BigDecimal precioPropietario, BigDecimal montoMensual, BigDecimal margen){
        this.codigoInmueble = codigoInmueble;
        this.cedulaAgente = cedulaAgente;
        this.precioPropietario = precioPropietario;
        this.montoMensual = montoMensual;
        this.margen = margen;
    }
    
    public ConexionBD getConexionBD() {
        return conexionBD;
    }

    public int getCodigoInmueble() {
        return codigoInmueble;
    }

    public int getCedulaAgente() {
        return cedulaAgente;
    }

    public BigDecimal getPrecioPropietario() {
        return precioPropietario;
    }

    public BigDecimal getMontoMensual() {
        return montoMensual;
    }

    public BigDecimal getMargen() {
        return margen;
    }

    public void setCodigoInmueble(int codigoInmueble) {
        this.codigoInmueble = codigoInmueble;
    }

    public void setCedulaAgente(int cedulaAgente) {
        this.cedulaAgente = cedulaAgente;
    }

    public void setPrecioPropietario(BigDecimal precioPropietario) {
        this.precioPropietario = precioPropietario;
    }

    public void setMontoMensual(BigDecimal montoMensual) {
        this.montoMensual = montoMensual;
    }

    public void setMargen(BigDecimal margen) {
        this.margen = margen;
    }
    
    public void mostrar(JTable tablatotal){
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Codigo inmueble");
        modelo.addColumn("Precio del propietario");
        modelo.addColumn("Monto mensual");
        modelo.addColumn("Margen");
        modelo.addColumn("Nombre Agente");
        
        if (tablatotal == null){
            tablatotal = new JTable();
        }

        tablatotal.setModel(modelo);
        String consulta = "SELECT i.codigo_inmueble, i.precio_propietario, a.monto_mensual, (i.precio_propietario - a.monto_mensual) AS margen, a.fk_agente as cedula_agente FROM inmobilaria.inmueble i JOIN inmobilaria.arriendo a ON i.codigo_inmueble = a.codigo_inmueble;";
        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){

                setCodigoInmueble(rs.getInt("codigo_inmueble"));
                setPrecioPropietario(rs.getBigDecimal("precio_propietario"));
                setMontoMensual(rs.getBigDecimal("monto_mensual"));
                setMargen(rs.getBigDecimal("margen"));
                setCedulaAgente(rs.getInt("cedula_agente"));

                modelo.addRow(new Object[]{ getCodigoInmueble(),
                        getPrecioPropietario(), getMontoMensual(),
                        getMargen(),getCedulaAgente()});
            }
            tablatotal.setModel(modelo);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }
}


