public class Main {

    static GUI GUI;

    public static void main(String[] args) {
        String databaseURI = DBConfig.dbURI;
        MovieDB movieDB= new MovieDB(databaseURI);

        MovieController movieController = new MovieController(movieDB);

        try{
            GUI = new GUI(movieController);
        } catch (NullPointerException nP) {
            System.out.println(nP);
        }

    }
    public static void quit() { GUI.dispose(); }
}
