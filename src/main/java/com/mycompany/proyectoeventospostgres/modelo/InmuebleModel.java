/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.modelo;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nico
 */
public class InmuebleModel {
    private final ConexionBD conexionBD = new ConexionBD();
    private int codigoInmueble;
    private String descripcion;
    private BigDecimal precioPropietario;
    private int cedulaPropietario;

    public InmuebleModel(){
    }
    
    public InmuebleModel(int codigo_inmueble, String descripcion, BigDecimal precio_propietario, int fk_cedula_propietario){
        this.codigoInmueble = codigo_inmueble;
        this.descripcion = descripcion;
        this.precioPropietario = precio_propietario;
        this.cedulaPropietario = fk_cedula_propietario;
    }

    public ConexionBD getConexionBD() {
        return conexionBD;
    }

    public int getCodigoInmueble() {
        return codigoInmueble;
    }

    public void setCodigoInmueble(int codigoInmueble) {
        this.codigoInmueble = codigoInmueble;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioPropietario() {
        return precioPropietario;
    }

    public void setPrecioPropietario(BigDecimal precioPropietario) {
        this.precioPropietario = precioPropietario;
    }

    public int getCedulaPropietario() {
        return cedulaPropietario;
    }

    public void setCedulaPropietario(int cedulaPropietario) {
        this.cedulaPropietario = cedulaPropietario;
    }

//-----------------------------------------------------------------------    
    
    public void mostrar(JTable tablatotal){

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Codigo inmueble");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Precio del propietario");
        modelo.addColumn("cedula propietario");

        if (tablatotal == null){
            tablatotal = new JTable();
        }

        tablatotal.setModel(modelo);
        String consulta = "SELECT * FROM inmueble;";

        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){

                setCodigoInmueble(rs.getInt("codigo_inmueble"));
                setDescripcion(rs.getString("descripcion"));
                setPrecioPropietario(rs.getBigDecimal("precio_propietario"));
                setCedulaPropietario(rs.getInt("fk_cedula_propietario"));

                modelo.addRow(new Object[]{ getCodigoInmueble(),
                        getDescripcion(), getPrecioPropietario(),
                        getCedulaPropietario()});
            }
            tablatotal.setModel(modelo);
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
//            conexionBD.ConnectionClosed();
        }
    }
    
    public void agregar(JTextField codigo_inmueble,
                        JTextField descripcion,
                        JTextField precio_propietario,
                        JComboBox fk_cedula_propietario ) throws SQLException, NumberFormatException{

        String consulta = "insert into inmueble (codigo_inmueble,descripcion, precio_propietario, fk_cedula_propietario) values (?,?,?,?);";
        System.out.println("entro a crear");
        setCodigoInmueble(Integer.parseInt(codigo_inmueble.getText()));
        setDescripcion(descripcion.getText());
        setPrecioPropietario(new BigDecimal(precio_propietario.getText()));
        setCedulaPropietario(Integer.parseInt(fk_cedula_propietario.getSelectedItem().toString()));

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCodigoInmueble());
        cs.setString(2, getDescripcion());
        cs.setBigDecimal(3, getPrecioPropietario());
        cs.setInt(4, getCedulaPropietario());
        cs.execute();

        JOptionPane.showMessageDialog(null, "Se guardó correctamente");

    }
    
    public void seleccionar(JTable tabla_inmueble, JTextField codigo_inmueble,
                        JTextField descripcion,
                        JTextField precio_propietario,
                        JComboBox fk_cedula_propietario){
        try{
            int fila = tabla_inmueble.getSelectedRow();
            if(fila>=0){
                codigo_inmueble.setText(tabla_inmueble.getValueAt(fila, 0).toString());
                descripcion.setText(tabla_inmueble.getValueAt(fila, 1).toString());
                precio_propietario.setText(tabla_inmueble.getValueAt(fila, 2).toString());
                fk_cedula_propietario.setSelectedItem(tabla_inmueble.getValueAt(fila, 3).toString());
            }
            else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error:"+ e);
        }
    }
    
    public void modificar(JTextField codigo_inmueble,
                        JTextField descripcion,
                        JTextField precio_propietario,
                        JComboBox fk_cedula_propietario) throws SQLException, NumberFormatException{

        setCodigoInmueble(Integer.parseInt(codigo_inmueble.getText()));
        setDescripcion(descripcion.getText());
        setPrecioPropietario(new BigDecimal(precio_propietario.getText()));
        setCedulaPropietario(Integer.parseInt(fk_cedula_propietario.getSelectedItem().toString()));

        String consulta = "UPDATE inmueble SET codigo_inmueble =?, descripcion = ?, precio_propietario = ?, fk_cedula_propietario = ? WHERE codigo_inmueble=?;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCodigoInmueble());
        cs.setString(2, getDescripcion());
        cs.setBigDecimal(3, getPrecioPropietario());
        cs.setInt(4, getCedulaPropietario());
        cs.setInt(5, getCodigoInmueble());

        cs.execute();
        JOptionPane.showMessageDialog(null, "Inmueble ha sido actualizado");
        System.out.println("si funciona");
    }
    
    public void eliminar(JTextField codigo_inmueble) throws SQLException, NumberFormatException{
        setCodigoInmueble(Integer.parseInt(codigo_inmueble.getText()));

        String consulta = "DELETE FROM inmueble WHERE inmueble.codigo_inmueble=?;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);
        cs.setInt(1, getCodigoInmueble());

        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el INMUEBLE?",
                "Confimación de elimnar agente",JOptionPane.YES_NO_OPTION);

        if (respuesta == 0){
            cs.execute();
            JOptionPane.showMessageDialog(null,"Elimino Correctamente");
        }
    }

    public boolean disponiblidadInmueble(int codigoInmueble) {
        String sql = "SELECT precio_cliente FROM inmueble WHERE codigo_inmueble = ?";

        try (Connection conn = conexionBD.establecerConnetion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigoInmueble);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getObject("precio_cliente") == null; // Disponible si es NULL
            }
            return false; // Inmueble no existe

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar disponibilidad: " + e.getMessage());
            return false;
        }
    }
}
