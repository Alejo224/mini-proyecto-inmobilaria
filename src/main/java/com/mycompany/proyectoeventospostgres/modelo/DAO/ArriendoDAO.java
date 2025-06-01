package com.mycompany.proyectoeventospostgres.modelo.DAO;

import com.mycompany.proyectoeventospostgres.modelo.ArriendoModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;

public class ArriendoDAO {

    private final ConexionBD conexionBD = new ConexionBD();

    //inserar un nuevo arriendo
    public void crearArriendo(ArriendoModel arriendo) throws SQLException{
        // Consulta SIN fecha_registro (se usará DEFAULT)
        String consulta = "INSERT INTO arriendo (codigo_inmueble, fecha_inicio, fecha_fin, " +
                "monto_mensual, comision_agente, comision_inmobiliaria, " +
                "fk_cliente, fk_agente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, arriendo.getCodigoInmueble());
        cs.setDate(2, arriendo.getFechaInicio());
        cs.setDate(3, arriendo.getFechaFin());
        cs.setBigDecimal(4, arriendo.getMontoMensual());
        cs.setBigDecimal(5, arriendo.getComisionAgente());
        cs.setBigDecimal(6, arriendo.getComisionInmobilaria());
        cs.setInt(7, arriendo.getCedulaCliente());
        cs.setInt(8, arriendo.getCedulaAgente());
        cs.setInt(8, arriendo.getCedulaAgente());
        cs.execute();

        System.out.println("Se guardo los datos correctamente");
    }

    //metodo actualizar
    public void actualizarArriendo(ArriendoModel arriendo) throws SQLException{
        String consulta = "UPDATE arriendo SET codigo_inmueble = ?, fecha_inicio = ?, fecha_fin = ?, " +
                "monto_mensual = ?, comision_agente = ?, comision_inmobiliaria = ?, " +
                "fk_cliente = ?, fk_agente = ? WHERE codigo_arriendo = ?";

        CallableStatement cs = conexionBD.establecerConnetion().prepareCall(consulta);

        cs.setInt(1, arriendo.getCodigoInmueble());
        cs.setDate(2, arriendo.getFechaInicio());
        cs.setDate(3, arriendo.getFechaFin());
        cs.setBigDecimal(4, arriendo.getMontoMensual());
        cs.setBigDecimal(5, arriendo.getComisionAgente());
        cs.setBigDecimal(6, arriendo.getComisionInmobilaria());
        cs.setInt(7, arriendo.getCedulaCliente());
        cs.setInt(8, arriendo.getCedulaAgente());
        cs.execute();

        System.out.println("Arriendo actualiado");
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


    public static void main(String[] args)  {
        ArriendoModel arriendo = new ArriendoModel(121, 99, Date.valueOf("2024-11-19"),
                1,Date.valueOf("2025-11-19"), new BigDecimal("15000.00") );

        ArriendoDAO obj = new ArriendoDAO();
        System.out.println(arriendo.toString());
        try{
            obj.crearArriendo(arriendo);
        }catch (SQLException e){
            System.out.println(e);
        }
    }

}
