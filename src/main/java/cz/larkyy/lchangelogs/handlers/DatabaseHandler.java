package cz.larkyy.lchangelogs.handlers;

import cz.larkyy.lchangelogs.LChangeLogs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class DatabaseHandler {
    private final LChangeLogs main;

    private final SQLite database;

    public DatabaseHandler(LChangeLogs main) {
        this.main = main;
        this.database = new SQLite(main);
    }

    public void setData(UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO Players (UUID,Size) VALUES(?,?)");
            ps.setString(1, uuid.toString());

            ps.setInt(2, getStorageHandler().getChangeLogs().size());
            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
    }

    public void decreaseUpdate() {

        int actualSize = getStorageHandler().getChangeLogs().size()+1;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;

        List<UUID> uuids = new ArrayList<>();

        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM Players WHERE Size = '"+ actualSize +"';");
            rs = ps.executeQuery();

            while (rs.next()) {
                uuids.add(UUID.fromString(rs.getString(1)));
            }

        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }

        for (UUID uuid : uuids) {
            setData(uuid);
        }
    }

    public int getData(UUID uuid) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;

        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM Players WHERE UUID = '"+ uuid +"';");
            rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(2);
            else
                return 0;

        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }

        return 0;
    }

    private StorageHandler getStorageHandler() {
        return main.getStorageHandler();
    }
}
