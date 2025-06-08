package DataBase;

public class TicketDAO {
    private static final String DELETE_FOR_USER = "DELETE FROM ticket WHERE owner_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM ticket WHERE id = ANY (?)";
    private static final String DELETE_TICKET = "DELETE FROM ticket WHERE id = ?";
    private static final String TRUNCATE_TICKET = "TRUNCATE TABLE ticket";

    private static final String CREATE_TICKET = """
            INSERT INTO ticket (
                id,
                ticket_name,
                x_coordinates,
                y_coordinates,
                data,
                price,
                refundable,
                birthday,
                weight,
                passporid,
                x_location,
                y_location, 
                z_location,
                name_location,
                owner_id
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;
}
