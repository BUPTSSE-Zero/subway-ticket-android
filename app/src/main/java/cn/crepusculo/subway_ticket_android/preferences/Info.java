package cn.crepusculo.subway_ticket_android.preferences;

import android.content.SharedPreferences;
import android.util.Log;

import com.subwayticket.database.model.HistoryRoute;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.PreferSubwayStation;

import java.util.Arrays;
import java.util.List;

import cn.crepusculo.subway_ticket_android.util.SharedPreferencesUtils;

/**
 * The Info Class
 * <br/>
 * put more info preference here
 * <br/>
 * <p/>
 * info-
 * - user
 * - id
 * - password cache
 * - ticket
 * - @redundant ticketsCode
 * - app
 * - city
 * - not work in Beta
 * - token
 * -
 */
public class Info {

    private static final String filename = "info";
    private static Info mInstance = null;
    public String token = null;
    public User user;
    public Ticket ticket;
    public App app;
    private SharedPreferences preferences = SharedPreferencesUtils.getPreferences(filename);
    public static List<PreferRoute> preferRoutes;
    public static List<HistoryRoute> historyRoutes;
    public static List<PreferSubwayStation> preferStations;

    private Info() {
        user = new User();
        ticket = new Ticket();
        app = new App();

        user.id = preferences.getString(InfoKeys.KEYS_ID, null);
        user.password = preferences.getString(InfoKeys.KEYS_PASSWORD, null);
        app.city = preferences.getString(InfoKeys.KEYS_CITY, null);
        token = preferences.getString(InfoKeys.KEYS_TOKEN, null);
    }

    public static Info getInstance() {
        if (mInstance == null) {
            mInstance = new Info();
            Log.e("Info", "-- Bulid new instance --");
        }

        return mInstance;
    }

    public String getToken() {
        return token;
    }

    public Info setToken(String token) {
        this.token = token;
        SharedPreferencesUtils.putString(preferences, InfoKeys.KEYS_TOKEN, token);
        return this;
    }

    public class User {
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
            SharedPreferencesUtils.putString(preferences, InfoKeys.KEYS_ID, id);
            return this;
        }

        public String getPassword() {
            return password;
        }

        public User setPassword(String password) {
            this.password = password;
            SharedPreferencesUtils.putString(preferences, InfoKeys.KEYS_PASSWORD, password);
            return this;
        }


    }

    public class Ticket {
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

        public int getCount() {
            if (ticketsCode != null)
                return ticketsCode.length;
            return 0;
        }
    }

    public class App {
        private String city = null;

        private App() {
            // Required empty
        }


        public String getCity() {
            return city;
        }

        public App setCity(String city) {
            this.city = city;
            SharedPreferencesUtils.putString(preferences, InfoKeys.KEYS_CITY, city);
            return this;
        }
    }
}
