package com.mycompany.proyectoeventospostgres.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteModel extends PersonaModel {

    private final ConexionBD conexionBD = new ConexionBD();
    //private AgenteModel agenteModel = new AgenteModel();
    //private int getCedulaAgente = agenteModel.getCedula();
    private AgenteModel agenteModel;

    public ClienteModel(){}

    public ClienteModel(int cedula, String nombre, String telefono, String direccion, String email,  AgenteModel agenteModel) {
        super(cedula, nombre, telefono, direccion, email);
        this.agenteModel = agenteModel;
    }

    public int getCedulaAgente(){
        return agenteModel.getCedula();
    }
    public void setCedulaAgente(int cedulaAgente){
        this.agenteModel.setCedula(cedulaAgente);
    }

    public AgenteModel getAgenteModel() {
        return agenteModel;
    }

    public void setAgenteModel(AgenteModel agenteModel) {
        this.agenteModel = agenteModel;
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
                        JTextField email,
                        JTextField cedulaAgente) throws SQLException, NumberFormatException{

        String consulta = "insert into inmobilaria.cliente (cedula, nombre_completo, telefono, direccion, email, cedulaAgente) values (?,?,?,?,?,?);";

        setCedula(Integer.parseInt(cedula.getText()));
        setNombre(nombre.getText());
        setDireccion(direccion.getText());
        setTelefono(telefono.getText());
        setEmail(email.getText());
        setCedulaAgente(Integer.parseInt(cedulaAgente.getText()));

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCedula());
        cs.setString(2, getNombre());
        cs.setString(3, getTelefono());
        cs.setString(4, getDireccion());
        cs.setString(5, getEmail());
        cs.setInt(6, getCedulaAgente());
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
        modelo.addColumn("agente");

        if (tablatotal == null){
            tablatotal = new JTable();
        }

        tablatotal.setModel(modelo);
        String consulta = "SELECT * FROM inmobilaria.cliente;";

        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){

                setCedula(rs.getInt("cedula"));
                setNombre(rs.getString("nombre_completo"));
                setTelefono(rs.getString("telefono"));
                setDireccion(rs.getString("direccion"));
                setEmail(rs.getString("email"));
                setCedulaAgente(rs.getInt("agente"));

                modelo.addRow(new Object[]{ getCedula(),
                        getNombre(), getTelefono(), getDireccion(),
                        getEmail(), getCedulaAgente()});
            }
            tablatotal.setModel(modelo);
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }

    public void seleccionar(JTable tablatotal, JTextField cedula, JTextField nombre_completo, JTextField direccion, JTextField celular, JTextField correo_electronico,
                            JTextField cedulaAgente){
        try{
            int fila = tablatotal.getSelectedRow();
            if(fila>=0){
                cedula.setText(tablatotal.getValueAt(fila, 0).toString());
                nombre_completo.setText(tablatotal.getValueAt(fila, 1).toString());
                celular.setText(tablatotal.getValueAt(fila, 2).toString());
                direccion.setText(tablatotal.getValueAt(fila, 3).toString());
                correo_electronico.setText(tablatotal.getValueAt(fila, 4).toString());
                cedulaAgente.setText(tablatotal.getValueAt(fila, 5).toString());
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
                          JTextField cedulaAgente) throws SQLException, NumberFormatException{

        setCedula(Integer.parseInt(cedula.getText()));
        setNombre(nombre.getText());
        setDireccion(direccion.getText());
        setTelefono(telefono.getText());
        setEmail(email.getText());
        setCedulaAgente(Integer.parseInt(cedulaAgente.getText()));

        String consulta = "UPDATE inmobilaria.cliente SET cedula =?, nombre_completo = ?, telefono = ?, direccion = ?, email = ?, cedulaAgente = ?, WHERE cedula=?;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, getCedula());
        cs.setString(2, getNombre());
        cs.setString(3, getTelefono());
        cs.setString(4, getDireccion());
        cs.setString(5, getEmail());
        cs.setInt(6, getCedulaAgente());
        cs.setInt(7, getCedula());

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

