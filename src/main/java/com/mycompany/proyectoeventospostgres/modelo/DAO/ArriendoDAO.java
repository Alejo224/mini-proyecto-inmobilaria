package com.mycompany.proyectoeventospostgres.modelo.DAO;

import com.mycompany.proyectoeventospostgres.modelo.ArriendoModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ArriendoDAO extends ArriendoModel{

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
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "Arriendo registrado con éxito",
                "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
    }

    //metodo actualizar
    public boolean actualizarArriendo(JDateChooser fechaInicio,
                                   JDateChooser fechaFin, JTextField montoMensual, JComboBox boxCodigoCliente, JComboBox boxCodigoAgente,
                                      JComboBox boxCodigoInmueble, int idArriendo)
            throws SQLException, NumberFormatException, IllegalArgumentException{

        //Obener los datos desde la ventana
        int cdInmueble = Integer.parseInt(boxCodigoInmueble.getSelectedItem().toString());
        int cdAgente = Integer.parseInt(boxCodigoAgente.getSelectedItem().toString());
        int cdCliente = Integer.parseInt(boxCodigoCliente.getSelectedItem().toString());
        //int idArriendo = arriendoModel.getIdArriendo();
        java.util.Date dateStart = fechaInicio.getDate();
        java.util.Date dateEnd = fechaFin.getDate();
        BigDecimal monto = new BigDecimal(montoMensual.getText());

        //validar si los datos cumple con los requisitos
        if (!validarArriendo(idArriendo, cdInmueble, dateStart, dateEnd, monto.doubleValue())){
            return false;
        }

        arriendoModel = new ArriendoModel(cdCliente, cdInmueble, dateStart, cdAgente, dateEnd, monto);

        // consulta de sql
        String consulta = "UPDATE arriendo SET id_arriendo =  ? ,codigo_inmueble = ?, fecha_inicio = ?, fecha_fin = ?, " +
                "monto_mensual = ?, comision_agente = ?, comision_inmobiliaria = ?, " +
                "fk_cliente = ?, fk_agente = ? WHERE id_arriendo = ?";

        PreparedStatement stmt = conexionBD.establecerConnetion().prepareCall(consulta);
        //enviar los datos a la consulta sql
        stmt.setInt(1, idArriendo);
        stmt.setInt(2, arriendoModel.getCodigoInmueble());
        stmt.setDate(3,arriendoModel.getFechaInicioSQL());
        stmt.setDate(4, arriendoModel.getFechaFinSQL());
        stmt.setBigDecimal(5, arriendoModel.getMontoMensual());
        stmt.setBigDecimal(6, arriendoModel.getComisionAgente());
        stmt.setBigDecimal(7, arriendoModel.getComisionInmobilaria());
        stmt.setInt(8, arriendoModel.getCedulaCliente());
        stmt.setInt(9, arriendoModel.getCedulaAgente());
        stmt.setInt(10, idArriendo);
        stmt.execute();

        JOptionPane.showMessageDialog(null, "Arriendo actualizado con éxito",
                "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public boolean finalizarArriendo(int idArriendo, String motivo){

        Connection connection = null;
        try{
            connection = conexionBD.establecerConnetion();
            connection.setAutoCommit(false); //Iniciar transacción

            // mover a historico el arriendo a elimnar
            String sqlInsert = "INSERT INTO historico_arriendos " +
                    "SELECT id_arriendo, codigo_inmueble, fecha_inicio, " +
                    "fecha_fin, monto_mensual, comision_agente, " +
                    "comision_inmobiliaria, fk_cliente, fk_agente, " +
                    "fecha_registro, NOW(), ? " +  // fecha_finalizacion y motivo
                    "FROM arriendo WHERE id_arriendo = ?";

            PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert);
            stmtInsert.setString(1, motivo);
            stmtInsert.setInt(2, idArriendo);

            //Eliminar el arriendo activo que se encuentra en la tabla de arriendo
            String sqlDelete = "DELETE FROM arriendo WHERE id_arriendo = ?";
            PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete);
            stmtDelete.setInt(1, idArriendo);

            int moved = stmtInsert.executeUpdate();
            int deleted = stmtDelete.executeUpdate();

            if (moved == 1 && deleted == 1){
                connection.commit();    // Confirma ambas operaciones
                System.out.println("Operacion exitosa");
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            if(connection != null) try { connection.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            if(connection != null) try { connection.setAutoCommit(true); connection.close(); } catch (SQLException e) {}
        }
    }

    private boolean isDisponible(int idArriendoActual, int codigoInmueble, Date nuevoInicio, Date nuevoFin) {
        String sql = "SELECT COUNT(*) FROM arriendo " +
                "WHERE codigo_inmueble = ? AND activo = 1 ";

        // Si es edición, excluye el id actual
        if (idArriendoActual != -1) {
            sql += "AND id_arriendo != ? ";
        }

        sql += "AND (" +
                "     (fecha_inicio <= ? AND fecha_fin >= ?) " +
                "  OR (fecha_inicio <= ? AND fecha_fin >= ?) " +
                "  OR (fecha_inicio >= ? AND fecha_fin <= ?) " +
                ")";
        try (PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)) {
            int index = 1;
            stmt.setInt(index++, codigoInmueble);

            if (idArriendoActual != -1) {
                stmt.setInt(index++, idArriendoActual);
            }

            stmt.setDate(index++, new Date(nuevoFin.getTime()));
            stmt.setDate(index++, new Date(nuevoInicio.getTime()));
            stmt.setDate(index++, new Date(nuevoFin.getTime()));
            stmt.setDate(index++, new Date(nuevoInicio.getTime()));
            stmt.setDate(index++, new Date(nuevoInicio.getTime()));
            stmt.setDate(index++, new Date(nuevoFin.getTime()));

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //metodo para obtener el precio minimo (inmueble: precio_propietario)
    public double obtenerPreciominimo(int codigoInmueble) throws SQLException{

        String sql = "SELECT precio_propietario * 0.9 AS precio_minimo FROM inmueble" +
                " WHERE codigo_inmueble = ?";

        try(PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)){
            stmt.setInt(1, codigoInmueble);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public boolean validarArriendo(int idArriendo, int codigoInmueble, java.util.Date fechaInicio, java.util.Date fechaFin,
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
        if (!isDisponible(idArriendo, codigoInmueble, fechaInicioSQL, fechaFinSQL)) {
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

    public boolean mostrarArriendos(int codigoInmueble, JTable tablaArriendos){
        String sql = "SELECT id_arriendo, fk_agente, fk_cliente, fecha_inicio, fecha_fin, monto_mensual, comision_agente" +
                " FROM arriendo WHERE codigo_inmueble = ?;";

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Id arriendo");
        model.addColumn("C. Agente");
        model.addColumn("C. Cliente");
        model.addColumn("Fecha Inicio");
        model.addColumn("Fecha Fin");
        model.addColumn( "Monto Mensual");

        if (tablaArriendos == null){
            tablaArriendos = new JTable();
        }
        boolean hayResultados = false;

        try(PreparedStatement stmt = conexionBD.establecerConnetion().prepareStatement(sql)){
            stmt.setInt(1, codigoInmueble);
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                hayResultados = true;
                arriendoModel.setIdArriendo(rs.getInt("id_arriendo"));
                arriendoModel.setCedulaAgente(rs.getInt("fk_agente"));
                arriendoModel.setCedulaCliente(rs.getInt("fk_cliente"));
                arriendoModel.setFechaInicio(rs.getDate("fecha_inicio"));
                arriendoModel.setFechaFin(rs.getDate("fecha_fin"));
                arriendoModel.setMontoMensual(BigDecimal.valueOf(rs.getDouble("monto_mensual")));

                System.out.println(arriendoModel.toString());

                model.addRow(new Object[]{ arriendoModel.getIdArriendo(), arriendoModel.getCedulaAgente(), arriendoModel.getCedulaCliente(), arriendoModel.getFechaInicioSQL(), arriendoModel.getFechaFinSQL(),
                arriendoModel.getMontoMensual()});
            }
            tablaArriendos.setModel(model);
            return hayResultados;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return hayResultados;
        }
    }

    public void seleccionar(JTable tablaArriendos, JDateChooser fechaInicio,
                            JDateChooser fechaFin, JTextField montoMensual, JComboBox boxCodigoCliente, JComboBox boxCodigoAgente){
        int fila = tablaArriendos.getSelectedRow();
        if (fila >= 0){
            String codigoInmueble = tablaArriendos.getValueAt(fila, 0).toString();
            String codigoAgente = tablaArriendos.getValueAt(fila, 1).toString();
            String codigoCliente = tablaArriendos.getValueAt(fila, 2).toString();
            String fechaInicioTexto = tablaArriendos.getValueAt(fila, 3).toString();
            String fechaFinTexto = tablaArriendos.getValueAt(fila, 4).toString();
            montoMensual.setText(tablaArriendos.getValueAt(fila, 5).toString());

            boxCodigoAgente.setSelectedItem(codigoAgente);
            boxCodigoCliente.setSelectedItem(codigoCliente);
            setIdArriendo(Integer.parseInt(codigoInmueble));

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

            try {
                java.util.Date fechaStart = formato.parse(fechaInicioTexto);
                fechaInicio.setDate(fechaStart);
                java.util.Date fechaEnd = formato.parse(fechaFinTexto);
                fechaFin.setDate(fechaEnd);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
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

}
