package io.hasura.drive_android.hasura;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaison on 23/01/17.
 */

public class Hasura {

    public static HasuraAuthInterface auth;
    public static HasuraFileServiceInterface file;
    public static HasuraDBInterface db;

    public static void initialise() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HasuraTokenInterceptor())
                .addInterceptor(logging)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Endpoint.AUTH_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        auth = retrofit.create(HasuraAuthInterface.class);

        final Retrofit retrofitDB = new Retrofit.Builder()
                .baseUrl(Endpoint.DB_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        db = retrofitDB.create(HasuraDBInterface.class);

        final Retrofit retrofitFile = new Retrofit.Builder()
                .baseUrl(Endpoint.FILE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        file = retrofitFile.create(HasuraFileServiceInterface.class);
    }
}
