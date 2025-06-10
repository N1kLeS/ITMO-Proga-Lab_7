package DataBase;

import models.Ticket;
import models.Person;
import models.Coordinates;
import models.Location;
import models.TicketType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private static final Logger logger = LogManager.getLogger(TicketDAO.class);
    private final Connection connection;

    private static final String DELETE_FOR_USER = "DELETE FROM ticket WHERE owner_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM ticket WHERE id = ANY (?)";
    private static final String DELETE_TICKET = "DELETE FROM ticket WHERE id = ?";
    private static final String TRUNCATE_TICKET = "TRUNCATE TABLE ticket";
    private static final String GET_ALL_TICKETS = "SELECT * FROM ticket";
    private static final String GET_TICKET_BY_ID = "SELECT * FROM ticket WHERE id = ?";
    private static final String GET_TICKETS_BY_OWNER = "SELECT * FROM ticket WHERE owner_id = ?";
    private static final String UPDATE_TICKET = """
            UPDATE ticket SET 
                ticket_name = ?,
                x_coordinates = ?,
                y_coordinates = ?,
                data = ?,
                price = ?,
                refundable = ?,
                tickettype = ?,
                birthday = ?,
                weight = ?,
                passportid = ?,
                x_location = ?,
                y_location = ?,
                z_location = ?,
                name_location = ?,
                owner_id = ?
            WHERE id = ?
            """;

    private static final String CREATE_TICKET = """
            INSERT INTO ticket (
                ticket_name,
                x_coordinates,
                y_coordinates,
                data,
                price,
                refundable,
                tickettype,
                birthday,
                weight,
                passportid,
                x_location,
                y_location, 
                z_location,
                name_location,
                owner_id
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

    public TicketDAO(Connection connection) {
        this.connection = connection;
    }

    public void createTicket(Ticket ticket) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_TICKET)) {
            stmt.setString(1, ticket.getName());
            
            Coordinates coordinates = ticket.getCoordinates();
            stmt.setInt(2, coordinates.getX());
            stmt.setInt(3, coordinates.getY());
            
            stmt.setString(4, ticket.getCreationDate().toString());
            stmt.setInt(5, ticket.getPrice());
            stmt.setBoolean(6, ticket.getRefundable());
            stmt.setString(7, ticket.getType().toString());

            Person person = (Person) ticket.getPerson();
            stmt.setString(8, person.getBirthday());
            stmt.setInt(9, person.getWeight());
            stmt.setString(10, person.getPassportID());

            stmt.setInt(11, person.getLocation().getX());
            stmt.setInt(12, person.getLocation().getY());
            stmt.setFloat(13, person.getLocation().getZ());
            stmt.setString(14, person.getLocation().getName());
            
            stmt.setLong(15, ticket.getOwnerUser().getId());

            stmt.executeUpdate();
            connection.commit();
            logger.info("Ticket with ID {} created successfully", ticket.getId());
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error creating ticket: {}", e.getMessage());
            throw e;
        }
    }

    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(GET_ALL_TICKETS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    public Ticket getTicketById(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(GET_TICKET_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTicket(rs);
                }
            }
        }
        return null;
    }

    public List<Ticket> getTicketsByOwner(Long ownerId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(GET_TICKETS_BY_OWNER)) {
            stmt.setLong(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToTicket(rs));
                }
            }
        }
        return tickets;
    }

    public void updateTicket(Ticket ticket) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_TICKET)) {
            stmt.setString(1, ticket.getName());
            
            Coordinates coordinates = ticket.getCoordinates();
            stmt.setInt(2, coordinates.getX());
            stmt.setInt(3, coordinates.getY());
            
            stmt.setString(4, ticket.getCreationDate().toString());
            stmt.setInt(5, ticket.getPrice());
            stmt.setBoolean(6, ticket.getRefundable());
            stmt.setString(7, ticket.getType().toString());

            Person person = (Person) ticket.getPerson();
            stmt.setString(8, person.getBirthday());
            stmt.setInt(9, person.getWeight());
            stmt.setString(10, person.getPassportID());

            stmt.setInt(11, person.getLocation().getX());
            stmt.setInt(12, person.getLocation().getY());
            stmt.setFloat(13, person.getLocation().getZ());
            stmt.setString(14, person.getLocation().getName());
            
            stmt.setLong(15, ticket.getOwnerUser().getId());
            stmt.setLong(16, ticket.getId());

            stmt.executeUpdate();
            connection.commit();
            logger.info("Ticket with ID {} updated successfully", ticket.getId());
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error updating ticket: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteTicket(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_TICKET)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            connection.commit();
            logger.info("Ticket with ID {} deleted successfully", id);
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error deleting ticket: {}", e.getMessage());
            throw e;
        }
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("ticket_name");
        Coordinates coordinates = new Coordinates(
            rs.getInt("x_coordinates"),
            rs.getInt("y_coordinates")
        );
        ZonedDateTime creationDate = ZonedDateTime.parse(rs.getString("data"));
        int price = rs.getInt("price");
        Boolean refundable = rs.getBoolean("refundable");
        TicketType ticketType = TicketType.valueOf(rs.getString("tickettype"));
        
        Person person = new Person(
            rs.getDate("birthday"),
            rs.getInt("weight"),
            rs.getString("passportid"),
            new Location(
                rs.getInt("x_location"),
                rs.getInt("y_location"),
                rs.getFloat("z_location"),
                rs.getString("name_location")
            )
        );
         long userID = rs.getLong("owner_id");

        return new Ticket(id, name, coordinates, creationDate, price, refundable, ticketType, person, userID);
    }
}
