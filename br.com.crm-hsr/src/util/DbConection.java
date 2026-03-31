package util;


import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
                String user = "rm560771";
                String password = "Fiap25";

                connection = DriverManager.getConnection(url, user, password);
            }
        }
        catch (SQLException e){
            throw new DatabaseException("Erro ao conectar ao Banco de Dados: " + e.getMessage());
        }
        return connection;
    }
}