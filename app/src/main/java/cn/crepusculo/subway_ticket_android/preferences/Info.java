package cn.crepusculo.subway_ticket_android.preferences;

import junit.framework.TestCase;

import java.util.Arrays;

public class Info {

    private static Info mInstance = null;

    public User user;
    public Ticket ticket;
    public App app;

    private Info() {
        // Required empty
    }

    public static Info getInstance(){
        if (mInstance == null) {
            mInstance = new Info();
        }

        return mInstance;
    }

    public void initTest(){
        user.setId("23323323333");
        user.setPassword("233");
        String[] s = {"13131","224242"};
        ticket.setTicketsCode(s);
        app.setCity("Beijing");
    }

    private class User{
        private String id;
        private String password;

        public User() {
            // Required empty
        }

        public String getId() {
            return id;
        }

        public User setId(String id) {
            this.id = id;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public User setPassword(String password) {
            this.password = password;
            return this;
        }


    }

    private class Ticket{
        public Ticket() {
            // Required empty
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Ticket ticket = (Ticket) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(ticketsCode, ticket.ticketsCode);

        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(ticketsCode);
        }

        public String[] getTicketsCode() {
            return ticketsCode;
        }

        public Ticket setTicketsCode(String[] ticketsCode) {
            this.ticketsCode = ticketsCode;
            return this;
        }

        public int getCount(){
            return ticketsCode.length;
        }
        private String[] ticketsCode;
    }

    private class App{
        public App() {
            // Required empty
        }

        private String city;

        public String getCity() {
            return city;
        }

        public App setCity(String city) {
            this.city = city;
            return this;
        }
    }
}
