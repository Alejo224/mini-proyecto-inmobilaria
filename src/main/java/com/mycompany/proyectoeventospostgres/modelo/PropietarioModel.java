
package com.mycompany.proyectoeventospostgres.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PropietarioModel extends PersonaModel {

    private final ConexionBD conexionBD = new ConexionBD();
    private int cedulaAgenteC;

    public PropietarioModel(){}

    public PropietarioModel(int cedula, String nombre, String telefono, String direccion, String email, int cedulaAgenteC) {
        super(cedula, nombre, telefono, direccion, email);
        this.cedulaAgenteC = cedulaAgenteC;
    }

    public int getCedulaAgenteC() {
        return cedulaAgenteC;
    }

    public void setCedulaAgenteC(int cedulaAgenteC) {
        this.cedulaAgenteC = cedulaAgenteC;
    }

    public void buscar(JTextField cedula){

        String consulta = "select * from propietario where cedula=?;";

        try{
            setCedula(Integer.parseInt(cedula.getText()));
            CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

            cs.execute();

            if (cs.getResultSet() == null){
                JOptionPane.showMessageDialog(null, "Se encontro el propietario");
            }
            else {
                JOptionPane.showMessageDialog(null, "NO se encontro el propietario");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error. " + e);
        } finally {
            conexionBD.ConnectionClosed();
        }


    }

    public void agregar(JTextField cedula,
                        JTextField nombre,
                        JTextField direccion,
                        JTextField telefono,
                        JTextField email,
                        JComboBox cedulaAgente) throws SQLException, NumberFormatException{

        String consulta = "insert into propietario (cedula, nombre_completo, telefono, direccion, email, cedula_agente) values (?,?,?,?,?,?);";

        setCedula(Integer.parseInt(cedula.getText()));
        setNombre(nombre.getText());
        setDireccion(direccion.getText());
        setTelefono(telefono.getText());
        setEmail(email.getText());
        setCedulaAgenteC(Integer.parseInt(cedulaAgente.getSelectedItem().toString()));

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCedula());
        cs.setString(2, getNombre());
        cs.setString(3, getTelefono());
        cs.setString(4, getDireccion());
        cs.setString(5, getEmail());
        cs.setInt(6, getCedulaAgenteC());
        cs.execute();

        JOptionPane.showMessageDialog(null, "Se guardó correctamente");

    }

    public void mostrar(JTable tablatotal){

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre Completo");
        modelo.addColumn("celular");
        modelo.addColumn("direccion");
        modelo.addColumn("correo electronico");
        modelo.addColumn("cedula agente");

        if (tablatotal == null){
            tablatotal = new JTable();
        }

        tablatotal.setModel(modelo);
        String consulta = "SELECT * FROM propietario;";

        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){

                setCedula(rs.getInt("cedula"));
                setNombre(rs.getString("nombre_completo"));
                setTelefono(rs.getString("telefono"));
                setDireccion(rs.getString("direccion"));
                setEmail(rs.getString("email"));
                setCedulaAgenteC(rs.getInt("cedula_agente"));

                modelo.addRow(new Object[]{ getCedula(),
                        getNombre(), getTelefono(), getDireccion(),
                        getEmail(), getCedulaAgenteC()});
            }
            tablatotal.setModel(modelo);
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }



    public void seleccionar(JTable tablatotal, JTextField cedula, JTextField nombre_completo, JTextField direccion, JTextField celular, JTextField correo_electronico,
                            JComboBox cedulaAgente){
        try{
            int fila = tablatotal.getSelectedRow();
            if(fila>=0){
                cedula.setText(tablatotal.getValueAt(fila, 0).toString());
                nombre_completo.setText(tablatotal.getValueAt(fila, 1).toString());
                celular.setText(tablatotal.getValueAt(fila, 2).toString());
                direccion.setText(tablatotal.getValueAt(fila, 3).toString());
                correo_electronico.setText(tablatotal.getValueAt(fila, 4).toString());
                cedulaAgente.getSelectedItem();

            }
            else{
                JOptionPane.showMessageDialog(null,"Fila no seleccionada");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error:"+ e);
        }
    }
    public void modificar(JTextField cedula,
                          JTextField nombre,
                          JTextField direccion,
                          JTextField telefono,
                          JTextField email,
                          JComboBox cedulaAgente) throws SQLException, NumberFormatException{

        setCedula(Integer.parseInt(cedula.getText()));
        setNombre(nombre.getText());
        setDireccion(direccion.getText());
        setTelefono(telefono.getText());
        setEmail(email.getText());
        setCedulaAgenteC(Integer.parseInt(cedulaAgente.getSelectedItem().toString()));

        String consulta = "UPDATE propietario SET cedula =?, nombre_completo = ?, telefono = ?, direccion = ?, email = ?, cedula_agente = ? WHERE cedula=?;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCedula());
        cs.setString(2, getNombre());
        cs.setString(3, getTelefono());
        cs.setString(4, getDireccion());
        cs.setString(5, getEmail());
        cs.setInt(6, getCedulaAgenteC());
        cs.setInt(7, getCedula());

        cs.execute();
        JOptionPane.showMessageDialog(null, "Propuietario ha sido actualizado");
        System.out.println("si funciona");
    }

    public void eliminar(JTextField cedula) throws SQLException, NumberFormatException{
        setCedula(Integer.parseInt(cedula.getText()));

        String consulta = "DELETE FROM propietario WHERE cedula=?";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);
        cs.setInt(1, getCedula());

        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar al cliente?",
                "Confimación de elimnar agente",JOptionPane.YES_NO_OPTION);

        if (respuesta == 0){
            cs.execute();
            JOptionPane.showMessageDialog(null,"Elimino Correctamente");
        }
    }
}
