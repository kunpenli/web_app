import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
public class MainWebSocket {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<MainWebSocket> webSocketSet = new CopyOnWriteArraySet<MainWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        for(MainWebSocket item: webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MainWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MainWebSocket.onlineCount--;
    }

}
/*
    @ServerEndpoint("/websocket")
    public class WebSocketTest {
        //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
        private static int onlineCount = 0;

        //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
        private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

        //与某个客户端的连接会话，需要通过它来给客户端发送数据
        private Session session;
        @OnOpen
        public void onOpen(Session session){
             this.session = session;
             webSocketSet.add(this);     //加入set中
             addOnlineCount();           //在线数加1
             System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
         }

        @OnClose
        public void onClose(){
             webSocketSet.remove(this);  //从set中删除
             subOnlineCount();           //在线数减1
             System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        }

        @OnMessage
        public void onMessage(String message, Session session) {
             System.out.println("来自客户端的消息:" + message);
             //群发消息
             for(WebSocketTest item: webSocketSet){
                     try {
                             item.sendMessage(message);
                         } catch (IOException e) {
                             e.printStackTrace();
                             continue;
                         }
                 }
         }
        @OnError
        public void onError(Session session, Throwable error){
            System.out.println("发生错误");
            error.printStackTrace();
        }

        public void sendMessage(String message) throws IOException{
         this.session.getBasicRemote().sendText(message);
         //this.session.getAsyncRemote().sendText(message);
        }

        public static synchronized int getOnlineCount() {
         return onlineCount;
        }

        public static synchronized void addOnlineCount() {
         WebSocketTest.onlineCount++;
        }

        public static synchronized void subOnlineCount() {
         WebSocketTest.onlineCount--;
        }
    }
*/
