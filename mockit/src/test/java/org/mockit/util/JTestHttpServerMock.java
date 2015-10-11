package org.mockit.util;

import org.mockit.core.JHttpCode;
import org.mockit.core.JHttpMethod;
import org.mockit.mock.http.HttpServerMockUnitConvert;
import org.mockit.mock.http.HttpRequest;
import org.mockit.mock.http.HttpResponse;

public class JTestHttpServerMock extends HttpServerMockUnitConvert {

    @Override
    public void jInit() throws Exception {
        HttpRequest request = new HttpRequest(JHttpMethod.GET, "/test");
        HttpResponse response = new HttpResponse(JHttpCode.OK);

        request.addHeader("user", "test-user");
        response.addHeader("user", "test-user");
        response.addBody("text/http", "<strong>hello world</strong>".getBytes("UTF-8"));

        this.jAdd(request, response);

        HttpRequest request2 = new HttpRequest(JHttpMethod.PUT, "/test");
        this.jAdd(request2, (HttpRequest x) -> response);
    }

}
