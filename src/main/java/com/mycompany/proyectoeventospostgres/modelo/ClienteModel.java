package com.mycompany.proyectoeventospostgres.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteModel extends PersonaModel {

    private final ConexionBD conexionBD = new ConexionBD();
    private AgenteModel agenteModel = new AgenteModel();
    private String cedulaAgente = String.valueOf(agenteModel.getCedula());

    public ClienteModel(){}

    public ClienteModel(int cedula, String nombre, String telefono, String direccion, String email,  String cedulaAgente) {
        super(cedula, nombre, telefono, direccion, email);
        this.cedulaAgente = cedulaAgente;
    }

    public void buscar(JTextField cedula){

        String consulta = "select * from inmobilaria.cliente where cedula=?;";

        try{
            setCedula(Integer.parseInt(cedula.getText()));
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
            conexionBD.ConnectionClosed();
        }


    }

    public void agregar(JTextField cedula,
                        JTextField nombre,
                        JTextField direccion,
                        JTextField telefono,
                        JTextField email) throws SQLException, NumberFormatException{

        String consulta = "insert into inmobilaria.cliente (cedula, nombre_completo, telefono, direccion, email) values (?,?,?,?,?);";

        setCedula(Integer.parseInt(cedula.getText()));
        setNombre(nombre.getText());
        setDireccion(direccion.getText());
        setTelefono(telefono.getText());
        setEmail(email.getText());

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCedula());
        cs.setString(2, getNombre());
        cs.setString(3, getTelefono());
        cs.setString(4, getDireccion());
        cs.setString(5, getEmail());

        cs.execute();

        JOptionPane.showMessageDialog(null, "Se guardó correctamente");

    }

    public void mostrarAgentesComerciales(JTable tablaTotalAgentes){

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre Completo");
        modelo.addColumn("celular");
        modelo.addColumn("direccion");
        modelo.addColumn("correo electronico");

        if (tablaTotalAgentes == null){
            tablaTotalAgentes = new JTable();
        }

        tablaTotalAgentes.setModel(modelo);
        String consulta = "SELECT * FROM inmobilaria.agente_comercial;";

        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){

                setCedula(rs.getInt("cedula"));
                setNombre(rs.getString("nombre_completo"));
                setTelefono(rs.getString("telefono"));
                setDireccion(rs.getString("direccion"));
                setEmail(rs.getString("email"));

                modelo.addRow(new Object[]{ getCedula(),
                        getNombre(), getTelefono(), getDireccion(),
                        getEmail()});
            }
            tablaTotalAgentes.setModel(modelo);
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }

    public void seleccionar(JTable tablaTotalAgentes, JTextField cedula, JTextField nombre_completo, JTextField direccion, JTextField celular, JTextField correo_electronico ){
        try{
            int fila = tablaTotalAgentes.getSelectedRow();
            if(fila>=0){
                cedula.setText(tablaTotalAgentes.getValueAt(fila, 0).toString());
                nombre_completo.setText(tablaTotalAgentes.getValueAt(fila, 1).toString());
                celular.setText(tablaTotalAgentes.getValueAt(fila, 2).toString());
                direccion.setText(tablaTotalAgentes.getValueAt(fila, 3).toString());
                correo_electronico.setText(tablaTotalAgentes.getValueAt(fila, 4).toString());
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
                          JTextField email) throws SQLException, NumberFormatException{

        setCedula(Integer.parseInt(cedula.getText()));
        setNombre(nombre.getText());
        setDireccion(direccion.getText());
        setTelefono(telefono.getText());
        setEmail(email.getText());

        String consulta = "UPDATE inmobilaria.cliente SET cedula =?, nombre_completo = ?, telefono = ?, direccion = ?, email = ? WHERE cedula=?;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCedula());
        cs.setString(2, getNombre());
        cs.setString(3, getTelefono());
        cs.setString(4, getDireccion());
        cs.setString(5, getEmail());

        cs.setInt(6, getCedula());

        cs.execute();
        JOptionPane.showMessageDialog(null, "Cliente ha sido actualizado");
        System.out.println("si funciona");
    }

    public void eliminar(JTextField cedula) throws SQLException, NumberFormatException{
        setCedula(Integer.parseInt(cedula.getText()));

        String consulta = "DELETE FROM cliente WHERE cliente.cedula=?";

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

