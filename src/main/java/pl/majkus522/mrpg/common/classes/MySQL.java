package pl.majkus522.mrpg.common.classes;

import pl.majkus522.mrpg.controllers.FilesController;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQL
{
    static Connection connection;

    public static void connect()
    {
        if (!isConnected())
        {
            try
            {
                Config config = FilesController.readJsonFile("database", Config.class);
                connection = DriverManager.getConnection("jdbc:mysql://" + config.host + ":" + config.port + "/" + config.database, config.user, config.password);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void disconnect()
    {
        if (isConnected())
        {
            try
            {
                connection.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isConnected()
    {
        return !(connection == null);
    }

    public static Connection getConnection()
    {
        return connection;
    }

    public static class Config
    {
        public String host;
        public String database;
        public String user;
        public String password;
        public int port;
    }
}