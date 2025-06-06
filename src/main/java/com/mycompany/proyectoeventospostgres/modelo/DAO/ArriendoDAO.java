package com.mycompany.proyectoeventospostgres.modelo.DAO;

import com.mycompany.proyectoeventospostgres.modelo.ArriendoModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;

import javax.swing.*;
import java.sql.*;

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

        PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(consulta);

        stmt.setInt(1, arriendo.getCodigoInmueble());
        stmt.setDate(2, arriendo.getFechaInicioSQL());
        stmt.setDate(3, arriendo.getFechaFinSQL());
        stmt.setBigDecimal(4, arriendo.getMontoMensual());
        stmt.setBigDecimal(5, arriendo.getComisionAgente());
        stmt.setBigDecimal(6, arriendo.getComisionInmobilaria());
        stmt.setInt(7, arriendo.getCedulaCliente());
        stmt.setInt(8, arriendo.getCedulaAgente());
        stmt.setInt(8, arriendo.getCedulaAgente());
        stmt.execute();

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

    private boolean isDisponible(int codigoInmueble, Date nuevoInicio, Date nuevoFin){
        String sql = "SELECT COUNT(*) FROM arriendo " +
                "   WHERE codigo_inmueble = ? AND activo = 1 " +
                "AND fecha_inicio <= ? AND fecha_fin >= ? ";

        try(PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)){

            stmt.setInt(1, codigoInmueble);
            stmt.setDate(2, new Date(nuevoFin.getTime()));
            stmt.setDate(3, new Date(nuevoInicio.getTime()));

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //metodo para obtener el precio minimo (inmueble: precio_propietario)
    public double obtenerPreciominimo(int codigoInmueble) throws SQLException{

        String sql = "SELECT precio_propietario * 0.9 FROM inmueble" +
                " WHERE codigo_inmueble = ?";

        try(PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)){
            stmt.setInt(1, codigoInmueble);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public boolean validarArriendo(int codigoInmueble, java.util.Date fechaInicio, java.util.Date fechaFin,
                                double montoMensual) throws SQLException{
        // validar fecha
        if (fechaInicio == null){
            JOptionPane.showMessageDialog(null, "Seleccione una fecha de inicio valida",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        System.out.println("validar fecha listo");

        //darle formato de tipo sql (2023-01-12)
        Date fechaInicioSQL =   new java.sql.Date(fechaInicio.getTime());
        Date fechaFinSQL = new java.sql.Date(fechaFin.getTime());

        //validar si la fecha seleccionada se encuentra disponible
        if (!isDisponible(codigoInmueble, fechaInicioSQL, fechaFinSQL)) {
            JOptionPane.showMessageDialog(null, "Ya se encuentra arrendado. Fechas posibles \n"
                            + fechaInicioSQL + " y " +fechaFinSQL,
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        System.out.println("validar fecha seleccionada listo");

        // Valdar monto minimo
        double precioMinimo;
        precioMinimo = obtenerPreciominimo(codigoInmueble);

        if (montoMensual < precioMinimo){
            JOptionPane.showMessageDialog(null, "El monto $" + montoMensual +" es menor al mínimo permitdo $"+
                    precioMinimo, "Precio no permitido", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        //Todas las validaciones han sido correctas
        System.out.println("validacción correctamente");
        return true;
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

    public void getCodigoInmueble(int codigo){
        arriendoModel.setCodigoInmueble(codigo);
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
