import java.util.Scanner;
import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Program {
    private static final String URL = "jdbc:mysql://localhost/car_service?serverTimezone=Europe/Moscow&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";


    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Connected to database.");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Please choose an action: add, edit, delete, or exit");
                String input = scanner.nextLine();

                switch (input.toLowerCase()) {
                    case "add":
                        addCar(conn);
                        break;
                    case "edit":
                        editCar(conn);
                        break;
                    case "delete":
                        deleteCar(conn);
                        break;
                    case "exit":
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed...");
            e.printStackTrace();
        }
    }

    private static void addCar(Connection conn) throws SQLException {
        System.out.println("Please enter the brand of the car:");
        Scanner scanner = new Scanner(System.in);
        String brand = scanner.nextLine();
        System.out.println("Please enter the model of the car:");
        String model = scanner.nextLine();
        System.out.println("Please enter the year of the car:");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.println("Please enter the type of the car:");
        String carType = scanner.nextLine();
        System.out.println("Please enter the VIN of the car:");
        String vin = scanner.nextLine();

        String sqlCommand = "INSERT INTO Cars(Brand, Model, Year, Car_type, VIN) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
            statement.setString(1, brand);
            statement.setString(2, model);
            statement.setInt(3, year);
            statement.setString(4, carType);
            statement.setString(5, vin);
            int rows = statement.executeUpdate();
            System.out.printf("%d row(s) added.%n", rows);
        }
    }

    private static void editCar(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the ID of the car to edit:");
        int id = Integer.parseInt(scanner.nextLine());

        String sqlCommand = "SELECT * FROM Cars WHERE Id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("No car found with that ID.");
                    return;
                }

                System.out.println("Please enter the new brand of the car (leave blank to keep existing value):");
                String brand = scanner.nextLine();
                if (!brand.isEmpty()) {
                    resultSet.updateString("Brand", brand);
                }

                System.out.println("Please enter the new model of the car (leave blank to keep existing value):");
                String model = scanner.nextLine();
                if (!model.isEmpty()) {
                    resultSet.updateString("Model", model);
                }

                System.out.println("Please enter the new year of the car (leave blank to keep existing value):");
                String yearStr = scanner.nextLine();
                if (!yearStr.isEmpty()) {
                    int year = Integer.parseInt(yearStr);
                    resultSet.updateInt("Year", year);
                }

                System.out.println("Please enter the new type of the car (leave blank to keep existing value):");
                String carType = scanner.nextLine();
                if (!carType.isEmpty()) {
                    resultSet.updateString("Car_type", carType);
                }

                System.out.println("Please enter the new VIN of the car (leave blank to keep existing value):");
                String vin = scanner.nextLine();
                if (!vin.isEmpty()) {
                    resultSet.updateString("VIN", vin);
                }

                resultSet.updateRow();
                System.out.println("Car updated successfully.");
            }
        }
    }

    private static void deleteCar(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the ID of the car to delete:");
        int id = Integer.parseInt(scanner.nextLine());

        String sqlCommand = "DELETE FROM Cars WHERE Id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            System.out.printf("%d row(s) deleted.%n", rows);
        }
    }

        /*try{
            String url = "jdbc:mysql://localhost/car_service?serverTimezone=Europe/Moscow&useSSL=false";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            String sqlCommand = "CREATE TABLE cars (Id INT PRIMARY KEY AUTO_INCREMENT, Brand VARCHAR(20), Model VARCHAR(20), Year INT, Car_type VARCHAR(20), VIN VARCHAR(20))";

            try (Connection conn = DriverManager.getConnection(url, username, password)){

                Statement statement = conn.createStatement();
                statement.executeUpdate(sqlCommand);
                System.out.println("Database has been created!");

                int rows = statement.executeUpdate("INSERT Cars(Brand, Model, Year, Car_type, VIN) VALUES ('Toyota', 'Camry', 2015, 'Седан', 'JTNBE46KX0321642')," +
                        "('Audi', 'A3', 2018, 'Хэтчбек', 'WAUZZZ8V3JA011475')," +
                        "('BMW', 'X5', 2012, 'Внедорожник', '5UXZV4C59D0Gxxxxx')," +
                        "('Mercedes-Benz', 'C-Class', 2019, 'Седан', 'WDDWF4HB8KRxxxxxx')," +
                        "('Ford', 'Mustang', 2014, 'Купе', '1ZVBP8CF3E52xxxxx')," +
                        "('Volkswagen', 'Golf', 2016, 'Хэтчбек', 'WVWZZZAUZGPxxxxxx')," +
                        "('Porsche', 'Cayenne', 2015, 'Внедорожник', 'WP1AD2AP6FLAxxxxx')," +
                        "('Jeep', 'Wrangler', 2017, 'Внедорожник', '1C4BJWDG0HLxxxxxx')," +
                        "('Renault', 'Logan', 2010, 'Седан', 'VF1BLSB0V5Fxxxxxx')," +
                        "('Kia', 'Sorento', 2020, 'Внедорожник', 'KNARG81B9Lxxxxxx')," +
                        "('Opel', 'Corsa', 2008, 'Хэтчбек', 'W0L0XCFE08xxxxxx')," +
                        "('Hyundai', 'i30', 2013, 'Хэтчбек', 'KMHDH41DBEUxxxxxx')," +
                        "('Nissan', 'GT-R', 2016, 'Купе', 'JN1AR5EF3GMxxxxxx')," +
                        "('Subaru', 'Impreza', 2009, 'Седан', 'JF1GE7ED0Axxxxxxx')," +
                        "('Mazda', 'CX-5', 2017, 'Внедорожник', 'JM3KFADL1Hxxxxxxx')," +
                        "('Chevrolet', 'Camaro', 2015, 'Купе', '2G1FB1E32F9xxxxxx')," +
                        "('Mitsubishi', 'Lancer', 2010, 'Седан', 'JA3AU26U4AUxxxxxx')," +
                        "('Honda', 'CR-V', 2012, 'Внедорожник', 'JHLRE4H51CCxxxxxx')," +
                        "('Peugeot', '308', 2014, 'Хэтчбек', 'VF3LPAFSCxxxxxxx')," +
                        "('Lexus', 'LS', 2018, 'Седан', 'JTHB51FF1J5xxxxxx')");

                System.out.printf("%d row(s) deleted", rows);
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }*/



    private String url;
    private String username;
    private String password;

    public Program(String url, String username, String password, DataSource dataSource) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.dataSource = dataSource;
    }

    // методы для установки соединения с базой данных
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // методы для закрытия соединения с базой данных
    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // методы для выполнения запросов к базе данных
    public ResultSet executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public int executeUpdate(Connection connection, String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement.executeUpdate();
    }

    private final DataSource dataSource;

    public Program (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void selectFromTable() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM car_service")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String carMake = rs.getString("car_brand");
                String carModel = rs.getString("car_model");
                int year = rs.getInt("year");
                String carType = rs.getString("car_type");
                String carVIN = rs.getString("car_vin");
                System.out.println("Id: " + id + ", Car Brand: " + carMake + ", Car Model: " + carModel +
                        ", Year: " + year + ", Car type: " + carType + ", VIN: " + carVIN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String fileName) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM car_service")) {

            FileWriter writer = new FileWriter(fileName);

            while (rs.next()) {
                int id = rs.getInt("id");
                String carMake = rs.getString("car_make");
                String carModel = rs.getString("car_model");
                int year = rs.getInt("year");
                String carType = rs.getString("car_type");
                String carVIN = rs.getString("car_vin");

                writer.write("Id: " + id + ", Car Brand: " + carMake + ", Car Model: " + carModel +
                ", Year: " + year + ", Car type: " + carType + ", VIN: " + carVIN + "\n");
            }

            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromTable(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM car_service WHERE id = ?")) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            System.out.printf("Deleted %d rows%n", rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

/*import java.sql.*;

public class Program{

    public static void main(String[] args) {
        try{
            String url = "jdbc:mysql://localhost/store?serverTimezone=Europe/Moscow&useSSL=false";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            try (Connection conn = DriverManager.getConnection(url, username, password)){

                Statement statement = conn.createStatement();
                int rows = statement.executeUpdate("INSERT Products(ProductName, Price) VALUES ('iPhone X', 76000)," +
                        "('Galaxy S9', 45000), ('Nokia 9', 36000)");
                System.out.printf("Added %d rows", rows);
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }
}*/

/*import java.sql.*;

public class Program{

    public static void main(String[] args) {
        try{
            String url = "jdbc:mysql://localhost/store?serverTimezone=Europe/Moscow&useSSL=false";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            // команда создания таблицы
            String sqlCommand = "CREATE TABLE products (Id INT PRIMARY KEY AUTO_INCREMENT, ProductName VARCHAR(20), Price INT)";

            try (Connection conn = DriverManager.getConnection(url, username, password)){

                Statement statement = conn.createStatement();
                // создание таблицы
                statement.executeUpdate(sqlCommand);

                System.out.println("Database has been created!");
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }
}*/

//public class Program{
//
//    public static void main(String[] args) {
//        //java -classpath c:\Java\mysql-connector-java-8.0.11.jar;c:\Java Program
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
//            System.out.println("Connection succesfull!");
//        }
//        catch(Exception ex){
//            System.out.println("Connection failed...");
//
//            System.out.println(ex);
//        }
//
//    }
//}
