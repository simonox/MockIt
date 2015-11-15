package com.github.pheymann.mockitjavaapi.util;

import com.github.pheymann.mockitjavaapi.core.JHttpCode;
import com.github.pheymann.mockitjavaapi.core.JHttpMethod;
import com.github.pheymann.mockit.mock.http.HttpRequest;
import com.github.pheymann.mockit.mock.http.HttpResponse;
import com.github.pheymann.mockitjavaapi.mock.http.JHTTPServerMockUnit;

public class JTestHttpServerMock extends JHTTPServerMockUnit {

    @Override
    public void jinit() throws Exception {
        HttpRequest request = new HttpRequest(JHttpMethod.GET, "/test");
        HttpResponse response = new HttpResponse(JHttpCode.OK);

        request.addHeader("user", "test-user");
        response.addHeader("user", "test-user");
        response.addBody("text/http", "<strong>hello world</strong>".getBytes("UTF-8"));

        this.jadd(request, response);

        HttpRequest request2 = new HttpRequest(JHttpMethod.PUT, "/test");
        this.jaddWithFunction(request2, (HttpRequest x) -> response);
    }

    @Override
    public HttpResponse errorResponse(HttpRequest request) {
        return  new HttpResponse(JHttpCode.BadRequest);
    }

}
