package io.hasura.drive_android.hasura;

/**
 * Created by jaison on 23/01/17.
 */

public class Endpoint {

    private static final String PROJECT_NAME = "hello70";
    public static final String AUTH_URL = "https://auth." + PROJECT_NAME + ".hasura-app.io";
    public static final String DB_URL = "https://data." + PROJECT_NAME + ".hasura-app.io";
    public static final String FILE_URL = "https://fileservice." + PROJECT_NAME + ".hasura-app.io";
    public static final String VERSION = "v1";

    public static final String LOGIN = "otp-login";
    public static final String REGISTER = "otp-signup";
    public static final String LOGOUT = "user/logout";
    public static final String QUERY = Endpoint.VERSION + "/query";
    public static final String FILE = Endpoint.VERSION + "/file";
    public static final String GET_FILE = Endpoint.FILE_URL + "/" + Endpoint.FILE + "/";
}
