package io.hasura.drive_android.hasura;

import java.io.IOException;

import io.hasura.drive_android.stores.UserAuthStore;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HasuraTokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        String session = UserAuthStore.getUserSession();
        if (session == null || session.isEmpty()) {
            response = chain.proceed(request);
        } else {
            Request newRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + session)
                    .build();
            response = chain.proceed(newRequest);
        }
        return response;
    }
}
