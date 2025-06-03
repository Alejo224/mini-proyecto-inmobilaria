package com.mycompany.proyectoeventospostgres.modelo.DAO;

import com.mycompany.proyectoeventospostgres.modelo.ArriendoModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArriendoDAO {

    private final ConexionBD conexionBD = new ConexionBD();
    private ArriendoModel arriendoModel = new ArriendoModel();

    public ArriendoDAO(){}

    //inserar un nuevo arriendo
    public void crearArriendo(ArriendoModel arriendo) throws SQLException,
            NumberFormatException, IllegalArgumentException{

        // Consulta SIN fecha_registro (se usará DEFAULT)
        String consulta = "INSERT INTO arriendo (codigo_inmueble, fecha_inicio, fecha_fin, " +
                "monto_mensual, comision_agente, comision_inmobiliaria, " +
                "fk_cliente, fk_agente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, arriendo.getCodigoInmueble());
        cs.setDate(2, arriendo.getFechaInicioSQL());
        cs.setDate(3, arriendo.getFechaFinSQL());
        cs.setBigDecimal(4, arriendo.getMontoMensual());
        cs.setBigDecimal(5, arriendo.getComisionAgente());
        cs.setBigDecimal(6, arriendo.getComisionInmobilaria());
        cs.setInt(7, arriendo.getCedulaCliente());
        cs.setInt(8, arriendo.getCedulaAgente());
        cs.setInt(8, arriendo.getCedulaAgente());
        cs.execute();

        JOptionPane.showMessageDialog(null, "Arriendo registrado con éxito",
                "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
    }

    //metodo actualizar
    public void actualizarArriendo(ArriendoModel arriendo) throws SQLException{
        String consulta = "UPDATE arriendo SET codigo_inmueble = ?, fecha_inicio = ?, fecha_fin = ?, " +
                "monto_mensual = ?, comision_agente = ?, comision_inmobiliaria = ?, " +
                "fk_cliente = ?, fk_agente = ? WHERE codigo_arriendo = ?";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, arriendo.getCodigoInmueble());
        cs.setDate(2, arriendo.getFechaInicioSQL());
        cs.setDate(3, arriendo.getFechaFinSQL());
        cs.setBigDecimal(4, arriendo.getMontoMensual());
        cs.setBigDecimal(5, arriendo.getComisionAgente());
        cs.setBigDecimal(6, arriendo.getComisionInmobilaria());
        cs.setInt(7, arriendo.getCedulaCliente());
        cs.setInt(8, arriendo.getCedulaAgente());
        cs.execute();

        JOptionPane.showMessageDialog(null, "Arriendo actualizado con éxito",
                "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
    }

    public void ElimnarArriendo(int idArriendo) throws SQLException{

        String consulta = "DELETE FROM arriendo WHERE id_arriendo=? ;";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);
        cs.setInt(1, idArriendo);

        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el INMUEBLE?",
                "Confimación de elimnar agente",JOptionPane.YES_NO_OPTION);

        if (respuesta == 0){
            cs.execute();
            JOptionPane.showMessageDialog(null,"Elimino Correctamente");
        }
    }

    public void mostrarComboBoxInmueble(JComboBox comboBox){

        DefaultComboBoxModel modelo = new DefaultComboBoxModel();


        if (comboBox == null) comboBox = new JComboBox<String>();

        String consulta = "SELECT codigo_inmueble FROM inmueble;";

        try {
            Statement st = conexionBD.establecerConnetion().createStatement();
            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){

                arriendoModel.setCodigoInmueble(rs.getInt("codigo_inmueble"));
                String codigo = String.valueOf(arriendoModel.getCodigoInmueble());

                System.out.println(codigo);
                comboBox.addItem(codigo);
            }
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }

    public void mostrarComboBoxAgente(JComboBox comboBox){
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();

        if (comboBox == null) comboBox = new JComboBox<String>();

        String consulta = "SELECT cedula, nombre_completo FROM agente_comercial;";
        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){
                arriendoModel.setCedulaAgente(rs.getInt("cedula"));

                String cedula = String.valueOf(arriendoModel.getCedulaAgente());

                System.out.println(cedula);
                comboBox.addItem(cedula);
            }
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }
    public void mostrarComboBoxCliente(JComboBox comboBox){
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();

        if (comboBox == null) comboBox = new JComboBox<String>();

        String consulta = "SELECT cedula, nombre_completo FROM cliente;";
        try {
            Statement st = conexionBD.establecerConnetion().createStatement();

            ResultSet rs= st.executeQuery(consulta);

            while (rs.next()){
                arriendoModel.setCedulaCliente(rs.getInt("cedula"));

                String cedula = String.valueOf(arriendoModel.getCedulaCliente());

                System.out.println(cedula);
                comboBox.addItem(cedula);
            }
        }catch (Exception e){

            JOptionPane.showMessageDialog(null,"Error: "+ e);
        }finally {
            conexionBD.ConnectionClosed();
        }
    }


    public static void main(String[] args)  {
        /*
        ArriendoModel arriendo = new ArriendoModel(121, 99, Date.valueOf("2024-11-19"),
                1,Date.valueOf("2025-11-19"), new BigDecimal("15000.00") );

        ArriendoDAO obj = new ArriendoDAO();
        System.out.println(arriendo.toString());
        try{
            obj.crearArriendo(arriendo);
        }catch (SQLException e){
            System.out.println(e);
        }

         */
    }

}
