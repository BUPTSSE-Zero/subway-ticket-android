package cn.crepusculo.subway_ticket_android.preferences;

import android.util.Log;

import junit.framework.TestCase;

import java.util.Arrays;

public class Info {

    private static Info mInstance = null;

    public User user;
    public Ticket ticket;
    public App app;

    private Info() {
        user = new User();
        ticket = new Ticket();
        app = new App();
        // Required empty
    }

    public static Info getInstance(){
        if (mInstance == null) {
            mInstance = new Info();
            Log.e("Info","-- Bulid new instance --");
        }

        return mInstance;
    }

    public void initTest(){
        mInstance.user.setId("23323323333");
        mInstance.user.setPassword("233");
        String[] s = {"13131","224242"};
        mInstance.ticket.setTicketsCode(s);
        mInstance.app.setCity("Beijing");
    }

    public class User{
        private String id = null;
        private String password = null;

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

    public class Ticket{
        private String[] ticketsCode = null;

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
            if (ticketsCode != null)
                return ticketsCode.length;
            return 0;
        }
    }

    public class App{
        private String city = null;

        private App() {
            // Required empty
        }


        public String getCity() {
            return city;
        }

        public App setCity(String city) {
            this.city = city;
            return this;
        }
    }
}
