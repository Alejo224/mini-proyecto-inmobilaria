/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.modelo;

import java.math.BigDecimal;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
    private BigDecimal precioCliente;
    private int cedulaPropietario;
    private int cedulaCliente;
    
    public InmuebleModel(){
    }
    
    public InmuebleModel(int codigo_inmueble, String descripcion, BigDecimal precio_propietario, BigDecimal precio_cliente, int fk_cedula_propietario, int fk_cedula_cliente){
        this.codigoInmueble = codigo_inmueble;
        this.descripcion = descripcion;
        this.precioPropietario = precio_propietario;
        this.precioCliente = precio_cliente;
        this.cedulaPropietario = fk_cedula_propietario;
        this.cedulaCliente = fk_cedula_cliente;
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

    public BigDecimal getPrecioCliente() {
        return precioCliente;
    }

    public void setPrecioCliente(BigDecimal precioCliente) {
        this.precioCliente = precioCliente;
    }

    public int getCedulaPropietario() {
        return cedulaPropietario;
    }

    public void setCedulaPropietario(int cedulaPropietario) {
        this.cedulaPropietario = cedulaPropietario;
    }

    public int getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(int cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }
    
//-----------------------------------------------------------------------    
    
    public void mostrar(JTable tablatotal){

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Codigo inmueble");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Precio del propietario");
        modelo.addColumn("Precio del cliente");
        modelo.addColumn("cedula propietario");
        modelo.addColumn("cedula cliente");

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
                setPrecioCliente(rs.getBigDecimal("precio_cliente"));
                setCedulaPropietario(rs.getInt("fk_cedula_propietario"));
                setCedulaCliente(rs.getInt("fk_cliente"));

                modelo.addRow(new Object[]{ getCodigoInmueble(),
                        getDescripcion(), getPrecioPropietario(), getPrecioCliente(),
                        getCedulaPropietario(), getCedulaCliente()});
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
                        JTextField fk_cedula_propietario ) throws SQLException, NumberFormatException{

        String consulta = "insert into inmueble (codigo_inmueble,descripcion, precio_propietario, fk_cedula_propietario) values (?,?,?,?);";
        System.out.println("entro a crear");
        setCodigoInmueble(Integer.parseInt(codigo_inmueble.getText()));
        setDescripcion(descripcion.getText());
        setPrecioPropietario(new BigDecimal(precio_propietario.getText()));
        setCedulaPropietario(Integer.parseInt(fk_cedula_propietario.getText()));

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
                        JTextField fk_cedula_propietario){
        try{
            int fila = tabla_inmueble.getSelectedRow();
            if(fila>=0){
                codigo_inmueble.setText(tabla_inmueble.getValueAt(fila, 0).toString());
                descripcion.setText(tabla_inmueble.getValueAt(fila, 1).toString());
                precio_propietario.setText(tabla_inmueble.getValueAt(fila, 2).toString());
                fk_cedula_propietario.setText(tabla_inmueble.getValueAt(fila, 4).toString());
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
                        JTextField fk_cedula_propietario) throws SQLException, NumberFormatException{

        setCodigoInmueble(Integer.parseInt(codigo_inmueble.getText()));
        setDescripcion(descripcion.getText());
        setPrecioPropietario(new BigDecimal(precio_propietario.getText()));
        setCedulaPropietario(Integer.parseInt(fk_cedula_propietario.getText()));

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
    
    public void arrendarInmueble(JTextField codigo_inmueble,
                        JTextField cedula_cliente,
                        JTextField precio_cliente) throws SQLException, NumberFormatException{

        setCodigoInmueble(Integer.parseInt(codigo_inmueble.getText()));
        
        setPrecioCliente(new BigDecimal(precio_cliente.getText()));
        setCedulaCliente(Integer.parseInt(cedula_cliente.getText()));

        String consulta = "UPDATE inmueble SET codigo_inmueble=?, fk_cliente = ?, precio_cliente = ? WHERE codigo_inmueble=?;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCodigoInmueble());
        cs.setInt(2, getCedulaCliente());
        cs.setBigDecimal(3, getPrecioCliente());
        cs.setInt(4, getCodigoInmueble());
        
        cs.execute();
        JOptionPane.showMessageDialog(null, "Inmueble ha sido arrendado");
        System.out.println("si funciona");
    }

/*
    public void buscar(JTextField cedula){

        String consulta = "select * from inmueble where codigo_inmueble=?;";

        try{
            //setCedula(Integer.parseInt(cedula.getText()));
            CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

            cs.execute();

            if (cs.getResultSet() == null){
                JOptionPane.showMessageDialog(null, "Se encontro el cliente");
            }
            else {
                JOptionPane.showMessageDialog(null, "NO se encontro el cliente");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error. " + e);
        } finally {
//            conexionBD.ConnectionClosed();
        }
    }
*/
}
