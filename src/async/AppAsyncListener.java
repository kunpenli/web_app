package async;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异步监听器
 */
@WebListener
public class AppAsyncListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        System.out.println("AppAsyncListener complete");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        ServletResponse response = event.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write("TimeOut Error in Processing");
        out.flush();
        out.close();

    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        System.out.println("AppAsyncListener error");
        ServletResponse response = event.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write("error on processing");
        out.flush();
        out.close();
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        System.out.println("AppAsyncListener start");
    }
}
